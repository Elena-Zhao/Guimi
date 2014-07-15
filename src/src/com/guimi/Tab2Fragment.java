package com.guimi;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.guimi.entities.Item;
import com.guimi.myadapters.TypeButtonAdapter;
import com.guimi.myviews.HorizontalListView;
import com.guimi.myviews.MyDrawerLayout;
import com.guimi.sqlite.GuiMiDB;
import com.guimi.util.DensityUtil;
import com.guimi.util.LocalAsyncImageLoader;
import com.guimi.util.LocalAsyncImageLoader.ImageCallback;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ViewSwitcher.ViewFactory;

public class Tab2Fragment extends Fragment implements ViewFactory {
	private MyDrawerLayout mDrawer;
	private HorizontalListView list;
	private ImageSwitcher clothesSwitcher;
	private Gallery coatDallery;
	// private List<Item>
	private GuiMiDB dbOperation;
	private GridView bookShelf;
	private View movingView;
	private List<Item> singleItems=null;
	private RadioGroup group;
	private LinearLayout loadBar;
	private Handler UIhandler;
	private LocalAsyncImageLoader imageLoader;
	boolean[] checkFlag = {true,true,true,true,true,true};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

			
		View view = inflater.inflate(R.layout.tab2_fragment, container, false);
		// 初始化数据库
		dbOperation = GuiMiDB.getInstance(getActivity());
		singleItems = new ArrayList<Item>();
		//albumHelper = AlbumHelper.getHelper();
		
		// 数据库获取所有单品
		loadBar = (LinearLayout) view.findViewById(R.id.load_status);
		loadBar.setVisibility(View.VISIBLE);
		
		
		

		// 初始化抽屉

		mDrawer = (MyDrawerLayout) view.findViewById(R.id.mydrawer);
		mDrawer.initDrawer();

		// generate the list of type buttons
		list = (HorizontalListView) view.findViewById(R.id.type_buttons);
		list.setAdapter(new TypeButtonAdapter(list, view.getContext()));
		list.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				
				((TypeButtonAdapter) list.getAdapter()).turn(pos);
				int lastPos = -1;
				for(int i = 0; i < checkFlag.length; i++){
					if (!checkFlag[i]) {
						lastPos = i;
						break;
					}
				}
				checkFlag[pos]= !checkFlag[pos];
				if(lastPos == pos){
					pos = -1;
				}else if(lastPos != -1){
					((TypeButtonAdapter) list.getAdapter()).turn(lastPos);
					checkFlag[lastPos]= !checkFlag[lastPos];
				}
				
				
				switch (pos) {
				case 0:
					singleItems = (dbOperation.getItemsByType(3));
					break;
				case 1:
					singleItems = dbOperation.getItemsByType(5);
					break;
				case 2:
					singleItems = dbOperation.getItemsByType(1);
					break;
				case 3:
					singleItems = dbOperation.getItemsByType(4);
					break;
				case 4:
					singleItems = dbOperation.getItemsByType(0);
					break;
				case 5:
					singleItems = dbOperation.getItemsByType(2);
					break;
				default:
					singleItems = dbOperation.getItems();
					break;
				}
				
				
				((ShlefAdapter)(bookShelf.getAdapter())).notifyDataSetChanged();
				coatDallery.getOnItemSelectedListener().onItemSelected(coatDallery, null, 0, 0);
				((ImageAdapter)(coatDallery.getAdapter())).notifyDataSetChanged();
			}
		});

		// 初始化列表模式及轮播模式
		bookShelf = (GridView) view.findViewById(R.id.clothingShelf);
		clothesSwitcher = (ImageSwitcher) view.findViewById(R.id.switcher);
		coatDallery = (Gallery) view.findViewById(R.id.coat_gallery);
		movingView = view.findViewById(R.id.movingView);
		initViewByList();
		initViewByMoving();
		group = (RadioGroup) view.findViewById(R.id.view_ways);
		
		
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int radioButtonId = group.getCheckedRadioButtonId();
				if (radioButtonId == R.id.mode_list) {
					//initViewByList();
					movingView.setVisibility(View.GONE);
					bookShelf.setVisibility(
							View.VISIBLE);
				} else {
					//initViewByMoving();
					bookShelf.setVisibility(
							View.GONE);
					movingView.setVisibility(
							View.VISIBLE);
					
				}
			}

		});

		imageLoader = new LocalAsyncImageLoader();
		
		UIhandler = new Handler(){
			@Override
			public void dispatchMessage(Message msg) {
				// TODO Auto-generated method stub
				if(singleItems==null||singleItems.size() == 0){
//					Toast toast = Toast.makeText(getActivity(),
//							"您的衣柜暂无单品，赶快去添加吧！^^", Toast.LENGTH_SHORT);
//					toast.show();
				}else{
					((ShlefAdapter)(bookShelf.getAdapter())).notifyDataSetChanged();
					((ImageAdapter)(coatDallery.getAdapter())).notifyDataSetChanged();
					loadBar.setVisibility(View.GONE);
				}
			}
		};
		
