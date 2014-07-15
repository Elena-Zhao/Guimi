package com.guimi;

import com.guimi.MatchInfoActivity.GetOutfitInfoTask;
import com.guimi.MatchInfoActivity.HandleMatch;
import com.guimi.entities.FavoriteOutfit;
import com.guimi.sqlite.GuiMiDB;
import com.guimi.sqlite.ImageHandeller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowMyFavorateOutfitActivity extends Activity {
	
	private GuiMiDB db;
	private Button cancleFavourite;
	private ImageView imageView;
	private FavoriteOutfit outfit;
	ImageHandeller imageHandeller;
	private int result = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_my_faourite_outfit);
		
		db = GuiMiDB.getInstance(this);
		
		imageView = (ImageView) findViewById(R.id.image);
		
		outfit = (FavoriteOutfit) getIntent().getExtras().get("myFavouriteOutfit");
		getActionBar().setHomeButtonEnabled(true);
		
		
		if (outfit == null){
			
			return;
		}
		//result = getIntent().getExtras().getInt("pos",-1);
		imageHandeller = ImageHandeller.getInstance(this);
		GetOutfitInfoTask getOutfitInfo = new GetOutfitInfoTask();
		getOutfitInfo.execute();

		
		cancleFavourite = (Button) findViewById(R.id.cancel_btn);
		cancleFavourite.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(ShowMyFavorateOutfitActivity.this)
				.setMessage("删除单品")
				.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {
								Toast.makeText(getApplicationContext(), "取消收藏", Toast.LENGTH_SHORT).show();
								//
								db.cancelFavorite(outfit.getOutfitID());
								startActivity(new Intent(ShowMyFavorateOutfitActivity.this,MyCollectActivity.class));
								//setResult(result);
								finish();
							}
						}).setNegativeButton("取消", null).show();
				
				
			}
		});
		getActionBar().setHomeButtonEnabled(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case android.R.id.home:
			//setResult(-1);
			startActivity(new Intent(ShowMyFavorateOutfitActivity.this,MyCollectActivity.class));
			
			this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		startActivity(new Intent(ShowMyFavorateOutfitActivity.this,MyCollectActivity.class));
		//setResult(result);
		finish();
	}
class GetOutfitInfoTask extends AsyncTask<Void,Void,Bitmap>{
		
		@Override
		protected Bitmap doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			Bitmap bmp = null;
			if(outfit!=null){
			
			try {
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
			
			
		}
	}
}
