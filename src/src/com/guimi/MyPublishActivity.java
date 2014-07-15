package com.guimi;

import java.util.List;
import java.util.Vector;

import com.guimi.entities.PublishedOutfit;
import com.guimi.myadapters.MyPublishListItemAdapter;
import com.guimi.myviews.UploadDialog;
import com.guimi.sqlite.GuiMiDB;
import com.guimi.uploadPhoto.HandlePic;
import com.guimi.util.DensityUtil;
import com.guimi.util.LocalAsyncImageLoader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class MyPublishActivity extends Activity {
	public final String TAG = "com.guimi.MyPublishActivity";
	private Handler UIhandler;
	private ListView list1;
	private ListView list2;
	private MyPublishListItemAdapter adapter1;
	private MyPublishListItemAdapter adapter2;
	private LinearLayout loadbar;
	private boolean showActionBar = true;
	private boolean someThreadRuning = false;
	private float listWidth = 0; 
	private List<PublishedOutfit> publishedOutfits;
	private GuiMiDB dbOperator;
	private LocalAsyncImageLoader localAsyncImageLoader;
	
	
	@SuppressLint("HandlerLeak")
	@Override 
	public void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_publish);
		
		dbOperator = GuiMiDB.getInstance(this);
		
		findViewById(R.id.dlist_container).setOnTouchListener(new OnTouchListener() { 
		     @Override 
	        public boolean onTouch(View view, MotionEvent m) {
//		    	 Log.i(TAG,"LinearLayout:onTouch action="+m.getAction()+"  isscroll:"+adapter1.isScroll());
//		    	 if(MotionEvent.ACTION_DOWN == m.getAction()){
//		     		adapter1.setScroll(true);
//		     		adapter2.setScroll(true);
		     	if(MotionEvent.ACTION_UP == m.getAction()){
		     		adapter1.setScroll(false);
		     		adapter2.setScroll(false);
		     	}
		    	 list1.dispatchTouchEvent(m);
		    	 list2.dispatchTouchEvent(m);
		    	 return true;
		     } 
	     });
		
		
		
		list1 = (ListView)findViewById(R.id.list1);
		list2 = (ListView)findViewById(R.id.list2);
		
		//Add headview to listview
		LinearLayout header = new LinearLayout(this);
		AbsListView.LayoutParams lp  = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT,80);
		header.setLayoutParams(lp);
		list1.addHeaderView(header);
		list2.addHeaderView(header);
		
		
		//Hide the actionbar when scrolling
		list1.setOnScrollListener(new OnScrollListener() {
			private int lastfirstVisibleItem = 0;
		    @Override
		    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		    	
		    }

		    @Override
		    public void onScrollStateChanged(AbsListView view, int scrollState) {
		    	if(OnScrollListener.SCROLL_STATE_IDLE == scrollState){
		    		showActionBar = true;
					if (!someThreadRuning) {
						someThreadRuning = true;
						Handler handler = new Handler();
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								if (showActionBar)
									MyPublishActivity.this.getActionBar()
											.show();
								someThreadRuning = false;
							}
						}, 1000);
					}
		    	}else if(OnScrollListener.SCROLL_STATE_TOUCH_SCROLL == scrollState&&showActionBar){
		    		MyPublishActivity.this.getActionBar().hide();
		    		showActionBar = false;
		    	}
		    }
		});
		
		loadbar = (LinearLayout)findViewById(R.id.load_status);
		loadbar.setVisibility(View.VISIBLE);
		adapter1 = new MyPublishListItemAdapter(this, 0);
		adapter2 = new MyPublishListItemAdapter(this, 1);
		
		list1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				Log.i("published","list1:on item click "+ pos);
				//Toast.makeText(MyMatchesActivity.this, "item click",1000).show();
				Intent intent = new Intent(MyPublishActivity.this,ShowMyPublishOutfitActivity.class);
				PublishedOutfit thisOutfit = adapter1.matches.get(pos-1);
				//Intent intent = new Intent(activity,showMyOutfitActitivity.class);
				//MyOutfit thisOutfit = matches.get(pos);
				intent.putExtra("myPublishedOutfit", thisOutfit);
				startActivity(intent);
				finish();
			}
		});
		list2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				Log.i("published","list2:on item click "+ pos);
				//Toast.makeText(MyMatchesActivity.this, "item click",1000).show();
				Intent intent = new Intent(MyPublishActivity.this,ShowMyPublishOutfitActivity.class);
				PublishedOutfit thisOutfit = adapter2.matches.get(pos-1);
				//Intent intent = new Intent(activity,showMyOutfitActitivity.class);
				//MyOutfit thisOutfit = matches.get(pos);
				intent.putExtra("myPublishedOutfit", thisOutfit);
				startActivity(intent);
				finish();
			}
		});
		
		list1.setAdapter(adapter1);
		list2.setAdapter(adapter2);
		
		listWidth = adapter1.listWidth;
		localAsyncImageLoader = new LocalAsyncImageLoader();
		adapter1.setLocalAsyncImageLoader(localAsyncImageLoader);
		adapter2.setLocalAsyncImageLoader(localAsyncImageLoader);
		adapter1.setListView(list1);
		adapter2.setListView(list2);
		
		// because the following constructions spend too much time,
		// so the UI keep nothing for several seconds
		// to avoid this, I use another thread to run it here:
		UIhandler = new Handler() {
			public void handleMessage(Message msg) {
				// ////////////////////////////////////////////
				Log.i("aaa", "start handle message ");
				super.handleMessage(msg);
				PageData data = (PageData) msg.obj;
				adapter1.addItems(data.getFirst());
				adapter2.addItems(data.getSecond());

				float height = data.getPlaceHolderViewHeight();
				if(height>0){
					adapter2.setPlaceHolderView(height);
				}else if(data.getPlaceHolderViewHeight()<0){
					adapter1.setPlaceHolderView(-height);
				}
				// ////////////////////////////////////////////
				loadbar.setVisibility(View.GONE);
			}
		};
		new GetPublishedOutfitAsyncTask().execute();
		//getActionBar().setTitle("共有12件发布");
		getActionBar().setHomeButtonEnabled(true);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		adapter1.setScroll(false);
		adapter2.setScroll(false);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_publish, menu);
		return true;
	}

	// dispatch items to list
	private void dispatchItems(List<PublishedOutfit> page) {
		PageData data = new PageData();
		float increasement = 0;
		float height1 = adapter1.getHeight();
		float height2 = adapter2.getHeight();
		PublishedOutfit item;
		for (int i = 0; i < page.size(); i++) {
			item = page.get(i);
			increasement = item.getPictureHeight() * listWidth
					/ item.getPictureWidth() + DensityUtil.dip2px(this, 20);
			Log.i(TAG, "heigth1:" + height1 + "     height2:" + height2
					+ "increasement:" + increasement);
			if (height1 > height2) {
				data.putInSecond(item);
				height2 += increasement;
			} else {
				data.putInFirst(item);
				height1 += increasement;
			}

		}

		data.setPlaceHolderViewHeight(height1 - height2);
		Message msg = UIhandler.obtainMessage();
		msg.obj = data;
		UIhandler.sendMessage(msg);

	}

	private class PageData {
		private Vector<PublishedOutfit> left;
		private Vector<PublishedOutfit> right;
		private float placeHolderViewHeight = 0;

		private void setPlaceHolderViewHeight(float height) {
			placeHolderViewHeight = height;
		}

		private float getPlaceHolderViewHeight() {
			return placeHolderViewHeight;
		}

		private PageData() {
			left = new Vector<PublishedOutfit>();
			right = new Vector<PublishedOutfit>();
		}

		public void putInFirst(PublishedOutfit item) {
			left.add(item);
		}

		public void putInSecond(PublishedOutfit item) {
			right.add(item);
		}

		public Vector<PublishedOutfit> getFirst() {
			return left;
		}

		public Vector<PublishedOutfit> getSecond() {
			return right;
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.action_add_collocation: 
			Dialog dialog = new UploadDialog(this,R.layout.upload_matches_dialog,
					new View.OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							switch (v.getId()) {
							case R.id.upload_single_from_local:
								break;
							case R.id.upload_single_from_camera:
								break;
							case R.id.upload_single_from_chest:
								Intent intent = new Intent(
										MyPublishActivity.this,
										AddTagForSingleActivity.class);
								startActivity(intent);
								break;
							}
						}
				
			});
			dialog.show();
			break;
		case android.R.id.home:
			this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class GetPublishedOutfitAsyncTask extends AsyncTask<Integer,Integer,List<PublishedOutfit>>{
		
		
		@Override
		protected List<PublishedOutfit> doInBackground(Integer... arg0) {
			// TODO Auto-generated method stub
			return dbOperator.getMyPublish();
		}
		
		@Override
		protected void onPostExecute(List<PublishedOutfit> result) {
			// TODO Auto-generated method stub
			if(result == null){
				getActionBar().setTitle("共有0件发布");
				Toast.makeText(getApplicationContext(), "你还没有发布搭配哦~", Toast.LENGTH_SHORT).show();
			}else{
				Log.i("db",result.toString());
				Toast.makeText(getApplicationContext(), "同步我的发布成功~", Toast.LENGTH_SHORT).show();
				getActionBar().setTitle("共有"+result.size()+"件发布");
				dispatchItems(result);
			}
		}
		
	}
		

	
}
