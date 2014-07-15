package com.guimi;

import java.util.List;
import java.util.Vector;

import com.guimi.entities.FavoriteOutfit;
import com.guimi.entities.MyOutfit;
import com.guimi.myadapters.MyCollectListItemAdapter;
import com.guimi.myadapters.MyCollectListItemAdapter.OnCancelFavourateListener;
import com.guimi.myviews.UploadDialog;
import com.guimi.sqlite.GuiMiDB;
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
import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;

public class MyCollectActivity extends Activity {
	public final String TAG = "com.guimi.MyCollectActivity";
	private Handler UIhandler;
	private ListView list1;
	private ListView list2;
	private MyCollectListItemAdapter adapter1;
	private MyCollectListItemAdapter adapter2;
	private LinearLayout loadbar;
	private boolean showActionBar = true;
	private boolean someThreadRuning = false;
	private float listWidth = 0; 
	private List<FavoriteOutfit> favoriteOutfits;
	private GuiMiDB dbOperator;
	private LocalAsyncImageLoader localAsyncImageLoader;    
	
	
	
	@SuppressLint("HandlerLeak")
	@Override 
	public void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_matches);
		
		findViewById(R.id.dlist_container).setOnTouchListener(new OnTouchListener() { 
		     @Override 
	        public boolean onTouch(View view, MotionEvent m) {
		    	 Log.i(TAG,"LinearLayout:onTouch action="+m.getAction()+"  isscroll:"+adapter1.isScroll());
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
		
		;
		
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
									MyCollectActivity.this.getActionBar()
											.show();
								someThreadRuning = false;
							}
						}, 1000);
					}
		    	}else if(OnScrollListener.SCROLL_STATE_TOUCH_SCROLL == scrollState&&showActionBar){
		    		MyCollectActivity.this.getActionBar().hide();
		    		showActionBar = false;
		    	}
		    }
		});
		
		
		
		
		adapter1 = new MyCollectListItemAdapter(this, 0);
		adapter2 = new MyCollectListItemAdapter(this, 1);
		adapter1.setOnCancelFavourateListener(new OnCancelFavourateListener() {
			
			@Override
			public void onCancelFavourate() {
				// TODO Auto-generated method stub
				getActionBar().setTitle("共有" + (list1.getCount()+list2.getCount()-4) + "件收藏");
			}
		});
		adapter2.setOnCancelFavourateListener(new OnCancelFavourateListener() {

			@Override
			public void onCancelFavourate() {
				// TODO Auto-generated method stub
				getActionBar().setTitle(
						"共有" + (list1.getCount() + list2.getCount() - 4)
								+ "件收藏");
			}
		});
		
		
		listWidth = adapter1.listWidth;
		localAsyncImageLoader = new LocalAsyncImageLoader();
		adapter1.setListView(list1);
		adapter2.setListView(list2);
		// because the following constructions spend too much time,
		// so the UI keep nothing for several seconds
		// to avoid this, I use another thread to run it here:
		UIhandler = new Handler() {
			public void handleMessage(Message msg) {
				// ////////////////////////////////////////////
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
		
		//new LoadNewPageTask().start();

		list1.setAdapter(adapter1);
		list2.setAdapter(adapter2);
		listWidth = adapter1.listWidth;
		
		//getActionBar().setTitle("共有12件收藏");
		getActionBar().setHomeButtonEnabled(true);
		list1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				Log.i("favourite","list1:on item click "+ pos);
				//Toast.makeText(MyMatchesActivity.this, "item click",1000).show();
				Intent intent = new Intent(MyCollectActivity.this,ShowMyFavorateOutfitActivity.class);
				FavoriteOutfit thisOutfit = adapter1.matches.get(pos-1);
				//Intent intent = new Intent(activity,showMyOutfitActitivity.class);
				//MyOutfit thisOutfit = matches.get(pos);
				intent.putExtra("myFavouriteOutfit", thisOutfit);
				startActivity(intent);
				finish();
			}
		});
		list2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				Log.i("favourite","list2:on item click "+ pos);
				//Toast.makeText(MyMatchesActivity.this, "item click",1000).show();
				Intent intent = new Intent(MyCollectActivity.this,ShowMyFavorateOutfitActivity.class);
				FavoriteOutfit thisOutfit = adapter2.matches.get(pos-1);
				//Intent intent = new Intent(activity,showMyOutfitActitivity.class);
				//MyOutfit thisOutfit = matches.get(pos);
				intent.putExtra("myFavouriteOutfit", thisOutfit);
				intent.putExtra("pos", ((pos-1)<<1)+1);
				startActivity(intent);;
			}
		});
		
		loadbar = (LinearLayout)findViewById(R.id.load_status);
		loadbar.setVisibility(View.VISIBLE);
		dbOperator = GuiMiDB.getInstance(this);
		GetFavoriteOutfitAsyncTask getFavoriteOutfitAsyncTask = new GetFavoriteOutfitAsyncTask(this);
		getFavoriteOutfitAsyncTask.execute();
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
		getMenuInflater().inflate(R.menu.my_collect, menu);
		return true;
	}
	
		
		//dispatch items to list
		private void dispatchItems(List<FavoriteOutfit> page){
			PageData data = new PageData();
			float increasement = 0;
			float height1 = adapter1.getHeight();
			float height2 = adapter2.getHeight();
			FavoriteOutfit item;
			for (int i = 0; i < page.size(); i++) {
				item = page.get(i);
			increasement = item.getPictureHeight() * listWidth
					/ item.getPictureWidth();
			if (increasement != Double.NaN) {
				increasement += DensityUtil.dip2px(this, 20);
			}else{
				increasement = DensityUtil.dip2px(this, 20);
			}
				Log.i(TAG,"heigth1:"+height1+"     height2:"+  height2 +"increasement:"+increasement);
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
		private Vector<FavoriteOutfit> left;
		private Vector<FavoriteOutfit> right;
		private float placeHolderViewHeight=0;

		private PageData() {
			left = new Vector<FavoriteOutfit>();
			right = new Vector<FavoriteOutfit>();
		}

		private void setPlaceHolderViewHeight(float height){
			placeHolderViewHeight = height;
		}
		
		private float getPlaceHolderViewHeight() {
			return placeHolderViewHeight;
		}
		
		public void putInFirst(FavoriteOutfit item) {
			left.add(item);
		}

		public void putInSecond(FavoriteOutfit item) {
			right.add(item);
		}

		public Vector<FavoriteOutfit> getFirst() {
			return left;
		}

		public Vector<FavoriteOutfit> getSecond() {
			return right;
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case android.R.id.home:
			this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class GetFavoriteOutfitAsyncTask extends AsyncTask<Integer,Integer,List<FavoriteOutfit>>{

		MyCollectActivity activity;
		public GetFavoriteOutfitAsyncTask(MyCollectActivity activity) {
			// TODO Auto-generated constructor stub
			this.activity = activity;
		}
		
		@Override
		protected List<FavoriteOutfit> doInBackground(Integer... arg0) {
			// TODO Auto-generated method stub
			favoriteOutfits = dbOperator.getMyFavorites();
			Log.i("favourite","获取favorite· item中");
			return favoriteOutfits;
			
		}
		
		@Override
		protected void onPostExecute(List<FavoriteOutfit> result) {
			// TODO Auto-generated method stub
			if (favoriteOutfits == null) {
				activity.getActionBar().setTitle("共有0件收藏");
				Toast.makeText(getApplicationContext(), "同步我的收藏失败！请检查网络是否连接",
						Toast.LENGTH_SHORT).show();

			} else if (favoriteOutfits.size() == 0) {
				activity.getActionBar().setTitle("共有0件收藏");
				Toast.makeText(getApplicationContext(), "同步我的收藏成功~暂时没有收藏任何搭配",
						Toast.LENGTH_SHORT).show();

			} else {
				dispatchItems(result);
				activity.getActionBar().setTitle("共有" + result.size() + "件收藏");

			}
			loadbar.setVisibility(View.GONE);
		}
		
	}
		
}
