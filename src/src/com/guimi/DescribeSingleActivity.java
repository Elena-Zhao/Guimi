package com.guimi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.guimi.entities.Item;
import com.guimi.entities.Tag;
import com.guimi.entities.TagIndex;
import com.guimi.myviews.TagGroup;
import com.guimi.sqlite.GuiMiDB;
import com.guimi.uploadPhoto.HandlePic;

import android.R.integer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class DescribeSingleActivity extends Activity {
	private GridLayout selectTypePanel;
	private LinearLayout selectTagPanel;
	private ScrollView selectTagPanelContainer;
	private GridLayout selectedTagPanel;
	private Button reselectBtn;
	private Button b_shoe, b_shirt, b_bag, b_accessiory, b_trousers, b_dress;
	private TextView title;
	private HandlePic handlePic;
	private String urlString;
	private int clothesType;
	private int tags[][];
	private List<TagIndex> tagList;
	private int tagsNumber;
	
	private GuiMiDB dbOperation; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_describe_single);
		final float scale = getApplicationContext().getResources()
				.getDisplayMetrics().density;

		dbOperation = GuiMiDB.getInstance(this);
		// 设置照片预览
		
		Intent intent = getIntent();
		ImageView previewImage = (ImageView) findViewById(R.id.preview_img);
		urlString = (String) intent.getStringExtra("url");
		//Log.e("url!",urlString);
		
		handlePic = new HandlePic("GuiMe", this);
		handlePic.setmCurrentPhotoPath(urlString);
		handlePic.handleBigCameraPhoto(previewImage);

		selectTypePanel = (GridLayout) findViewById(R.id.select_type_panel);
		selectTagPanel = (LinearLayout) findViewById(R.id.select_tag_panel);
		selectTagPanelContainer = (ScrollView) findViewById(R.id.scroll_container);
		selectedTagPanel = (GridLayout) findViewById(R.id.selected_tag_panel);

		title = (TextView) findViewById(R.id.title);

		// type buttons
		b_shoe = (Button) findViewById(R.id.type_shoe);
		b_shirt = (Button) findViewById(R.id.type_shirt);
		b_bag = (Button) findViewById(R.id.type_bag);
		b_accessiory = (Button) findViewById(R.id.type_accessory);
		b_trousers = (Button) findViewById(R.id.type_trousers);
		b_dress = (Button) findViewById(R.id.type_dress);

		// reselect button
		reselectBtn = (Button) findViewById(R.id.reselect_btn);
		// reselect button click event and animation
		reselectBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clothesType = -1;

				selectedTagPanel.removeAllViews();
				selectTagPanel.removeAllViews();
				selectTagPanelContainer.setVisibility(View.GONE);
				title.setText("请选择单品种类：");
				selectTypePanel.setVisibility(View.VISIBLE);
				b_shoe.startAnimation(animTranslate(0, 0, 180 * scale + 0.5f,
						-90 * scale + 0.5f, b_shoe, 300, true, 3.0f, -1.0f));
				b_dress.startAnimation(animTranslate(0, 0, 0, 0, b_dress, 220,
						true, 1.0f, 0.0f));
				b_bag.startAnimation(animTranslate(0, 0, 0, -90 * scale + 0.5f,
						b_bag, 240, true, 1.0f, -1.0f));
				b_accessiory.startAnimation(animTranslate(0, 0,
						180 * scale + 0.5f, 0, b_accessiory, 280, true, 3.0f,
						0.0f));
				b_shirt.startAnimation(animTranslate(0, 0, 90 * scale + 0.5f,
						0, b_shirt, 240, true, 2.0f, 0.0f));
				b_trousers.startAnimation(animTranslate(0, 0,
						90 * scale + 0.5f, -90 * scale + 0.5f, b_trousers, 280,
						true, 2.0f, -1.0f));

			}

		});

		// type button click event and animation
		OnClickListener typeBtnListener = new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				reselectBtn.setVisibility(View.VISIBLE);
				b_dress.startAnimation(animTranslate(0, 0, 0, 0, b_dress, 340,
						false, 1.0f, 0.0f));
				b_bag.startAnimation(animTranslate(0, -90 * scale + 0.5f, 0, 0,
						b_bag, 380, false, 1.0f, -1.0f));
				b_shirt.startAnimation(animTranslate(90 * scale + 0.5f, 0, 0,
						0, b_shirt, 380, false, 2.0f, 0.0f));
				b_accessiory.startAnimation(animTranslate(180 * scale + 0.5f,
						0, 0, 0, b_accessiory, 460, false, 3.0f, 0.0f));
				b_trousers.startAnimation(animTranslate(90 * scale + 0.5f, -90
						* scale + 0.5f, 0, 0, b_trousers, 460, false, 2.0f,
						-1.0f));
				b_shoe.startAnimation(animTranslate(180 * scale + 0.5f, -90
						* scale + 0.5f, 0, 0, b_shoe, 500, false, 3.0f, -1.0f));

				switch (view.getId()) {
				case R.id.type_shoe:
					clothesType = 3;
					title.setText("鞋子");
					setupTagSelectPanel(R.id.type_shoe);
					break;
				case R.id.type_shirt:
					clothesType = 0;
					title.setText("上衣");
					setupTagSelectPanel(R.id.type_shirt);
					break;
				case R.id.type_bag:
					clothesType = 4;
					title.setText("包包");
					setupTagSelectPanel(R.id.type_bag);
					break;
				case R.id.type_accessory:
					clothesType = 5;
					title.setText("饰品");
					setupTagSelectPanel(R.id.type_accessory);
					break;
				case R.id.type_trousers:
					clothesType = 1;
					title.setText("裤子");
					setupTagSelectPanel(R.id.type_trousers);
					break;
				case R.id.type_dress:
					clothesType = 2;
					title.setText("裙装");
					setupTagSelectPanel(R.id.type_dress);
					break;
				}

			}

		};

		b_shoe.setOnClickListener(typeBtnListener);
		b_shirt.setOnClickListener(typeBtnListener);
		b_bag.setOnClickListener(typeBtnListener);
		b_accessiory.setOnClickListener(typeBtnListener);
		b_trousers.setOnClickListener(typeBtnListener);
		b_dress.setOnClickListener(typeBtnListener);
		
		tagList = new ArrayList<TagIndex>();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.describe_single, menu);
		return true;
	}

	// 移动的动画效果
	/*
	 * TranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta,
	 * float toYDelta)
	 * 
	 * float fromXDelta:这个参数表示动画开始的点离当前View X坐标上的差值；
	 * 
	 * 　　 * float toXDelta, 这个参数表示动画结束的点离当前View X坐标上的差值；
	 * 
	 * 　　 * float fromYDelta, 这个参数表示动画开始的点离当前View Y坐标上的差值；
	 * 
	 * 　　 * float toYDelta)这个参数表示动画开始的点离当前View Y坐标上的差值；
	 */
	protected Animation animTranslate(float toX, float toY, final float lastX,
			final float lastY, final Button b_shoe2, long durationMillis,
			final boolean isopen, final float pivotX, final float pivotY) {
		// TODO Auto-generated method stub
		TranslateAnimation animationTranslate = new TranslateAnimation(lastX,
				toX, lastY, toY);
		ScaleAnimation animationScale;
		if (isopen) {
			animationScale = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
					Animation.RELATIVE_TO_SELF, pivotX,
					Animation.RELATIVE_TO_SELF, pivotY);
		} else {
			animationScale = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
					Animation.RELATIVE_TO_SELF, pivotX,
					Animation.RELATIVE_TO_SELF, pivotY);
		}
		AnimationSet animationSet = new AnimationSet(true);
		animationSet.setDuration(durationMillis);
		animationSet.addAnimation(animationTranslate);
		animationSet.addAnimation(animationScale);

		animationTranslate.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				b_shoe2.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				// LayoutParams params = new LayoutParams(0, 0);
				// params.height = 50;
				// params.width = 50;
				// params.setMargins(lastX, lastY, 0, 0);
				// selectTypePanel2.setLayoutParams(params);
				if (b_shoe2.getId() == R.id.type_shoe) {
					if (!isopen) {
						selectTypePanel.setVisibility(View.GONE);
						//setupTagSelectPanel(R.id.type_shoe);
						selectTagPanelContainer.scrollTo(0, 0);
						selectTagPanelContainer.setVisibility(View.VISIBLE);
					} else {
						reselectBtn.setVisibility(View.GONE);
					}
				}
				if (!isopen) {
					b_shoe2.setVisibility(View.GONE);
				}
				b_shoe2.clearAnimation();

			}
		});

		return animationSet;
	}

	private void setupTagSelectPanel(int type) {
		List<String[]> tags = new ArrayList<String[]>();
		for(int i = 0;i<dbOperation.getTagTitles(clothesType).length;i++)
			tags.add(dbOperation.getTagContent(dbOperation.getTagTitles(clothesType)[i]));

		TagGroup[] tagBar = new TagGroup[dbOperation.getTagTitles(clothesType).length];
		OnClickListener selectTagListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Button b = (Button) v;
				LinearLayout ll = (LinearLayout) v.getParent().getParent();
				int position = (Integer)b.getTag();
				addSelectedTag(b.getText().toString(), 
						((Integer) ll.getTag()).intValue(), position);
				Log.i("db",((Integer) ll.getTag()).intValue()+";"+position);
				ll.setVisibility(View.GONE);
			}
		};
		
		switch (type) {
		case R.id.type_shoe:
			tagBar[0] = new TagGroup(DescribeSingleActivity.this, "类型",
					tags.get(0), selectTagListener);
			tagBar[0].setTag(0);
			selectTagPanel.addView(tagBar[0]);

			tagBar[1] = new TagGroup(DescribeSingleActivity.this, "风格",
					tags.get(1), selectTagListener);
			tagBar[1].setTag(1);
			selectTagPanel.addView(tagBar[1]);

			tagBar[2] = new TagGroup(DescribeSingleActivity.this, "颜色",
					tags.get(2), selectTagListener);
			tagBar[2].setTag(2);
			selectTagPanel.addView(tagBar[2]);

			tagBar[3] = new TagGroup(DescribeSingleActivity.this, "热款",
					tags.get(3), selectTagListener);
			tagBar[3].setTag(3);
			selectTagPanel.addView(tagBar[3]);

			tagBar[4] = new TagGroup(DescribeSingleActivity.this, "跟高",
					tags.get(4), selectTagListener);
			tagBar[4].setTag(4);
			selectTagPanel.addView(tagBar[4]);
			break;
		case R.id.type_shirt:
			tagBar[0] = new TagGroup(DescribeSingleActivity.this, "类型",
					tags.get(0), selectTagListener);
			tagBar[0].setTag(0);
			selectTagPanel.addView(tagBar[0]);

			tagBar[1] = new TagGroup(DescribeSingleActivity.this, "风格",
					tags.get(1), selectTagListener);
			tagBar[1].setTag(1);
			selectTagPanel.addView(tagBar[1]);

			tagBar[2] = new TagGroup(DescribeSingleActivity.this, "颜色",
					tags.get(2), selectTagListener);
			tagBar[2].setTag(2);
			selectTagPanel.addView(tagBar[2]);

			tagBar[3] = new TagGroup(DescribeSingleActivity.this, "长度",
					tags.get(3), selectTagListener);
			tagBar[3].setTag(3);
			selectTagPanel.addView(tagBar[3]);

			tagBar[4] = new TagGroup(DescribeSingleActivity.this, "袖长",
					tags.get(4), selectTagListener);
			tagBar[4].setTag(4);
			selectTagPanel.addView(tagBar[4]);
			break;
		case R.id.type_bag:
			tagBar[0] = new TagGroup(DescribeSingleActivity.this, "类型",
					tags.get(0), selectTagListener);
			tagBar[0].setTag(0);
			selectTagPanel.addView(tagBar[0]);

			tagBar[1] = new TagGroup(DescribeSingleActivity.this, "风格",
					tags.get(1), selectTagListener);
			tagBar[1].setTag(1);
			selectTagPanel.addView(tagBar[1]);

			tagBar[2] = new TagGroup(DescribeSingleActivity.this, "热款",
					tags.get(2), selectTagListener);
			tagBar[2].setTag(2);
			selectTagPanel.addView(tagBar[2]);

			tagBar[3] = new TagGroup(DescribeSingleActivity.this, "款式",
					tags.get(3), selectTagListener);
			tagBar[3].setTag(3);
			selectTagPanel.addView(tagBar[3]);

			tagBar[4] = new TagGroup(DescribeSingleActivity.this, "大小",
					tags.get(4), selectTagListener);
			tagBar[4].setTag(4);
			selectTagPanel.addView(tagBar[4]);

			tagBar[5] = new TagGroup(DescribeSingleActivity.this, "材质",
					tags.get(5), selectTagListener);
			tagBar[5].setTag(5);
			selectTagPanel.addView(tagBar[5]);
			break;
		case R.id.type_accessory:
			tagBar[0] = new TagGroup(DescribeSingleActivity.this, "类型",
					tags.get(0), selectTagListener);
			tagBar[0].setTag(0);
			selectTagPanel.addView(tagBar[0]);

			tagBar[1] = new TagGroup(DescribeSingleActivity.this, "风格",
					tags.get(1), selectTagListener);
			tagBar[1].setTag(1);
			selectTagPanel.addView(tagBar[1]);

			tagBar[2] = new TagGroup(DescribeSingleActivity.this, "颜色",
					tags.get(2), selectTagListener);
			tagBar[2].setTag(2);
			selectTagPanel.addView(tagBar[2]);
			break;
		case R.id.type_trousers:
			tagBar[0] = new TagGroup(DescribeSingleActivity.this, "类型",
					tags.get(0), selectTagListener);
			tagBar[0].setTag(0);
			selectTagPanel.addView(tagBar[0]);

			tagBar[1] = new TagGroup(DescribeSingleActivity.this, "风格",
					tags.get(1), selectTagListener);
			tagBar[1].setTag(1);
			selectTagPanel.addView(tagBar[1]);

			tagBar[2] = new TagGroup(DescribeSingleActivity.this, "颜色",
					tags.get(2), selectTagListener);
			tagBar[2].setTag(2);
			selectTagPanel.addView(tagBar[2]);

			tagBar[3] = new TagGroup(DescribeSingleActivity.this, "长度",
					tags.get(3), selectTagListener);
			tagBar[3].setTag(3);
			selectTagPanel.addView(tagBar[3]);
			break;
		case R.id.type_dress:
			tagBar[0] = new TagGroup(DescribeSingleActivity.this, "类型",
					tags.get(0), selectTagListener);
			tagBar[0].setTag(0);
			selectTagPanel.addView(tagBar[0]);

			tagBar[1] = new TagGroup(DescribeSingleActivity.this, "风格",
					tags.get(1), selectTagListener);
			tagBar[1].setTag(1);
			selectTagPanel.addView(tagBar[1]);

			tagBar[2] = new TagGroup(DescribeSingleActivity.this, "颜色",
					tags.get(2), selectTagListener);
			tagBar[2].setTag(2);
			selectTagPanel.addView(tagBar[2]);

			tagBar[3] = new TagGroup(DescribeSingleActivity.this, "长度",
					tags.get(3), selectTagListener);
			tagBar[3].setTag(3);
			selectTagPanel.addView(tagBar[3]);

			tagBar[4] = new TagGroup(DescribeSingleActivity.this, "裙摆",
					tags.get(4), selectTagListener);
			tagBar[4].setTag(4);
			selectTagPanel.addView(tagBar[4]);

			tagBar[5] = new TagGroup(DescribeSingleActivity.this, "材质",
					tags.get(5), selectTagListener);
			tagBar[5].setTag(5);
			selectTagPanel.addView(tagBar[5]);
			break;
		
//			selectselectTagPanel.addView(tagBar[5]);
//			break;
		}
		LinearLayout nullView = new LinearLayout(DescribeSingleActivity.this);
		LayoutParams lp = new LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(20, 140, 20, 100);
		nullView.setLayoutParams(lp);
		nullView.setGravity(Gravity.CENTER_HORIZONTAL);

		Button confirmBtn = new Button(DescribeSingleActivity.this);
		confirmBtn.setText("完成");
		confirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// try{
