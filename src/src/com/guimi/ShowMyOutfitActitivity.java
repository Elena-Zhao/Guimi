package com.guimi;

import com.guimi.entities.MyOutfit;
import com.guimi.sqlite.GuiMiDB;
import com.guimi.uploadPhoto.HandlePic;
import com.guimi.uploadPhoto.combineUtil.ImageBucket;
import com.guimi.util.LocalAsyncImageLoader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowMyOutfitActitivity extends Activity {
	private ImageView imageView;
	private Button pulish_of_dispublishButton;
	private TextView isPublished;
	private String strPathString;
	private boolean isPublishedBoolean;
	private int outfitID;
	private GuiMiDB dbOperator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_show_my_all_outfit_info);

		dbOperator = GuiMiDB.getInstance(this);
		
		strPathString = getIntent().getExtras().getString("this_myOutfitUrl");
		isPublishedBoolean = getIntent().getExtras().getBoolean("isPublished");
		outfitID = getIntent().getExtras().getInt("this_myOutfitId");
		imageView = (ImageView) findViewById(R.id.myoutfitPic);
		pulish_of_dispublishButton = (Button) findViewById(R.id.publish_or_dipublishButton);
		isPublished = (TextView) findViewById(R.id.myoutfitIsPublished);

		Log.i("db", "url:" + strPathString);
		Log.i("db", "outfitID:" + outfitID);
		initData();
	}

	private void initData() {

		BitmapFactory.Options opts = new BitmapFactory.Options();

		opts.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(strPathString, opts);

		opts.inSampleSize = LocalAsyncImageLoader.computeSampleSize(opts, -1,
				512 * 512);

		opts.inJustDecodeBounds = false;
		Bitmap bmp = null;
		try {
			bmp = BitmapFactory.decodeFile(strPathString, opts);
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
		}
		imageView.setImageBitmap(bmp);

		String isPublishedTextString;
		if (isPublishedBoolean) {
			isPublishedTextString = "已发布";
			pulish_of_dispublishButton.setText("删除该发布");
		} else {
			isPublishedTextString = "未发布";
			pulish_of_dispublishButton.setText("发布该搭配");
		}
		isPublished.setText(isPublishedTextString);

		pulish_of_dispublishButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (isPublishedBoolean) {
					new AlertDialog.Builder(ShowMyOutfitActitivity.this)
							.setMessage("删除发布")
							.setPositiveButton("确认",
									new DialogInterface.OnClickListener() {

										public void onClick(
												DialogInterface dialog,
												int whichButton) {

											// 连接数据库
											new DeletePublish().execute();
											// test
											// startActivity(new
											// Intent(ShowMyOutfitActitivity.this,MainActivity.class));
											finish();
										}
									}).setNegativeButton("取消", null).show();

					// Toast.makeText(ShowItemInfoActivity.this, "删除成功！",
					// Toast.LENGTH_LONG).show();

				} else {
					// 连接数据库
					Intent intent = new Intent(ShowMyOutfitActitivity.this,
							AddTagForSingleActivity.class);
					intent.putExtra("url", strPathString);
					intent.putExtra("outfitId", outfitID);
					startActivity(intent);
					finish();
				}

			}
		});
	}
	class DeletePublish extends AsyncTask {
		private int Result;

		@Override
		protected Object doInBackground(Object... params) {
			try {
				Result = dbOperator.deletePublishInMyArea(outfitID);
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