//		Thread loadData = new Thread(new Runnable(){
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				try {
//					singleItems = dbOperation.getItems();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				UIhandler.sendMessage(UIhandler.obtainMessage());
//				
//			}
//			
//		});
//		loadData.start();
		int viewId = group.getCheckedRadioButtonId();
		if(viewId == R.id.mode_list){
			movingView.setVisibility(View.GONE);
			bookShelf.setVisibility(
				View.VISIBLE);
		}
		else{
			bookShelf.setVisibility(View.GONE);
			movingView.setVisibility(
				View.VISIBLE);
		}
		return view;
	}

	

	public void initViewByMoving() {
		// this.getActivity().setContentView(R.layout.tab2_fragment_moving);

		
		clothesSwitcher.setFactory(this);

		clothesSwitcher.setInAnimation(AnimationUtils.loadAnimation(
				this.getActivity(), android.R.anim.fade_in));
		clothesSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
				this.getActivity(), android.R.anim.fade_out));
		
		//coatDallery.setUnselectedAlpha(0.3f);
		
		coatDallery.setAdapter(new ImageAdapter(this.getActivity()));
		
		coatDallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (singleItems != null&&singleItems.size() != 0) {
					String url = singleItems.get(position).getItemUrl();
					Options options = new Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeFile(url, options);

					options.inSampleSize = LocalAsyncImageLoader
							.computeSampleSize(options, -1, 512 * 512);
					options.inJustDecodeBounds = false;
					Bitmap singleBitmap = BitmapFactory
							.decodeFile(url, options);

					Drawable drawable = new BitmapDrawable(singleBitmap);
					clothesSwitcher.setImageDrawable(drawable);
					clothesSwitcher.setTag(position);
				}else{
					clothesSwitcher.setImageResource(android.R.color.transparent);
					clothesSwitcher.setTag(-1);
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				clothesSwitcher.setImageResource(R.drawable.null_image_icon);
				clothesSwitcher.setTag(-1);
			}
		});
		
		clothesSwitcher.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				int pos = (Integer) clothesSwitcher.getTag();
				if (pos >= 0) {
					Item item = singleItems.get(pos);
					Intent itemIntent = new Intent(getActivity(),
							ShowItemInfoActivity.class);
					itemIntent.putExtra("item", item);
					startActivity(itemIntent);
				}
			}
		});

	}

	public void initViewByList() {
		
		ShlefAdapter adapter = new ShlefAdapter();
		bookShelf.setAdapter(adapter);
		bookShelf.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				Item item = singleItems.get(pos);
				Intent itemIntent = new Intent(getActivity(), ShowItemInfoActivity.class);
				itemIntent.putExtra("item", item);
				startActivity(itemIntent);
			}
		});

	}

	@Override
	public View makeView() {
		ImageView i = new ImageView(this.getActivity());
		
		//???????????
		i.setBackgroundColor(0xFFFFFF);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return i;
	}

	public class ImageAdapter extends BaseAdapter {
		public ImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			if (singleItems == null) {
				return 0;
			} else {
				return singleItems.size();
			}

		}

		public Object getItem(int position) {
			return singleItems.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView image = null;
			if(convertView == null){
				convertView = new LinearLayout(mContext);
				convertView.setTag("container"+position);
				image = new ImageView(mContext);
				image.setTag(position);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(150,LayoutParams.MATCH_PARENT);
				lp.setMargins(5, 5, 5, 5);
				image.setLayoutParams(lp);
				image.setAdjustViewBounds(true);
				image.setScaleType(ScaleType.FIT_CENTER);
				((LinearLayout)convertView).addView(image);
				convertView.setBackgroundResource(R.drawable.select_image_bg);
			}else{
				image = (ImageView) convertView.findViewWithTag(position);
			}
			//Uri singleUri = Uri.parse(singleItems.get(position).getItemUrl());
			image.setTag(position);
			Drawable drawable = imageLoader.loadDrawable(position, singleItems.get(position).getItemUrl(), 
					new ImageCallback() {
						
						@Override
						public void imageLoaded(Drawable imageDrawable, int pos) {
							// TODO Auto-generated method stub
							ImageView image = null;
							try {
								image = (ImageView) coatDallery.findViewWithTag(pos);
							} catch (Exception e) {
							}
							if(image != null ){
								image.setImageDrawable(imageDrawable);
							}
						}
					});
			if(drawable == null ){
				image.setImageResource(R.drawable.null_image_icon);
			}else{
				image.setImageDrawable(drawable);
			}
			return convertView;
		}

		private Context mContext;

	}

	class ShlefAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (singleItems == null) {
				return 0;
			} else {
				return singleItems.size();
			}

		}

		@Override
		public Object getItem(int pos) {
			// TODO Auto-generated method stub
			return singleItems.get(pos);
		}

		@Override
		public long getItemId(int pos) {
			// TODO Auto-generated method stub
			return pos;
		}

		@Override
		public View getView(int position, View contentView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ImageView image = null;
			if (contentView == null) {
				contentView = LayoutInflater.from(
						getActivity().getApplicationContext()).inflate(
						R.layout.item1, null);
				image = (ImageView) contentView
						.findViewById(R.id.clothesView);
				image.setBackgroundResource(R.drawable.shelf_bg);
				image.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, DensityUtil.dip2px(
								getActivity(), 150)));
			}else{
				image = (ImageView) contentView
						.findViewById(R.id.clothesView);
			}
			
			

			image.setTag(position);
			Drawable drawable = imageLoader.loadDrawable(position, singleItems.get(position).getItemUrl(), 
					new ImageCallback() {
						
						@Override
						public void imageLoaded(Drawable imageDrawable, int pos) {
							// TODO Auto-generated method stub
							ImageView image = null;
							try {
								image = (ImageView) bookShelf.findViewWithTag(pos);
							} catch (Exception e) {
							}
							if(image != null ){
								image.setImageDrawable(imageDrawable);
							}
						}
					});
			if(drawable == null ){
				image.setImageResource(R.drawable.null_image_icon);
			}else{
				image.setImageDrawable(drawable);
			}
			
			
			return contentView;

		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		int lastPos = -1;
		for(int i = 0; i < checkFlag.length; i++){
			if (!checkFlag[i]) {
				lastPos = i;
				break;
			}
		}
		switch (lastPos) {
		case 0:
			singleItems = (dbOperation.getItemsByType(3));
			break;
		case 1:
			singleItems = dbOperation.getItemsByType(5);
			break;
		case 2:
			singleItems = dbOperation.getItemsByType(1);
			break;
		case 3:
			singleItems = dbOperation.getItemsByType(4);
			break;
		case 4:
			singleItems = dbOperation.getItemsByType(0);
			break;
		case 5:
			singleItems = dbOperation.getItemsByType(2);
			break;
		default:
			singleItems = dbOperation.getItems();
			break;
		}
		
		
		((ShlefAdapter)(bookShelf.getAdapter())).notifyDataSetChanged();
		coatDallery.getOnItemSelectedListener().onItemSelected(coatDallery, null, 0, 0);
		((ImageAdapter)(coatDallery.getAdapter())).notifyDataSetChanged();
		loadBar.setVisibility(View.GONE);
		super.onResume();
	}

}