//				tags = new int[tagList.size()][2];
//				for (int i = 0; i < tagList.size(); i++) {
//					tags[i][0] = tagList.get(i).getTitle();
//					tags[i][1] = tagList.get(i).getContent();
//				}
//				for (int j = 0; j < tags.length; j++) {
//					System.out.println("a:"+tags[j][0]+" b:"+tags[j][1]);
//				}
//			
//				dbOperation.addCloth(clothesType, tags, urlString, "");
//				
//				Toast toast = Toast.makeText(DescribeSingleActivity.this,
//						"单品添加成功！", Toast.LENGTH_LONG);
//				toast.show();
//				Log.e("get items from database", dbOperation.getItems()
//						.toString());
//				Intent intent = new Intent(DescribeSingleActivity.this, MainActivity.class);
//				intent.putExtra("fragment", 1);
//				startActivity(intent);
				descriptionCompletedAction();
			}

		});

		
		confirmBtn.setTextColor(Color.parseColor("#ffffff"));
		confirmBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
		lp = new LayoutParams(500, LinearLayout.LayoutParams.WRAP_CONTENT);
		confirmBtn.setLayoutParams(lp);

		nullView.addView(confirmBtn);
		selectTagPanel.addView(nullView);
	}

	private void addSelectedTag(String name, final int index, final int position) {
		Button tag = new Button(DescribeSingleActivity.this);
		tag.setText(name);
		tag.setTag(index);
		tag.setBackgroundResource(R.drawable.tag_delete_btn);
		tag.setTextSize(15);
		tag.setTextColor(Color.parseColor("#702044"));
		tag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Button tag = (Button) v;

				int singleIndex = getTagListIndex(index, position);
				Log.e("remove tag", "index:" + index + "//position:" + position
						+ "//singleIndex:" + singleIndex);
				
				tagList.remove(singleIndex);
				selectedTagPanel.removeView(tag);
				selectTagPanel.getChildAt(index).setVisibility(View.VISIBLE);
			}

		});

		selectedTagPanel.addView(tag);
		TagIndex newTag = new TagIndex(index, position);
		tagList.add(newTag);
		tagsNumber++;
	}
	
	//找到选中tag在tagList中的位置
	private int getTagListIndex(int tagTitleNumber, int tagContentNumber){
		for(int i = 0;i < tagList.size();i++){
			if(tagList.get(i).getTitle() == tagTitleNumber && tagList.get(i).getContent() == tagContentNumber)
				return i;
		}
		return -1;
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		descriptionCompletedAction();
		return super.onMenuItemSelected(featureId, item);
	}
	
	void descriptionCompletedAction(){
		tags = new int[tagList.size()][2];
		for (int i = 0; i < tagList.size(); i++) {
			tags[i][0] = tagList.get(i).getTitle();
			tags[i][1] = tagList.get(i).getContent();
		}
		for (int j = 0; j < tags.length; j++) {
			System.out.println("a:"+tags[j][0]+" b:"+tags[j][1]);
		}
	
		dbOperation.addCloth(clothesType, tags, urlString, "");
		
		Toast toast = Toast.makeText(DescribeSingleActivity.this,
				"单品添加成功！", Toast.LENGTH_SHORT);
		toast.show();
		
		this.finish();
	}
	
}
