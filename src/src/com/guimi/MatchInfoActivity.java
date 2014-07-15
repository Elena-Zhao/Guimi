package com.guimi;

import com.guimi.entities.Match;
import com.guimi.entities.Outfit;
import com.guimi.sqlite.GuiMiDB;
import com.guimi.sqlite.ImageHandeller;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MatchInfoActivity extends Activity {
	GuiMiDB db = null;
	Outfit outfit = null;
	Match matchItem;
	ImageHandeller imageHandeller;

	
	private TextView upNum;
	private TextView downNum;
	private ImageButton addImage;
	private ImageButton upImage;
	private ImageButton downImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match_info);
		Intent intent = getIntent();
		matchItem = (Match) intent.getSerializableExtra("item");
		db = GuiMiDB.getInstance(this);
		imageHandeller = imageHandeller.getInstance(this);

		if (matchItem != null) {
			GetOutfitInfoTask getOutfitInfo = new GetOutfitInfoTask();
			getOutfitInfo.execute();
		}
		getActionBar().setHomeButtonEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.match_info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class GetOutfitInfoTask extends AsyncTask<Void,Void,Bitmap>{
		
		@Override
		protected Bitmap doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			try{
			outfit = db.getOutfit(matchItem.getMatchId());
			}catch(NullPointerException e){
				return null;
			}
			Bitmap bmp = null;
			if(outfit!=null){
			
			try {
				
				//Result = imageHandeller.getBitmap(matchImageUrl);
				//if(Result == null){
				bmp = imageHandeller.download(outfit.getOutfitUrl());
				//}
			} catch (Exception e) {
				e.printStackTrace();
			}
			}
			return bmp;
		}
		@Override
		protected void onPostExecute(Bitmap bmp) {
			if(outfit == null)
				return;
			
			
			TextView tv;
			tv = (TextView) findViewById(R.id.describe);
			tv.setText(outfit.getOutfitDescription());
			((TextView) findViewById(R.id.uploadername)).setText(outfit
					.getUploaderName());
			tv = (TextView) findViewById(R.id.date);
			tv.setText(tv.getText()+outfit.getUploadTime());
			tv = (TextView) findViewById(R.id.item_tag);
			tv.setText(outfit.getClothDescription());
			if(bmp!=null)
				((ImageView) findViewById(R.id.image)).setImageBitmap(bmp);
			
			//ImageButton button;
			upImage = (ImageButton) findViewById(R.id.up_image);
			if(outfit.isLiked()){
				upImage.setImageResource(R.drawable.up_true);
			}else {
				upImage.setImageResource(R.drawable.up_false);
			}
			downImage = (ImageButton) findViewById(R.id.down_image);
			if(outfit.isDisliked()){
				downImage.setImageResource(R.drawable.down_true);
			}else {
				downImage.setImageResource(R.drawable.down_false);
			}
			addImage = (ImageButton) findViewById(R.id.add_image);
			if(outfit.isFavorited()){
				addImage.setImageResource(R.drawable.add_true);
			}else {
				addImage.setImageResource(R.drawable.add_false);
			}
			
			upNum = (TextView) findViewById(R.id.up_num);
			upNum.setText(String.valueOf(outfit.getLikes()));
			downNum = (TextView) findViewById(R.id.down_num);
			downNum.setText(String.valueOf(outfit.getDislikes()));
			
			upImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (outfit.isLiked() == false) {
						new HandleMatch()
								.execute(0);
					} else {
						new HandleMatch()
								.execute(1);
					}
				}
			});
			downImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					if (outfit.isDisliked() == false) {
						new HandleMatch()
								.execute(2);
					} else {
						new HandleMatch()
								.execute(3);
					}
				}
			});

			addImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					if (outfit.isFavorited() == false) {
						new HandleMatch()
								.execute(4);
					} else {
						new HandleMatch()
								.execute(5);
					}
				}
			});
		}
	}
	
	//HandleMatch.execute(int type)
		//param type:
		//0 like
		//1 cancel like
		//2 dislike
		//3 cancel dislike
		//4 collect
		//5 cancel collect
		class HandleMatch extends AsyncTask <Integer,Void,Integer>{
			private int type;
			
			public HandleMatch(){
			}
			@Override
			protected Integer doInBackground(Integer... params) {
				type = params[0];
				int result = Integer.MIN_VALUE;
				int id = outfit.getOutfitID();
				String outFitId = outfit.getUploaderId();
				try {
					switch (type) {
					case 0:
						result = db.like(id,outFitId);
						break;
					case 1:
						result = db.cancelLike(id);
						break;
					case 2:
						result = db.dislike(id,outFitId);
						break;
					case 3:
						result = db.cancelDislike(id);
						break;
					case 4:
						result = db.addToFavorite(id,outFitId);
						break;
					case 5:
						result = db.cancelFavorite(id);
						break;
					default:
						result = -1;
						break;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return result;
			}

			@Override
			protected void onPostExecute(Integer result) {
				//成功
				if (result == 0) {
					try {
						switch (type) {
						case 0:
							outfit.setLikes(outfit.getLikes()+1);
							outfit.setLiked(true);
							upNum.setText(""+(Integer.parseInt(upNum.getText().toString())+1));
							upImage.setImageResource(R.drawable.up_true);
							break;
						case 1:
							outfit.setLikes(outfit.getLikes()-1);
							outfit.setLiked(false);
							upNum.setText(""+(Integer.parseInt(upNum.getText().toString())-1));
							upImage.setImageResource(R.drawable.up_false);
							break;
						case 2:
							outfit.setDislikes(outfit.getDislikes()+1);
							outfit.setDisliked(true);
							downNum.setText(""+(Integer.parseInt(downNum.getText().toString())+1));
							downImage.setImageResource(R.drawable.down_true);
							break;
						case 3:
							outfit.setDislikes(outfit.getDislikes()-1);
							outfit.setDisliked(false);
							downNum.setText(""+(Integer.parseInt(downNum.getText().toString())-1));
							downImage.setImageResource(R.drawable.down_false);
							break;
						case 4:
							outfit.setFavorited(true);
							result = 0;
							addImage.setImageResource(R.drawable.add_true);
							break;
						case 5:
							outfit.setFavorited(false);
							result = 0;
							addImage.setImageResource(R.drawable.add_false);
							break;
						default:
							result = -1;
							break;
						}
					} catch (NullPointerException e) {
						// TODO: handle exception
					}
				} else if(result == 2){
					Toast.makeText(MatchInfoActivity.this, "不能对自己的发布进行该操作！",Toast.LENGTH_SHORT).show();
				}
				
				//失败
				else {
					Toast.makeText(MatchInfoActivity.this, "网络连接超时，操作失败！",Toast.LENGTH_SHORT).show();
				}
			}
		}
}
