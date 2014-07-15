package com.guimi;

import java.util.ArrayList;
import java.util.List;

import com.guimi.entities.MatchTag;
import com.guimi.myadapters.AddTypeButtonAdapter;
import com.guimi.myviews.HorizontalListView;
import com.guimi.myviews.TagGroup;
import com.guimi.sqlite.ConnectServer;
import com.guimi.sqlite.GuiMiDB;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ScrollView;

public class AddTagForSingleActivity extends Activity {
	private HorizontalListView typeBtns;
	private LinearLayout tagPanel;
	private LinearLayout itemPanel;
	private Button editTagFinishBtn;
	private TextView itemName;
	private TagGroup selectedTagGroup;
	private ScrollView itemsContainer;
	private LinearLayout tagContainer;
	private GuiMiDB dbOperation;
	private List<MatchTag> matchTags;
	private String picUrlString;
	private int outfitId;
	private LinearLayout describeView;
	private LinearLayout loadBar;
	private EditText descriveText;
	private Button finishButton;
	private boolean isfinished = false;
	private boolean isloading = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_tag_for_single);

		dbOperation = GuiMiDB.getInstance(this);
		matchTags = new ArrayList<MatchTag>();
		Intent intent = getIntent();
		picUrlString = (String) intent.getStringExtra("url");
		outfitId = (int) intent.getIntExtra("outfitId", -1);
		// Log.e("url!",urlString);

		loadBar = (LinearLayout)findViewById(R.id.load_status);
		describeView = (LinearLayout)findViewById(R.id.describe_view);
		descriveText = (EditText)findViewById(R.id.describe_text);
		finishButton = (Button)findViewById(R.id.finish_button);
		finishButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				finishButton.requestFocus();
				finishButton.setEnabled(false);
				isloading = true;
				loadBar.setVisibility(View.VISIBLE);
				new UpLoadOutfits().execute(descriveText.getText().toString());
			}
		});
		
		itemsContainer = (ScrollView) findViewById(R.id.items_container);
		tagContainer = (LinearLayout) findViewById(R.id.tag_container);
		itemName = (TextView) findViewById(R.id.item_name);
		tagPanel = (LinearLayout) findViewById(R.id.tag_panel);
		itemPanel = (LinearLayout) findViewById(R.id.items_panel);
		editTagFinishBtn = (Button) findViewById(R.id.edit_tag_finish_btn);
		editTagFinishBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				hideTagPanel();
			}

		});
		typeBtns = (HorizontalListView) findViewById(R.id.add_type_buttons);
		typeBtns.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String typeName="";
				switch (arg2) {
				case 0:
					typeName = "鞋子";
					break;
				case 1:
					typeName = "饰品";
					break;
				case 2:
					typeName = "裤子";
					break;
				case 3:
					typeName = "包包";
					break;
				case 4:
					typeName = "上衣";
					break;
				case 5:
					typeName = "裙子";
					break;
				}
				showTagPanel(arg2);

				TagGroup tg = new TagGroup(AddTagForSingleActivity.this,
					typeName, new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								TagGroup group = (TagGroup) v.getParent()
										.getParent();
								group.removeTag(v);

							}

						}, true, new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								TagGroup tagGroup = (TagGroup) v.getTag();
								itemPanel.removeView(tagGroup);
								hideTagPanel();
							}

						});
				itemPanel.addView(tg);
				selectedTagGroup = tg;
				tg.setBackgroundResource(R.drawable.match_item_bg);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						itemsContainer.fullScroll(ScrollView.FOCUS_DOWN);
					}
				}, 500);
			}
		});

		AddTypeButtonAdapter adapter = new AddTypeButtonAdapter(
				new OnClickListener() {

					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						// Toast.makeText(getApplicationContext(),
						// "button click", Toast.LENGTH_SHORT).show();
						Log.e("tag selected!!!", "succeed!!!");
						ImageButton b = (ImageButton) view;
						// showTagPanel((String) b.getTag());

						TagGroup tg = new TagGroup(
								AddTagForSingleActivity.this,
								(String) b.getTag(), new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										TagGroup group = (TagGroup) v
												.getParent().getParent();
										group.removeTag(v);

									}

								}, true, new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										TagGroup tagGroup = (TagGroup) v
												.getTag();
										itemPanel.removeView(tagGroup);
										hideTagPanel();
									}

								});
						itemPanel.addView(tg);
						selectedTagGroup = tg;
						tg.setBackgroundResource(R.drawable.match_item_bg);
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								itemsContainer
										.fullScroll(ScrollView.FOCUS_DOWN);
							}
						}, 500);
					}
				}, this);
		typeBtns.setAdapter(adapter);
	}

	private void setupTagPanel(int index) {
		tagPanel.removeAllViews();

		switch (index) {
		case 0:
			// clothesType = 3;
			itemName.setText("鞋子");
			matchTags.add(new MatchTag("鞋子"));
			setupTagSelectPanel(R.id.type_shoe, 3,
					matchTags.get(matchTags.size() - 1));
			break;
		case 1:
			// clothesType = 5;
			itemName.setText("饰品");
			matchTags.add(new MatchTag("饰品"));
			setupTagSelectPanel(R.id.type_accessory, 5,
					matchTags.get(matchTags.size() - 1));
			break;
		case 2:
			// clothesType = 1;
			itemName.setText("裤子");
			matchTags.add(new MatchTag("裤子"));
			setupTagSelectPanel(R.id.type_trousers, 1,
					matchTags.get(matchTags.size() - 1));
			break;
		case 3:
			// clothesType = 4;
			itemName.setText("包包");
			matchTags.add(new MatchTag("包包"));
			setupTagSelectPanel(R.id.type_bag, 4,
					matchTags.get(matchTags.size() - 1));
			break;
		case 4:
			// clothesType = 0;
			itemName.setText("上衣");
			matchTags.add(new MatchTag("上衣"));
			setupTagSelectPanel(R.id.type_shirt, 0,
					matchTags.get(matchTags.size() - 1));
			break;
		case 5:
			// clothesType = 2;
			itemName.setText("裙装");
			matchTags.add(new MatchTag("裙装"));
			setupTagSelectPanel(R.id.type_dress, 2,
					matchTags.get(matchTags.size() - 1));
			break;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_tag_for_single, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (tagContainer.getVisibility() == View.VISIBLE) {
				hideTagPanel();
				return true;
			}

		}
		return super.onKeyDown(keyCode, event);
	}

	public void hideTagPanel() {
		tagContainer.setVisibility(View.GONE);
		RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) itemsContainer
				.getLayoutParams();
		rp.height = RelativeLayout.LayoutParams.MATCH_PARENT;
		itemsContainer.setLayoutParams(rp);
	}

	public void showTagPanel(int index) {
		RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) itemsContainer
				.getLayoutParams();
		rp.height = 450;
		itemsContainer.setLayoutParams(rp);
		setupTagPanel(index);
		tagContainer.setVisibility(View.VISIBLE);
	}

	private void setupTagSelectPanel(int type, int clothType, final MatchTag matchTag) {
		List<String[]> tags = new ArrayList<String[]>();
		for(int i = 0;i<dbOperation.getTagTitles(clothType).length;i++)
			tags.add(dbOperation.getTagContent(dbOperation.getTagTitles(clothType)[i]));

		TagGroup[] tagBar = new TagGroup[dbOperation.getTagTitles(clothType).length];
		// OnClickListener selectTagListener = new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Button b = (Button) v;
		// LinearLayout ll = (LinearLayout) v.getParent().getParent();
		// int position = (Integer)b.getTag();
		// addSelectedTag(b.getText().toString(),
		// ((Integer) ll.getTag()).intValue(), position);
		//
		// ll.setVisibility(View.GONE);
		// }
		// };
		OnClickListener selectTagListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Button b = (Button) v;
				LinearLayout ll = (LinearLayout) v.getParent().getParent();
				selectedTagGroup.addTag(b.getText().toString(),
						((Integer) ll.getTag()).intValue());

				matchTag.getTags().add(b.getText().toString());

				ll.setVisibility(View.GONE);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						itemsContainer.fullScroll(ScrollView.FOCUS_DOWN);
					}
				}, 500);
			}
		};

		switch (type) {
		case R.id.type_shoe:
			tagBar[0] = new TagGroup(AddTagForSingleActivity.this, "类型",
					tags.get(0), selectTagListener);
			tagBar[0].setTag(0);
			tagPanel.addView(tagBar[0]);

			tagBar[1] = new TagGroup(AddTagForSingleActivity.this, "风格",
					tags.get(1), selectTagListener);
			tagBar[1].setTag(1);
			tagPanel.addView(tagBar[1]);

			tagBar[2] = new TagGroup(AddTagForSingleActivity.this, "颜色",
					tags.get(2), selectTagListener);
			tagBar[2].setTag(2);
			tagPanel.addView(tagBar[2]);

			tagBar[3] = new TagGroup(AddTagForSingleActivity.this, "热款",
					tags.get(3), selectTagListener);
			tagBar[3].setTag(3);
			tagPanel.addView(tagBar[3]);

			tagBar[4] = new TagGroup(AddTagForSingleActivity.this, "跟高",
					tags.get(4), selectTagListener);
			tagBar[4].setTag(4);
			tagPanel.addView(tagBar[4]);
			break;
		case R.id.type_shirt:
			tagBar[0] = new TagGroup(AddTagForSingleActivity.this, "类型",
					tags.get(0), selectTagListener);
			tagBar[0].setTag(0);
			tagPanel.addView(tagBar[0]);

			tagBar[1] = new TagGroup(AddTagForSingleActivity.this, "风格",
					tags.get(1), selectTagListener);
			tagBar[1].setTag(1);
			tagPanel.addView(tagBar[1]);

			tagBar[2] = new TagGroup(AddTagForSingleActivity.this, "颜色",
					tags.get(2), selectTagListener);
			tagBar[2].setTag(2);
			tagPanel.addView(tagBar[2]);

			tagBar[3] = new TagGroup(AddTagForSingleActivity.this, "长度",
					tags.get(3), selectTagListener);
			tagBar[3].setTag(3);
			tagPanel.addView(tagBar[3]);

			tagBar[4] = new TagGroup(AddTagForSingleActivity.this, "袖长",
					tags.get(4), selectTagListener);
			tagBar[4].setTag(4);
			tagPanel.addView(tagBar[4]);
			break;
		case R.id.type_bag:
			tagBar[0] = new TagGroup(AddTagForSingleActivity.this, "类型",
					tags.get(0), selectTagListener);
			tagBar[0].setTag(0);
			tagPanel.addView(tagBar[0]);

			tagBar[1] = new TagGroup(AddTagForSingleActivity.this, "风格",
					tags.get(1), selectTagListener);
			tagBar[1].setTag(1);
			tagPanel.addView(tagBar[1]);

			tagBar[2] = new TagGroup(AddTagForSingleActivity.this, "热款",
					tags.get(2), selectTagListener);
			tagBar[2].setTag(2);
			tagPanel.addView(tagBar[2]);

			tagBar[3] = new TagGroup(AddTagForSingleActivity.this, "款式",
					tags.get(3), selectTagListener);
			tagBar[3].setTag(3);
			tagPanel.addView(tagBar[3]);

			tagBar[4] = new TagGroup(AddTagForSingleActivity.this, "大小",
					tags.get(4), selectTagListener);
			tagBar[4].setTag(4);
			tagPanel.addView(tagBar[4]);

			tagBar[5] = new TagGroup(AddTagForSingleActivity.this, "材质",
					tags.get(5), selectTagListener);
			tagBar[5].setTag(5);
			tagPanel.addView(tagBar[5]);
			break;
		case R.id.type_accessory:
			tagBar[0] = new TagGroup(AddTagForSingleActivity.this, "类型",
					tags.get(0), selectTagListener);
			tagBar[0].setTag(0);
			tagPanel.addView(tagBar[0]);

			tagBar[1] = new TagGroup(AddTagForSingleActivity.this, "风格",
					tags.get(1), selectTagListener);
			tagBar[1].setTag(1);
			tagPanel.addView(tagBar[1]);

			tagBar[2] = new TagGroup(AddTagForSingleActivity.this, "颜色",
					tags.get(2), selectTagListener);
			tagBar[2].setTag(2);
			tagPanel.addView(tagBar[2]);
			break;
		case R.id.type_trousers:
			tagBar[0] = new TagGroup(AddTagForSingleActivity.this, "类型",
					tags.get(0), selectTagListener);
			tagBar[0].setTag(0);
			tagPanel.addView(tagBar[0]);

			tagBar[1] = new TagGroup(AddTagForSingleActivity.this, "风格",
					tags.get(1), selectTagListener);
			tagBar[1].setTag(1);
			tagPanel.addView(tagBar[1]);

			tagBar[2] = new TagGroup(AddTagForSingleActivity.this, "颜色",
					tags.get(2), selectTagListener);
			tagBar[2].setTag(2);
			tagPanel.addView(tagBar[2]);

			tagBar[3] = new TagGroup(AddTagForSingleActivity.this, "长度",
					tags.get(3), selectTagListener);
			tagBar[3].setTag(3);
			tagPanel.addView(tagBar[3]);
			break;
		case R.id.type_dress:
			tagBar[0] = new TagGroup(AddTagForSingleActivity.this, "类型",
					tags.get(0), selectTagListener);
			tagBar[0].setTag(0);
			tagPanel.addView(tagBar[0]);

			tagBar[1] = new TagGroup(AddTagForSingleActivity.this, "风格",
					tags.get(1), selectTagListener);
			tagBar[1].setTag(1);
			tagPanel.addView(tagBar[1]);

			tagBar[2] = new TagGroup(AddTagForSingleActivity.this, "颜色",
					tags.get(2), selectTagListener);
			tagBar[2].setTag(2);
			tagPanel.addView(tagBar[2]);

			tagBar[3] = new TagGroup(AddTagForSingleActivity.this, "长度",
					tags.get(3), selectTagListener);
			tagBar[3].setTag(3);
			tagPanel.addView(tagBar[3]);

			tagBar[4] = new TagGroup(AddTagForSingleActivity.this, "裙摆",
					tags.get(4), selectTagListener);
			tagBar[4].setTag(4);
			tagPanel.addView(tagBar[4]);

			tagBar[5] = new TagGroup(AddTagForSingleActivity.this, "材质",
					tags.get(5), selectTagListener);
			tagBar[5].setTag(5);
			tagPanel.addView(tagBar[5]);
			break;
		}
		LinearLayout nullView = new LinearLayout(AddTagForSingleActivity.this);
		LayoutParams lp = new LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(20, 140, 20, 100);
		nullView.setLayoutParams(lp);
		nullView.setGravity(Gravity.CENTER_HORIZONTAL);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		super.onMenuItemSelected(featureId, item);
		if (item.getItemId() == R.id.action_finish&&!isloading) {
			if(!isfinished){
				Log.e("match tags", matchTags.toString());
				isfinished = true;
				getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
				describeView.setVisibility(View.VISIBLE);
			}else{
				isfinished = false;
				getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
				//new UpLoadOutfits().execute();
				describeView.setVisibility(View.GONE);
			}
		}
		return true;
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if(isfinished){
			menu.findItem(R.id.action_finish).setTitle("上一步");
		}else{
			menu.findItem(R.id.action_finish).setTitle("下一步");
		}
		super.onPrepareOptionsMenu(menu);
		return true;
	}
	class UpLoadOutfits extends AsyncTask<String,Void,Object> {
		private int Result;

		@Override
		protected Object doInBackground(String... params) {
			try {
				Result = dbOperation.publishOutfit(outfitId,params[0],
					matchTags.toString(), picUrlString);
			} catch (Exception e) {
				Log.i("db", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			if (Result == 0) {
				Toast.makeText(getApplication(), "成功", Toast.LENGTH_LONG)
						.show();
				
			} else {
				Toast.makeText(getApplication(), "请求失败，请检查网络连接",
						Toast.LENGTH_LONG).show();
				
			}
			loadBar.setVisibility(View.GONE);
//			Intent intent = new Intent(AddTagForSingleActivity.this,
//					MainActivity.class);
//			startActivity(intent);
			finish();
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(isloading){
			return;
		}else if(isfinished){
			describeView.setVisibility(View.GONE);
			isfinished = false;
			getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
		}else
			super.onBackPressed();
	}
}
