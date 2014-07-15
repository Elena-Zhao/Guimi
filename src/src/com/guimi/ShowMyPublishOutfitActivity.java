package com.guimi;

import com.guimi.entities.PublishedOutfit;
import com.guimi.sqlite.GuiMiDB;
import com.guimi.util.LocalAsyncImageLoader;

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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowMyPublishOutfitActivity extends Activity {
	
	private GuiMiDB dbOperatorDb;
	private PublishedOutfit outfit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_my_publish_outfit_info);
		
		dbOperatorDb = GuiMiDB.getInstance(this);
		
		ImageView imageView = (ImageView) findViewById(R.id.thisPublishedPic);
		
		outfit = (PublishedOutfit) getIntent().getExtras().get("myPublishedOutfit");
		
		if(outfit == null)
			return;
		
		BitmapFactory.Options opts = new BitmapFactory.Options();

		opts.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(outfit.getOutfitUrl(), opts);

		opts.inSampleSize = LocalAsyncImageLoader.computeSampleSize(opts, -1,
				512 * 512);

		opts.inJustDecodeBounds = false;
		Bitmap bmp = null;
		try {

			bmp = BitmapFactory.decodeFile(outfit.getOutfitUrl(), opts);
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
		}
		imageView.setImageBitmap(bmp);
		
		Button publishBtn = (Button) findViewById(R.id.cancleThisPublishedButton);
		publishBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(ShowMyPublishOutfitActivity.this)
				.setMessage("删除发布")
				.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {

							public void onClick(
									DialogInterface dialog,
									int whichButton) {

								//取消发布
								new DeletePublish().execute();
								startActivity(new Intent(ShowMyPublishOutfitActivity.this,MyPublishActivity.class));
								finish();
							}
						}).setNegativeButton("取消", null).show();
				
			}
		});
		
		TextView text = (TextView)findViewById(R.id.date);
		text.setText("发布于     "+outfit.getUploadTime());
		
		text = (TextView)findViewById(R.id.item_tag);
		text.setText("发布于"+outfit.getClothDescription());
		
		text = (TextView)findViewById(R.id.describe);
		text.setText(outfit.getOutfitDescription());
		
		getActionBar().setHomeButtonEnabled(true);
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		startActivity(new Intent(ShowMyPublishOutfitActivity.this,MyPublishActivity.class));
		finish();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId() == android.R.id.home){
			startActivity(new Intent(ShowMyPublishOutfitActivity.this,MyPublishActivity.class));
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	class DeletePublish extends AsyncTask {
		private int Result;

		@Override
		protected Object doInBackground(Object... params) {
			try {
				Result = dbOperatorDb.deletePublish(outfit.getOutfitID());
			} catch (Exception e) {
				Log.i("db", e.toString());
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			if (Result == 0) {
			Toast.makeText(getApplicationContext(), "取消发布", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
