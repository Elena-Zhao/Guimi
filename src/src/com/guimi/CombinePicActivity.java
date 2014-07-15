package com.guimi;

import java.io.Serializable;
import java.util.List;

import com.guimi.uploadPhoto.combineUtil.AlbumHelper;
import com.guimi.uploadPhoto.combineUtil.Bimp;
import com.guimi.uploadPhoto.combineUtil.ImageBucket;
import com.guimi.uploadPhoto.combineUtil.ImageBucketAdapter;
import com.guimi.uploadPhoto.combineUtil.ImageGridActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class CombinePicActivity extends Activity {
	// ArrayList<Entity> dataList;//用来装载数据源的列表
	List<ImageBucket> dataList;
	GridView gridView;
	TextView cancleCombine;
	ImageBucketAdapter adapter;// 自定义的适配器
	AlbumHelper helper;
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static Bitmap bimap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_bucket_4m);

		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		initData();
		initView();
		Intent intent = new Intent(CombinePicActivity.this,
				ImageGridActivity.class);
		intent.putExtra(CombinePicActivity.EXTRA_IMAGE_LIST,
				(Serializable) dataList.get(0).imageList);
		startActivity(intent);
		finish();
		
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		dataList = helper.getImagesBucketList(false);	
		bimap=BitmapFactory.decodeResource(
				getResources(),
				R.drawable.icon_addpic_unfocused_4m);
	}

	/**
	 * 初始化view视图
	 */
	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview_4m);
		adapter = new ImageBucketAdapter(CombinePicActivity.this, dataList);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/**
				 * 根据position参数，可以获得跟GridView的子View相绑定的实体类，然后根据它的isSelected状态，
				 * 来判断是否显示选中效果。 至于选中效果的规则，下面适配器的代码中会有说明
				 */
				// if(dataList.get(position).isSelected()){
				// dataList.get(position).setSelected(false);
				// }else{
				// dataList.get(position).setSelected(true);
				// }
				/**
				 * 通知适配器，绑定的数据发生了改变，应当刷新视图
				 */
				// adapter.notifyDataSetChanged();
				Intent intent = new Intent(CombinePicActivity.this,
						ImageGridActivity.class);
				intent.putExtra(CombinePicActivity.EXTRA_IMAGE_LIST,
						(Serializable) dataList.get(position).imageList);
				startActivity(intent);
				finish();
			}

		});
		cancleCombine = (TextView)findViewById(R.id.cancleCombinePic_4m);
		cancleCombine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//预留接口跳转
				Intent intent = new Intent(CombinePicActivity.this,MainActivity.class);
				startActivity(intent);
				getIntent().removeExtra(CombinePicActivity.EXTRA_IMAGE_LIST);
				Bimp.drr.clear();
				finish();
			}
		});
	}
}
//