package com.guimi;

import java.io.File;
import java.net.URI;

import com.guimi.entities.Item;
import com.guimi.myviews.TagGroup;
import com.guimi.sqlite.GuiMiDB;
import com.guimi.util.LocalAsyncImageLoader;

import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShowItemInfoActivity extends Activity {
	GuiMiDB dbOperator;
	String singleId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_item_info);

		dbOperator = GuiMiDB.getInstance(this);

		Intent intent = getIntent();
		Item singleItem = (Item) intent.getSerializableExtra("item");
		singleId = singleItem.getClothId();

		ImageView itemImage = (ImageView) findViewById(R.id.itemPic);
		
		
		BitmapFactory.Options opts = new BitmapFactory.Options();

		opts.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(singleItem.getItemUrl(), opts);

		   

		opts.inSampleSize = LocalAsyncImageLoader.computeSampleSize(opts, -1, 512*512);  

		opts.inJustDecodeBounds = false;
		Bitmap bmp = null;
		try {

		 bmp = BitmapFactory.decodeFile(singleItem.getItemUrl(), opts);
		}
		catch (OutOfMemoryError err) {
			err.printStackTrace();
	    }
		if(bmp!=null){
			itemImage.setImageBitmap(bmp);
		}

		TextView itemType = (TextView) findViewById(R.id.itemType);
		itemType.setText(singleItem.getItemType());
		
		if(singleItem.getTags() != null){
			LinearLayout itemTagContainer = (LinearLayout)findViewById(R.id.item_tag_container);
			
			TagGroup tags = new TagGroup(this,"",singleItem.getTagsString() ,null);
			itemTagContainer.addView(tags);
		}
		

		Button deleteButton = (Button) findViewById(R.id.delete_single);
		deleteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(ShowItemInfoActivity.this)
						.setMessage("删除单品")
						.setPositiveButton("确认",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int whichButton) {
										Log.e("ID", singleId);
										dbOperator.deleteCloth(singleId);
										
										Intent intent = new Intent(
												ShowItemInfoActivity.this,
												MainActivity.class);
										ShowItemInfoActivity.this.finish();
										startActivity(intent);
									}
								}).setNegativeButton("取消", null).show();
			}
		});
		
		getActionBar().setHomeButtonEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId() == android.R.id.home){
			Intent intent = new Intent(
					this,
					MainActivity.class);
			finish();
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

}
