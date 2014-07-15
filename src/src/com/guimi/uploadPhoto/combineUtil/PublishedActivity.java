package com.guimi.uploadPhoto.combineUtil;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.guimi.MyMatchesActivity;
import com.guimi.R;
import com.guimi.CombinePicActivity;
import com.guimi.sqlite.GuiMiDB;
import com.guimi.uploadPhoto.HandlePic;


import android.R.bool;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.BoringLayout.Metrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class PublishedActivity extends Activity {

	private GridView noScrollgridview;
	private GridAdapter adapter;
	private TextView activity_selectimg_send;
	private GuiMiDB guimiDB;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectimg_4m);
		guimiDB = GuiMiDB.getInstance(this);
		Init();
	}

	public void Init() {
		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview_4m);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {
			

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.bmp.size()) {
					new PopupWindows(PublishedActivity.this, noScrollgridview);
				} else {
					Intent intent = new Intent(PublishedActivity.this,
							PhotoActivity.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
		activity_selectimg_send = (TextView) findViewById(R.id.activity_selectimg_send_4m);
		activity_selectimg_send.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//List<String> list = new ArrayList<String>();				
				/*for (int i = 0; i < Bimp.drr.size(); i++) {
					
					String Str = Bimp.drr.get(i).substring( 
							Bimp.drr.get(i).lastIndexOf("/") + 1,
							Bimp.drr.get(i).lastIndexOf("."));
					Log.i("Combine Send", Str+"\n");
					list.add(FileUtils.SDPATH+Str+".JPEG");				
				}*/
				for(int i = 0;i<Bimp.drr.size();i++ )
				{
					Log.i("photoPath", Bimp.drr.get(i));
				}
				//FileUtils.deleteDir();
				Intent sendIntent = new Intent(PublishedActivity.this,MyMatchesActivity.class);
				HandlePic handlePic = new HandlePic("GuiMi", PublishedActivity.this);
				String outfitPath = null;
				int size = Bimp.drr.size();
				if(size >9){
					Toast.makeText(getApplicationContext(), "搭配最多包含9张单品", Toast.LENGTH_LONG);
					return;
				}
				ArrayList<Bitmap> photoBitmap = new ArrayList<Bitmap>();
				//Toast.makeText(PublishedActivity.this, "搭配图片正在生成中", Toast.LENGTH_LONG).show();
				
				for(int i = 0;i<size;i++)
				{
					if(Bimp.bmp.get(i).isRecycled() == false)
						Bimp.bmp.get(i).recycle();
					try {
						
						photoBitmap.add(compressPhoto(Bimp.drr.get(i)));
						Log.i("bmp.bmp","压缩成功："+(i+1));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Bitmap combineBitmap = null;
				
				/*try {
					photoBitmap=compressPhotos(photoBitmap);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
				if(Bimp.drr.size() == 1){
					combineBitmap = photoBitmap.get(0);
				}else if(size == 2){
					Bitmap up,down;
					
					
					up = photoBitmap.get(0);
					down = photoBitmap.get(1); 
			        
			        double combinePicWidth = up.getWidth();
			        double downScale = (double)up.getWidth()/(double)down.getWidth();
			        
			        double downActualHeight = downScale*(double)down.getHeight();
			        double combinePicHeight = up.getHeight() + downActualHeight;
			        //Bitmap Outof Memeory Error
			        
			        
			       
			        combineBitmap = Bitmap.createBitmap((int)combinePicWidth+90, (int)combinePicHeight+45*2+30, Config.ARGB_8888); 
			        Canvas cv = new Canvas(combineBitmap); 
			        cv.drawColor(Color.argb(255, 255, 204, 204));
			        //draw bg into  
			        Matrix matrix = new Matrix();
			        matrix.setScale((float)downScale, (float)downScale);
			        Bitmap downNew = Bitmap.createBitmap(down, 0, 0, down.getWidth(), down.getHeight(),matrix,true);
			        if(down.isRecycled() == false)
			        	down.recycle();
			        cv.drawBitmap(up, 45, 45, null); 
			        cv.drawBitmap(downNew, 45, (float)(combinePicHeight+120-45-downNew.getHeight()),null);
			        //draw fg into  
			       
			        //save all clip  
			        cv.save(Canvas.ALL_SAVE_FLAG);
			        //store  
			        cv.restore();
			         
				}else if(size == 3){
					double widthTogether = photoBitmap.get(0).getWidth();
					double upHeight = photoBitmap.get(0).getHeight();
					
					double downLeftHeight = photoBitmap.get(1).getHeight();
					double downLetWidth = photoBitmap.get(1).getWidth();
					
					double downRightWidth = photoBitmap.get(2).getWidth();
					double downRightHeight = photoBitmap.get(2).getHeight();
					
					double downRightScale = downLeftHeight/downRightHeight;
					
					double downOriginalWidth = downLetWidth + downRightWidth*downRightScale;
					double downScale = widthTogether/downOriginalWidth;
					
					double downActualHeight = downLeftHeight*downScale;
					
					double heightTogether = upHeight + downActualHeight;
					combineBitmap = Bitmap.createBitmap((int)widthTogether+45*2, (int)heightTogether+45*2+5,	Config.ARGB_8888);
					Canvas cv = new Canvas(combineBitmap); 
					Matrix matrix = new Matrix();
			        cv.drawColor(Color.argb(255, 193, 210, 240));
			        cv.drawBitmap(photoBitmap.get(0),45,45,null);
			        matrix.setScale((float)downScale, (float)downScale);
			        Bitmap downLeft = Bitmap.createBitmap(photoBitmap.get(1),0,0,photoBitmap.get(1).getWidth(),photoBitmap.get(1).getHeight(),matrix,true);
			        cv.drawBitmap(downLeft, 45,(float) (heightTogether+95-45-downLeft.getHeight()) ,null);
			        double downRightActualScale = downScale*downRightScale;
			        matrix.setScale((float)downRightActualScale, (float) downRightActualScale);
			        Bitmap downRight = Bitmap.createBitmap(photoBitmap.get(2),0,0,photoBitmap.get(2).getWidth(),photoBitmap.get(2).getHeight(),matrix,true);
			        cv.drawBitmap(downRight,(float) (widthTogether + 90-45-downRight.getWidth()),(float)(heightTogether+95-45-downRight.getHeight()),null);

			        //save all clip  
			        cv.save(Canvas.ALL_SAVE_FLAG);
			        //store  
			        cv.restore();
			        
					
				}else if(size == 4){
					double leftWidth = photoBitmap.get(0).getWidth();
					double leftDownScale = (double)photoBitmap.get(0).getWidth()/(double)photoBitmap.get(2).getWidth();
					double leftDownHeight = (double)photoBitmap.get(2).getHeight()*leftDownScale;
					double rightDownScale = (double)photoBitmap.get(1).getWidth()/(double)photoBitmap.get(3).getWidth();
					double rightUpHeight = photoBitmap.get(1).getHeight();
					double rightDownHeight = photoBitmap.get(3).getHeight()*rightDownScale;
					double heightTogether = leftDownHeight + photoBitmap.get(0).getHeight();
					double rightWholeScale = heightTogether/(double)(rightUpHeight+rightDownHeight);
					double widthTogether = (double)photoBitmap.get(1).getWidth()*rightWholeScale+photoBitmap.get(0).getWidth();
					double rightDownWholeScale = rightDownScale*rightWholeScale;
					combineBitmap = Bitmap.createBitmap((int)(widthTogether)+120, (int)heightTogether+120, Config.ARGB_8888); 
					Canvas cv = new Canvas(combineBitmap); 
					//45,30,45
					cv.drawColor(Color.argb(255, 255, 220, 245));
					cv.drawBitmap(photoBitmap.get(0), 45,45,null);
					Matrix matrix = new Matrix();
					matrix.setScale((float)leftDownScale, (float)leftDownScale);
					Bitmap leftDownBitmap = Bitmap.createBitmap(photoBitmap.get(2),0,0,photoBitmap.get(2).getWidth(),photoBitmap.get(2).getHeight(),matrix,true);
					cv.drawBitmap(leftDownBitmap, 45, (int)heightTogether+120-leftDownBitmap.getHeight()-45,null);
					//draw pic 1 (in 0,1,2,3)
					matrix.setScale((float)rightWholeScale,(float)rightWholeScale );
					Bitmap rightUpBitmap = Bitmap.createBitmap(photoBitmap.get(1),0,0,photoBitmap.get(1).getWidth(),photoBitmap.get(1).getHeight(),matrix,true);
					cv.drawBitmap(rightUpBitmap,(float)(leftWidth+30+45), 45,null);
					//Bitmap rightDownBitmap = Bitmap.createBitmap(photoBitmap.get(3),0,0,photoBitmap.get(3).getWidth(),photoBitmap.get(3).getHeight(),matrix,true);
					
					matrix.setScale((float)rightDownWholeScale,(float)rightDownWholeScale);
					Bitmap rightDownBitmap = Bitmap.createBitmap(photoBitmap.get(3),0,0,photoBitmap.get(3).getWidth(),photoBitmap.get(3).getHeight(),matrix,true);
					//matrix.setScale((float)rightWholeScale,(float)rightWholeScale );
					//rightDownBitmap = Bitmap.createBitmap(photoBitmap.get(3),0,0,photoBitmap.get(3).getWidth(),photoBitmap.get(3).getHeight(),matrix,true);
					cv.drawBitmap(rightDownBitmap, (float)(widthTogether+120-45-rightDownBitmap.getWidth()),(float)(heightTogether+120-45-rightDownBitmap.getHeight()) ,null);
					
					cv.save(Canvas.ALL_SAVE_FLAG);
			        //store  
			        cv.restore();
				}else if(size == 5){
					double leftWidth = photoBitmap.get(0).getWidth();
					double leftMidleScale = leftWidth/(double)photoBitmap.get(1).getWidth();
					double leftDownScale = leftWidth/(double)photoBitmap.get(2).getWidth();
					
					double heightTogether = (double)photoBitmap.get(0).getHeight()+(double)photoBitmap.get(1).getHeight()*leftMidleScale+(double)photoBitmap.get(2).getHeight()*leftDownScale;
					
					double rightOriginalWidth = photoBitmap.get(3).getWidth();
					double rightDownOriginalScale = rightOriginalWidth/(double)photoBitmap.get(4).getWidth();
					double rightOriginalHeight = (double)photoBitmap.get(3).getHeight()+(double)photoBitmap.get(4).getHeight()*rightDownOriginalScale;
					double rightScale = heightTogether/rightOriginalHeight;
					double rightWidth = rightOriginalWidth*rightScale;
					
					double widthTogether = leftWidth + rightWidth;
					//width:45,30,45  heigth 45,30,30,45
					combineBitmap = Bitmap.createBitmap((int)(widthTogether)+120, (int)heightTogether+150, Config.ARGB_8888); 
					Canvas cv = new Canvas(combineBitmap); 
					cv.drawColor(Color.argb(255, 255,228,181));
					cv.drawBitmap(photoBitmap.get(0), 45,45,null);
					Matrix matrix = new Matrix();
					//draw 1 in (0,1,2,3,4)
					matrix.setScale((float)leftMidleScale, (float)leftMidleScale);
					Bitmap tempBitmap = Bitmap.createBitmap(photoBitmap.get(1),0,0,photoBitmap.get(1).getWidth(),photoBitmap.get(1).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, 45, 45+30+photoBitmap.get(0).getHeight(),null);
					//draw 2 in (0,1,2,3,4)
					matrix.setScale((float)leftDownScale, (float)leftDownScale);
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(2),0,0,photoBitmap.get(2).getWidth(),photoBitmap.get(2).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, 45, (float) (heightTogether+150-45-tempBitmap.getHeight()),null);
					//draw 3 in (0,1,2,3,4)
					matrix.setScale((float)rightScale, (float)rightScale);
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(3),0,0,photoBitmap.get(3).getWidth(),photoBitmap.get(3).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, (float) (widthTogether+150-45-tempBitmap.getWidth()), 45,null);
					//draw 4 in (0,1,2,3,4)
					double rightDownWholeScale = rightScale*rightDownOriginalScale;
					matrix.setScale((float)rightDownWholeScale, (float)rightDownWholeScale);
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(4),0,0,photoBitmap.get(4).getWidth(),photoBitmap.get(4).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, (float) (widthTogether+150-45-tempBitmap.getWidth()), (float) (heightTogether+150-45-tempBitmap.getHeight()),null);
					cv.save(Canvas.ALL_SAVE_FLAG);
			        //store  
			        cv.restore();
				}else if(size == 6){
					double upHeight = photoBitmap.get(0).getHeight();
					double upRightScale = upHeight/(double)photoBitmap.get(1).getHeight();
					
					double widthTogether = upRightScale*(double)photoBitmap.get(1).getWidth()+photoBitmap.get(0).getWidth();
					
					double midleOriginalHeight = photoBitmap.get(2).getHeight();
					double midleRightOriginalScale = midleOriginalHeight/(double)photoBitmap.get(3).getHeight();
					double midleOrighalWidth = photoBitmap.get(2).getWidth() + midleRightOriginalScale*(double)photoBitmap.get(3).getWidth();
					double midleWholeScale = widthTogether/midleOrighalWidth;
					
					double downOriginalHeight = photoBitmap.get(4).getHeight();
					double downRightOriginalScale = downOriginalHeight/(double)photoBitmap.get(5).getHeight();
					double downOrighalWidth = photoBitmap.get(4).getWidth() + downRightOriginalScale*(double)photoBitmap.get(5).getWidth();
					double downWholeScale = widthTogether/downOrighalWidth;
					
					double heightTogether = upHeight + midleOriginalHeight*midleWholeScale + downOriginalHeight*downWholeScale;
					
					//width:45,30,45  heigth 45,30,30,45
					combineBitmap = Bitmap.createBitmap((int)(widthTogether)+120, (int)heightTogether+150, Config.ARGB_8888); 
					Canvas cv = new Canvas(combineBitmap); 
					cv.drawColor(Color.argb(244, 220,220,220));
					Bitmap tempBitmap = null;
					Matrix matrix = new Matrix();
					//draw 0 in (0,1,2,3,4,5)
					cv.drawBitmap(photoBitmap.get(0), 45, 45,null);
					matrix.setScale((float)upRightScale, (float)upRightScale);
					//draw 1 in (0,1,2,3,4,5)
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(1),0,0,photoBitmap.get(1).getWidth(),photoBitmap.get(1).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, (float)widthTogether+120-45-tempBitmap.getWidth(), 45,null);
					//draw 2 in (0,1,2,3,4,5)
					matrix.setScale((float)midleWholeScale, (float)midleWholeScale);
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(2),0,0,photoBitmap.get(2).getWidth(),photoBitmap.get(2).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, 45, (float) (45+30+upHeight),null);
					//draw 3 in (0,1,2,3,4,5)
					double midleRightScale = midleRightOriginalScale*midleWholeScale;
					matrix.setScale((float)midleRightScale, (float)midleRightScale);
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(3),0,0,photoBitmap.get(3).getWidth(),photoBitmap.get(3).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, (float)widthTogether+120-45-tempBitmap.getWidth(), (float) (45+30+upHeight),null);
					//draw 4 in (0,1,2,3,4,5)
					matrix.setScale((float)downWholeScale, (float)downWholeScale);
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(4),0,0,photoBitmap.get(4).getWidth(),photoBitmap.get(4).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, 45, (float) (heightTogether+150-45-tempBitmap.getHeight()),null);
					//draw 5 in (0,1,2,3,4,5)
					double downRightScale = downRightOriginalScale*downWholeScale;
					matrix.setScale((float)downRightScale, (float)downRightScale);
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(5),0,0,photoBitmap.get(5).getWidth(),photoBitmap.get(5).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, (float)widthTogether+120-45-tempBitmap.getWidth(),  (float) (heightTogether+150-45-tempBitmap.getHeight()),null);
					
					
					cv.save(Canvas.ALL_SAVE_FLAG);
			        //store  
			       cv.restore();
					
				}else if(size == 7){
					double upHeight = photoBitmap.get(0).getHeight();
					double upRightScale = upHeight/(double)photoBitmap.get(1).getHeight();
					
					double widthTogether = upRightScale*(double)photoBitmap.get(1).getWidth()+photoBitmap.get(0).getWidth();
					
					double midleOriginalHeight = photoBitmap.get(2).getHeight();
					double midleRightOriginalScale = midleOriginalHeight/(double)photoBitmap.get(3).getHeight();
					double midleOrighalWidth = photoBitmap.get(2).getWidth() + midleRightOriginalScale*(double)photoBitmap.get(3).getWidth();
					double midleWholeScale = widthTogether/midleOrighalWidth;
					
					double downOriginalHeight = photoBitmap.get(4).getHeight();
					double downMidleOriginalScale = downOriginalHeight/(double)photoBitmap.get(5).getHeight();
					double downRightOriginalScale = downOriginalHeight/(double)photoBitmap.get(6).getHeight();
					double downOrighalWidth = photoBitmap.get(4).getWidth() + downMidleOriginalScale*(double)photoBitmap.get(5).getWidth()+downRightOriginalScale*(double)photoBitmap.get(6).getWidth();
					double downWholeScale = widthTogether/downOrighalWidth;
					
					double heightTogether = upHeight + midleOriginalHeight*midleWholeScale + downOriginalHeight*downWholeScale;
					//width:45,30,45;45,15,15,45  heigth 45,30,30,45
					combineBitmap = Bitmap.createBitmap((int)(widthTogether)+120, (int)heightTogether+150, Config.ARGB_8888); 
					Canvas cv = new Canvas(combineBitmap); 
					cv.drawColor(Color.argb(244, 60,179,113));
					Bitmap tempBitmap = null;
					Matrix matrix = new Matrix();
					//draw 0 in (0,1,2,3,4,5)
					cv.drawBitmap(photoBitmap.get(0), 45, 45,null);
					matrix.setScale((float)upRightScale, (float)upRightScale);
					//draw 1 in (0,1,2,3,4,5)
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(1),0,0,photoBitmap.get(1).getWidth(),photoBitmap.get(1).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, (float)widthTogether+120-45-tempBitmap.getWidth(), 45,null);
					//draw 2 in (0,1,2,3,4,5)
					matrix.setScale((float)midleWholeScale, (float)midleWholeScale);
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(2),0,0,photoBitmap.get(2).getWidth(),photoBitmap.get(2).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, 45, (float) (45+30+upHeight),null);
					//draw 3 in (0,1,2,3,4,5)
					double midleRightScale = midleRightOriginalScale*midleWholeScale;
					matrix.setScale((float)midleRightScale, (float)midleRightScale);
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(3),0,0,photoBitmap.get(3).getWidth(),photoBitmap.get(3).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, (float)widthTogether+120-45-tempBitmap.getWidth(), (float) (45+30+upHeight),null);
					//draw 4 in (0,1,2,3,4,5)
					matrix.setScale((float)downWholeScale, (float)downWholeScale);
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(4),0,0,photoBitmap.get(4).getWidth(),photoBitmap.get(4).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, 45, (float) (heightTogether+150-45-tempBitmap.getHeight()),null);
					//draw 5 in (0,1,2,3,4,5)
					double downMidleScale = downMidleOriginalScale*downWholeScale;
					matrix.setScale((float)downMidleScale, (float)downMidleScale);
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(5),0,0,photoBitmap.get(5).getWidth(),photoBitmap.get(5).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, (float) ((float)45+(float)(photoBitmap.get(4).getWidth())*downWholeScale+15),  (float) (heightTogether+150-45-tempBitmap.getHeight()),null);
					//draw 6 in (0,1,2,3,4,5)
					double downRightScale = downRightOriginalScale*downWholeScale;
					matrix.setScale((float)downRightScale, (float)downRightScale);
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(6),0,0,photoBitmap.get(6).getWidth(),photoBitmap.get(6).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, (float) (widthTogether+120-tempBitmap.getWidth()-45),  (float) (heightTogether+150-45-tempBitmap.getHeight()),null);
					//
					cv.save(Canvas.ALL_SAVE_FLAG);
			        //store  
			        cv.restore();
					
				}else if(size == 8){
					//cv.save(Canvas.ALL_SAVE_FLAG);
			        //store  
			        //cv.restore();
					double upHeight = photoBitmap.get(0).getHeight();
					double upMidleScale = upHeight/(double)photoBitmap.get(1).getHeight();
					double upRightScale = upHeight/(double)photoBitmap.get(2).getHeight();
					
					double widthTogether = upRightScale*(double)photoBitmap.get(2).getWidth()+upMidleScale*(double)photoBitmap.get(1).getWidth()+photoBitmap.get(0).getWidth();
					
					double midleOriginalHeight = photoBitmap.get(3).getHeight();
					double midleMidleOriginalScale = midleOriginalHeight/(double)photoBitmap.get(4).getHeight();
					double midleRightOriginalScale = midleOriginalHeight/(double)photoBitmap.get(5).getHeight();
					double midleOrighalWidth = photoBitmap.get(3).getWidth() + midleMidleOriginalScale*(double)photoBitmap.get(4).getWidth()+midleRightOriginalScale*(double)photoBitmap.get(5).getWidth();
					double midleWholeScale = widthTogether/midleOrighalWidth;
					
					
					
					double downOriginalHeight = photoBitmap.get(6).getHeight();
					double downRightOriginalScale = downOriginalHeight/(double)photoBitmap.get(7).getHeight();
					double downOrighalWidth = photoBitmap.get(6).getWidth() + downRightOriginalScale*(double)photoBitmap.get(7).getWidth();
					double downWholeScale = widthTogether/downOrighalWidth;
					
					double heightTogether = upHeight + midleOriginalHeight*midleWholeScale +downOriginalHeight*downWholeScale;
					//width:45,40,45;45,20,20,45  height: 45,30,30,45
					combineBitmap = Bitmap.createBitmap((int)(widthTogether)+130, (int)heightTogether+150, Config.ARGB_8888); 
					Canvas cv = new Canvas(combineBitmap); 
					cv.drawColor(Color.argb(244, 255,192,203));
					Bitmap tempBitmap = null;
					Matrix matrix = new Matrix();
					//draw 0 in (0,1,2,3,4,5,6,7)
					cv.drawBitmap(photoBitmap.get(0), 45, 45,null);
					//draw 1 in (0,1,2,3,4,5)
					matrix.setScale((float)upMidleScale, (float)upMidleScale);
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(1),0,0,photoBitmap.get(1).getWidth(),photoBitmap.get(1).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, 45 + photoBitmap.get(0).getWidth()+20, 45,null);
					//draw 2 in (0,1,2,3,4,5)
					matrix.setScale((float)upRightScale, (float)upRightScale);
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(2),0,0,photoBitmap.get(2).getWidth(),photoBitmap.get(2).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, (float) (widthTogether+130-tempBitmap.getWidth())-45,45,null);
					//draw 3 in (0,1,2,3,4,5,6,7)
					matrix.setScale((float)midleWholeScale, (float) midleWholeScale);
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(3),0,0,photoBitmap.get(3).getWidth(),photoBitmap.get(3).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, 45, (float) (45+30+upHeight),null);
					matrix.setScale((float)upMidleScale, (float)upMidleScale);
					//draw 4 in (0,1,2,3,4,5)
					matrix.setScale((float)(midleWholeScale*midleMidleOriginalScale),(float)(midleWholeScale*midleMidleOriginalScale));
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(4),0,0,photoBitmap.get(4).getWidth(),photoBitmap.get(4).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, (float) (45 + (double)photoBitmap.get(3).getWidth()*midleWholeScale+20), (float) (45 + upHeight+30),null);
					//draw 5 in (0,1,2,3,4,5)
					matrix.setScale((float)(midleWholeScale*midleRightOriginalScale),(float)(midleWholeScale*midleRightOriginalScale));
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(5),0,0,photoBitmap.get(5).getWidth(),photoBitmap.get(5).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, (float) (widthTogether+130-tempBitmap.getWidth()-45),(float) (45+30+upHeight),null);
					//draw 6 in (0,1,2,3,4,5,6,7)
					matrix.setScale((float)downWholeScale, (float) downWholeScale);
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(6),0,0,photoBitmap.get(6).getWidth(),photoBitmap.get(6).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, 45, (float) (heightTogether+150-45-tempBitmap.getHeight()),null);
					//draw 7 in (0,1,2,3,4,5)
					matrix.setScale((float)(downWholeScale*downRightOriginalScale), (float)(downRightOriginalScale*downWholeScale));
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(7),0,0,photoBitmap.get(7).getWidth(),photoBitmap.get(7).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, (float) (widthTogether+130-tempBitmap.getWidth()-45),(float) (heightTogether+150-tempBitmap.getHeight()-45),null);
					cv.save(Canvas.ALL_SAVE_FLAG);
			        //store  
			        cv.restore();
					
				}else if(size == 9){
					double upHeight = photoBitmap.get(0).getHeight();
					double upMidleScale = upHeight/(double)photoBitmap.get(1).getHeight();
					double upRightScale = upHeight/(double)photoBitmap.get(2).getHeight();
					
					double widthTogether = upRightScale*(double)photoBitmap.get(2).getWidth()+upMidleScale*(double)photoBitmap.get(1).getWidth()+photoBitmap.get(0).getWidth();
					
					double midleOriginalHeight = photoBitmap.get(3).getHeight();
					double midleMidleOriginalScale = midleOriginalHeight/(double)photoBitmap.get(4).getHeight();
					double midleRightOriginalScale = midleOriginalHeight/(double)photoBitmap.get(5).getHeight();
					double midleOrighalWidth = photoBitmap.get(3).getWidth() + midleMidleOriginalScale*(double)photoBitmap.get(4).getWidth()+midleRightOriginalScale*(double)photoBitmap.get(5).getWidth();
					double midleWholeScale = widthTogether/midleOrighalWidth;
					
					
					
					double downOriginalHeight = photoBitmap.get(6).getHeight();
					double downMidleOriginalScale = downOriginalHeight/(double)photoBitmap.get(7).getHeight();
					double downRightOriginalScale = downOriginalHeight/(double)photoBitmap.get(8).getHeight();
					double downOrighalWidth = photoBitmap.get(6).getWidth() + downMidleOriginalScale*(double)photoBitmap.get(7).getWidth()+downRightOriginalScale*(double)photoBitmap.get(8).getWidth();
					double downWholeScale = widthTogether/downOrighalWidth;
					
					double heightTogether = upHeight + midleOriginalHeight*midleWholeScale +downOriginalHeight*downWholeScale;
					//width:45,20,20,45  height: 45,30,30,45
					combineBitmap = Bitmap.createBitmap((int)(widthTogether)+130, (int)heightTogether+150, Config.ARGB_8888); 
					Canvas cv = new Canvas(combineBitmap); 
					cv.drawColor(Color.argb(244, 0,191,255));
					Bitmap tempBitmap = null;
					Matrix matrix = new Matrix();
					//draw 0 in (0,1,2,3,4,5,6,7)
					cv.drawBitmap(photoBitmap.get(0), 45, 45,null);
					//draw 1 in (0,1,2,3,4,5)
					matrix.setScale((float)upMidleScale, (float)upMidleScale);
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(1),0,0,photoBitmap.get(1).getWidth(),photoBitmap.get(1).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, 45 + photoBitmap.get(0).getWidth()+20, 45,null);
					//draw 2 in (0,1,2,3,4,5)
					matrix.setScale((float)upRightScale, (float)upRightScale);
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(2),0,0,photoBitmap.get(2).getWidth(),photoBitmap.get(2).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, (float) (widthTogether+130-tempBitmap.getWidth())-45,45,null);
					//draw 3 in (0,1,2,3,4,5,6,7)
					matrix.setScale((float)midleWholeScale, (float) midleWholeScale);
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(3),0,0,photoBitmap.get(3).getWidth(),photoBitmap.get(3).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, 45, (float) (45+30+upHeight),null);
					matrix.setScale((float)upMidleScale, (float)upMidleScale);
					//draw 4 in (0,1,2,3,4,5)
					matrix.setScale((float)(midleWholeScale*midleMidleOriginalScale),(float)(midleWholeScale*midleMidleOriginalScale));
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(4),0,0,photoBitmap.get(4).getWidth(),photoBitmap.get(4).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, (float) (45 + (double)photoBitmap.get(3).getWidth()*midleWholeScale+20), (float) (45 + upHeight+30),null);
					//draw 5 in (0,1,2,3,4,5)
					matrix.setScale((float)(midleWholeScale*midleRightOriginalScale),(float)(midleWholeScale*midleRightOriginalScale));
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(5),0,0,photoBitmap.get(5).getWidth(),photoBitmap.get(5).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, (float) (widthTogether+130-tempBitmap.getWidth()-45),(float) (45+30+upHeight),null);
					//draw 6 in (0,1,2,3,4,5,6,7)
					matrix.setScale((float)downWholeScale, (float) downWholeScale);
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(6),0,0,photoBitmap.get(6).getWidth(),photoBitmap.get(6).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, 45, (float) (heightTogether+150-45-tempBitmap.getHeight()),null);
					//draw 7 in (0,1,2,3,4,5,6,7,8)
					matrix.setScale((float)(downWholeScale*downMidleOriginalScale),(float)(downWholeScale*downMidleOriginalScale));
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(7),0,0,photoBitmap.get(7).getWidth(),photoBitmap.get(7).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, (float) (45 + (double)photoBitmap.get(6).getWidth()*downWholeScale+20), (float) (heightTogether+150-tempBitmap.getHeight()-45),null);
					//draw 8 in (0,1,2,3,4,5,6,7,8,9)
					matrix.setScale((float)(downWholeScale*downRightOriginalScale), (float)(downRightOriginalScale*downWholeScale));
					tempBitmap = Bitmap.createBitmap(photoBitmap.get(8),0,0,photoBitmap.get(8).getWidth(),photoBitmap.get(8).getHeight(),matrix,true);
					cv.drawBitmap(tempBitmap, (float) (widthTogether+130-tempBitmap.getWidth()-45),(float) (heightTogether+150-tempBitmap.getHeight()-45),null);
					cv.save(Canvas.ALL_SAVE_FLAG);
			        //store  
			        cv.restore();
					
				}
				Log.d("items:",""+adapter.getCount()+"个");
				Bimp.drr.clear();
				
				outfitPath = handlePic.saveBitmap(combineBitmap);
				for(int i = 0;i<photoBitmap.size();i++)
					if(photoBitmap.get(i).isRecycled() == false)
						photoBitmap.get(i).recycle();
				guimiDB.addMyOutfit(outfitPath, null, false);
				startActivity(sendIntent);
				getIntent().removeExtra(CombinePicActivity.EXTRA_IMAGE_LIST);
				Bimp.drr.clear();
				Bimp.bmp.clear();
				FileUtils.deleteDir();
				finish();
				
			}
		});
	}
	private int getAllPhotoWidthTogether(ArrayList<Bitmap> photoBitmap){
		int result = 0;
		for(int i = 0;i<photoBitmap.size();i++){
			result += photoBitmap.get(i).getWidth();
		}
		return result;
	}
	private int getAllPhotoHeightTogether(ArrayList<Bitmap> photoBitmap){
		int result = 0;
		for(int i = 0;i<photoBitmap.size();i++){
			result += photoBitmap.get(i).getHeight();
		}
		return result;
	}
	private Bitmap compressPhoto(Bitmap originalBitmap) throws IOException{
		int maxSinglePicSize = 100;
		Log.i("bitmap size","size:"+getBitmapsize(originalBitmap));
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
        originalBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos); 
        byte[] a = baos.toByteArray(); 
        int singlePicSize = a.length/1024;
        int scale = 80;
        int be = 2;
        while(singlePicSize > maxSinglePicSize){
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray()); 
        BitmapFactory.Options newOpts = new BitmapFactory.Options(); 
        newOpts.inJustDecodeBounds = true; 
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts); 
        newOpts.inJustDecodeBounds = false; 
        int w = newOpts.outWidth; 
        int h = newOpts.outHeight;
        newOpts.inSampleSize = be;
        isBm.reset();
        isBm = new ByteArrayInputStream(baos.toByteArray()); 
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts); 
        isBm.reset();
        baos.reset();
        bitmap.compress(Bitmap.CompressFormat.JPEG, scale, baos);  
        singlePicSize = baos.toByteArray().length/1024;
        
        originalBitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()),null,null);
        Log.i("iteration","lalala");
        if(scale>30){
        	scale -=5;
        }
        baos.flush();
        baos.close();
        isBm.close();
		}
        return originalBitmap;
		
		
	}
	private Bitmap compressPhoto(String path) throws IOException{
		BitmapFactory.Options options = new BitmapFactory.Options(); 
		if(Bimp.drr.size()<3)
			options.inSampleSize = 2;
		else 
			options.inSampleSize=4;//图片高宽度都为本来的二分之一，即图片大小为本来的大小的四分之一     //设置16MB的姑且存储空间（不过感化还没看出来，待验证）    
		Bitmap bitMap = BitmapFactory.decodeFile(path,options);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options1 = 80;  
        while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
            baos.reset();//重置baos即清空baos  
            bitMap.compress(Bitmap.CompressFormat.JPEG, options1, baos);//这里压缩options%，把压缩后的数据存放到baos中  
            options1 -= 10;//每次都减少10  
            if(options1 == 10)
            	break;
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
        if(bitMap.isRecycled())
        	bitMap.recycle();
        return bitmap;  
	}
	private ArrayList<Bitmap>  compressPhotos(ArrayList<Bitmap> photoBitmap) throws IOException{
		double maxSize =500.00; 
		double singlePicSize;
		double maxSinglePicSize = maxSize/photoBitmap.size();
		if(photoBitmap.size()>4)
			Toast.makeText(PublishedActivity.this, "搭配图片正在生成中", 500).show();
		for(int i = 0;i<photoBitmap.size();i++){
			Log.i("bitmap size","size:"+getBitmapsize(photoBitmap.get(i)));
			ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
            photoBitmap.get(i).compress(Bitmap.CompressFormat.JPEG, 80, baos); 
            byte[] a = baos.toByteArray(); 
            singlePicSize = a.length/1024;
            int scale = 80;
            int be = 2;
            while(singlePicSize > maxSinglePicSize){
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray()); 
            BitmapFactory.Options newOpts = new BitmapFactory.Options(); 
            newOpts.inJustDecodeBounds = true; 
            Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts); 
            newOpts.inJustDecodeBounds = false; 
            int w = newOpts.outWidth; 
            int h = newOpts.outHeight;
            newOpts.inSampleSize = be;
            isBm = new ByteArrayInputStream(baos.toByteArray()); 
            bitmap = BitmapFactory.decodeStream(isBm, null, newOpts); 
            
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, scale, baos);  
            singlePicSize = baos.toByteArray().length/1024;
            
            photoBitmap.set(i, BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()),null,null));
            Log.i("iteration","lalala");
            if(scale>30){
            	scale -=5;
            }
            baos.flush();
            baos.close();
			}
			
		}
		return photoBitmap;
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		getIntent().removeExtra(CombinePicActivity.EXTRA_IMAGE_LIST);
		Bimp.drr.clear();
		super.onBackPressed();
	}
	
	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater; // ËßÜÂõæÂÆπÂô®
		private int selectedPosition = -1;// ÈÄâ‰∏≠ÁöÑ‰ΩçÁΩÆ
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			return (Bimp.bmp.size() + 1);
		}

		public Object getItem(int arg0) {

			return null;
		}

		public long getItemId(int arg0) {

			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			final int coord = position;
			ViewHolder holder = null;
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.item_published_grida_4m,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image_4m);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.bmp.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused_4m));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.bmp.get(position));
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.drr.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							try {
								String path = Bimp.drr.get(Bimp.max);
								System.out.println(path);
								
								Bitmap bm = Bimp.revitionImageSize(path);
								Bimp.bmp.add(bm);
								String newStr = path.substring(
										path.lastIndexOf("/") + 1,
										path.lastIndexOf("."));
								FileUtils.saveBitmap(bm, "" + newStr);
								Bimp.max += 1;
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
							} catch (IOException e) {

								e.printStackTrace();
							}
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent) {

			View view = View
					.inflate(mContext, R.layout.item_popupwindows_4m, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins_4m));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup_4m);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2_4m));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			/*Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);*/
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo_4m);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel_4m);
			/*bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					photo();
					dismiss();
				}
			});*/
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(PublishedActivity.this,
							CombinePicActivity.class);
					startActivity(intent);
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}

	private static final int TAKE_PICTURE = 0x000000;
	private String path = "";

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/myimage/", String.valueOf(System.currentTimeMillis())
				+ ".jpg");
		path = file.getPath();
		Uri imageUri = Uri.fromFile(file);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.drr.size() < 9 && resultCode == -1) {
				Bimp.drr.add(path);
			}
			break;
		}
	}
	public long getBitmapsize(Bitmap bitmap){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
        	return bitmap.getByteCount();
        }
// Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
	}

}
