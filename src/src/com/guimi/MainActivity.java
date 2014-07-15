package com.guimi;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import com.guimi.entities.PersonInfo;
import com.guimi.myviews.UploadDialog;
import com.guimi.uploadPhoto.HandlePic;

import android.os.Bundle;
import android.os.Handler;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		OnTabChangeListener {

	private TabHost mTabHost;
	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();
	private String[] tabTitle = { "潮流圈", "我的衣柜", "搭配空间" };
	private List<Fragment> fragmentList;
	private int lastTabPos = 1;
	private int backPressCount = 0;
	private FragmentTransaction ftransaction;
	private HandlePic handlePic;
	private boolean loginState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		loginState = PersonInfo.userName != null;
		//loginState =true;
		
		// generate the tabhost
		this.initialiseTabHost(savedInstanceState);

		// generate the fragments
		fragmentList = new Vector<Fragment>();

		Fragment tab1Fragment = new Tab1Fragment();
		Fragment tab2Fragment = new Tab2Fragment();
		Fragment tab3Fragment = new Tab3Fragment();
		Fragment nullFragment = new AskLoginFragment();

		fragmentList.add(tab1Fragment);
		fragmentList.add(tab2Fragment);
		fragmentList.add(tab3Fragment);
		fragmentList.add(nullFragment);

		// select the first tab by default
		mTabHost.setCurrentTab(0);
		lastTabPos = 0;
		FragmentManager fmanager = getSupportFragmentManager();
		ftransaction = fmanager.beginTransaction();
		ftransaction.add(R.id.fragment_content, fragmentList.get(0));
		ftransaction.add(R.id.fragment_content, fragmentList.get(1));
		ftransaction.hide(fragmentList.get(1));
		ftransaction.add(R.id.fragment_content, fragmentList.get(2));
		ftransaction.hide(fragmentList.get(2));
		ftransaction.add(R.id.fragment_content, fragmentList.get(3));
		ftransaction.hide(fragmentList.get(3));
		ftransaction.commit();

		// remember the current tab
		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); 
			// set the tab as per the saved state
		}
		handlePic = new HandlePic("GuiMi", this);
		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("tab", mTabHost.getCurrentTabTag()); // save the tab
																// selected
		super.onSaveInstanceState(outState);
	}

	private void initialiseTabHost(Bundle args) {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		TabInfo tabInfo = null;

		MainActivity.AddTab(0, this, this.mTabHost,
				this.mTabHost.newTabSpec("tab1").setIndicator(getTabView(0)),
				(tabInfo = new TabInfo("Tab1", Tab1Fragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);

		MainActivity.AddTab(1, this, this.mTabHost,
				this.mTabHost.newTabSpec("tab2").setIndicator(getTabView(1)),
				(tabInfo = new TabInfo("Tab2", Tab2Fragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);

		MainActivity.AddTab(2, this, this.mTabHost,
				this.mTabHost.newTabSpec("tab3").setIndicator(getTabView(2)),
				(tabInfo = new TabInfo("Tab3", Tab3Fragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);

		mTabHost.setOnTabChangedListener(this);
	}
	

	/**
	 * Add Tab content to the Tabhost
	 * 
	 * @param activity
	 * @param tabHost
	 * @param tabSpec
	 * @param clss
	 * @param args
	 */
	private static void AddTab(int index, MainActivity activity,
			TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
		// Attach a Tab view factory to the spec
		tabHost.addTab(tabSpec.setContent(activity.new TabFactory(activity)));
	}

	private View getTabView(int index) {
		View view = getLayoutInflater().inflate(R.layout.tab, null);
		TextView text = (TextView) view.findViewById(R.id.tab_text);
		text.setText(tabTitle[index]);
		return view;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
	 */
	public void onTabChanged(String tag) {
		// TabInfo newTab = this.mapTabInfo.get(tag);
		int pos = this.mTabHost.getCurrentTab();
		FragmentManager fmanager = getSupportFragmentManager();
		FragmentTransaction ftransaction = fmanager.beginTransaction();

		if (lastTabPos < pos) {
			ftransaction.setCustomAnimations(R.anim.slide_right_in,
					R.anim.slide_left_out);
		} else {
			ftransaction.setCustomAnimations(R.anim.slide_left_in,
					R.anim.slide_right_out);
		}
		
		
		ftransaction.hide(fragmentList.get(lastTabPos));
		if(!loginState&&pos!=0){
			ftransaction.show(fragmentList.get(3));
		}else{
			ftransaction.show(fragmentList.get(pos));
			if(pos == 2){
				((Tab3Fragment)fragmentList.get(2)).notifyTabChanged();
			}
		}
		
		

		ftransaction.commit();
		lastTabPos = pos;
		getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);

	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if(loginState){
			menu.findItem(R.id.action_logout).setVisible(true);
		}else{
			menu.findItem(R.id.action_logout).setVisible(false);
		}
		if (lastTabPos == 1) {
			menu.findItem(R.id.action_add_collocation).setVisible(false);
			menu.findItem(R.id.action_add_clothes).setVisible(true);
			
		} else if (lastTabPos == 0) {
			menu.findItem(R.id.action_add_clothes).setVisible(false);
			menu.findItem(R.id.action_add_collocation).setVisible(true);
			menu.findItem(R.id.action_add_collocation).setVisible(false);

		} else {
			menu.findItem(R.id.action_add_clothes).setVisible(false);
			menu.findItem(R.id.action_add_collocation).setVisible(false);
		}
		super.onPrepareOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		
		
		if (!loginState) {

			Dialog dialog = new UploadDialog(this, R.layout.ask_login_page,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (v.getId() == R.id.login_btn) {
								Intent intent = new Intent(MainActivity.this,
										LoginActivity.class);
								startActivity(intent);
								// MainActivity.this.finish();
							} else {
								Intent intent = new Intent(MainActivity.this,
										SignupActivity.class);
								startActivity(intent);

							}
						}

					});
			dialog.show();
		
		}
		else if (item.getItemId() == R.id.action_add_clothes) {
			Dialog dialog = new UploadDialog(this,
					R.layout.upload_colthes_dialog, new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (v.getId() == R.id.upload_single_from_local) {
								handlePic
										.dispatchTakePictureIntent(HandlePic.ACTION_GET_LOCAL_PHOTO);

							} else {
								handlePic
										.dispatchTakePictureIntent(HandlePic.ACTION_TAKE_PHOTO);
							}
						}

					});
			dialog.show();
		}else if(item.getItemId() == R.id.action_logout){
			SharedPreferences userInfo = getSharedPreferences("user_info",MODE_PRIVATE);
			Editor editor = userInfo.edit();
			editor.remove("userId");  
			editor.remove("userName");
			editor.remove("password");
			editor.putString("logedUserId", PersonInfo.userId);
			editor.commit();  
			PersonInfo.userId = null;
			PersonInfo.userName = null;
			PersonInfo.password = null;

			Intent intent = new Intent(this, LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			finish();
			startActivity(intent);
		}
		return super.onMenuItemSelected(featureId, item);
	}

	
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		Log.i("aaa","onNewIntent");
		loginState = PersonInfo.userName != null;
		this.onTabChanged("tag");
		
		int fragId = intent.getIntExtra("fragment", -1);
		if(fragId!=-1){
			mTabHost.setCurrentTab(fragId);
		}
		String url = handlePic.getmCurrentPhotoPath();
		if(url!=null){
			Intent intent1 = new Intent(this, DescribeSingleActivity.class);
			intent1.putExtra("url", url);
			handlePic.setmCurrentPhotoPath(null);
			//Log.e("Photo!!!", "right in tab !!!!!!!!!!!!!!!!!!!!!");
			startActivity(intent1);
		}

	}



	private class TabInfo {
		private String tag;
		private Class<?> clss;
		private Bundle args;

		TabInfo(String tag, Class<?> clazz, Bundle args) {
			this.tag = tag;
			this.clss = clazz;
			this.args = args;
		}
	}

	/**
	 * A simple factory that returns dummy views to the Tabhost
	 */
	class TabFactory implements TabContentFactory {

		private final Context mContext;

		/**
		 * @param context
		 */
		public TabFactory(Context context) {
			mContext = context;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
		 */
		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (backPressCount == 0) {
				Toast.makeText(MainActivity.this, "再按一次返回键退出闺蜜！",
						Toast.LENGTH_SHORT).show();
				backPressCount = 1;
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						backPressCount = 0;
					}
				}, 2000);

			} else if (backPressCount == 1) {
				finish();
				//System.exit(0);
			}
			return true;
		} else
			return super.onKeyDown(keyCode, event);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("bbb", "result" + resultCode);
		switch (requestCode) {

		case HandlePic.ACTION_TAKE_PHOTO: {
			Log.e("bbb", "result" + resultCode);
			if (resultCode == RESULT_OK) {
				String url = handlePic.getmCurrentPhotoPath();
				Intent intent = new Intent(this, DescribeSingleActivity.class);
				intent.putExtra("url", url);
				// Log.e("Photo!!!", "right in tab !!!!!!!!!!!!!!!!!!!!!");
				startActivity(intent);
				// finish();
			}
			break;
		}
		case HandlePic.ACTION_GET_LOCAL_PHOTO: {
			Log.e("bbb", "result" + resultCode);
			if (resultCode == RESULT_OK) {

				String url = handlePic.getmCurrentPhotoPath();
				System.out.println("!!!!!!!!!!!!!" + url);

				Intent intent = new Intent(this, DescribeSingleActivity.class);
				intent.putExtra("url", url);
				Log.e("Photo!!!", "right in tab !!!!!!!!!!!!!!!!!!!!!");
				startActivity(intent);
				// test
				// finish();

			}
		}
		}
		handlePic.setmCurrentPhotoPath(null);
	}
	

}
