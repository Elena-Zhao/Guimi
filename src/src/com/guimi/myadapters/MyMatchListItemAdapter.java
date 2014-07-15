package com.guimi.myadapters;

import java.util.Vector;

import com.guimi.AddTagForSingleActivity;
import com.guimi.R;
import com.guimi.ShowMyOutfitActitivity;
import com.guimi.entities.MyOutfit;
import com.guimi.util.LocalAsyncImageLoader;
import com.guimi.util.LocalAsyncImageLoader.ImageCallback;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MyMatchListItemAdapter extends FlowListAdapter{
	private final String TAG = "com.guimi.myadapters.MyMatchListItemAdapter";
	public Vector<MyOutfit> matches;
	private LocalAsyncImageLoader localAsyncImageLoader = null;
	private ListView listView;
	private Context context;
	
	
	
	public ListView getListView() {
		return listView;
	}

	public void setListView(ListView listView) {
		this.listView = listView;
	}

	public LocalAsyncImageLoader getLocalAsyncImageLoader() {
		return localAsyncImageLoader;
	}

	public void setLocalAsyncImageLoader(LocalAsyncImageLoader localAsyncImageLoader) {
		this.localAsyncImageLoader = localAsyncImageLoader;
	}
	






	private int colNum;
	
	public MyMatchListItemAdapter(Context context,int col){
		
		super(context);
		this.context = context;
		colNum = col;
		matches = new Vector<MyOutfit>();
		setupFlowListAdapter(context, 0, new Vector<String>());
		
	}
	
	public void addItems(Vector<MyOutfit> items){
		for(int i = 0; i < items.size(); i++){
			addItem(items.get(i));
		}
		super.notifyDataSetChanged();
	}
	
	public void addItem(MyOutfit item){
		super.addItem(item.getOutfitUrl(),item.getPictureWidth(),item.getPictureHeight());
		this.matches.add(item);
	}

	class OtherViewHolder{
		public ImageView image;
		public ImageButton publishBtn;
	}

	
	@Override
	public View getView(final int pos, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.i(TAG,"list"+(colNum+1)+"  getView at "+ pos + " isScroll:"+isScroll);
		if(pos == matches.size()){
			return getPlaceHolderView();
		}
		Log.i(TAG,"list"+(colNum+1)+"  matches size is  "+ matches.size());
		
		OtherViewHolder holder = null;
		
		
		
		if(convertView == null||convertView.equals(getPlaceHolderView())){
			
			convertView = mInflater
					.inflate(R.layout.my_match_item, null);
			holder = new OtherViewHolder();
			holder.image = (ImageView)convertView.findViewById(R.id.image);
			holder.publishBtn = (ImageButton)convertView.findViewById(R.id.add_image);
			convertView.setTag(holder);
			
		}else{
			holder = (OtherViewHolder)convertView.getTag();
		}
//		
//		//return a null image view when scroll
//		if (isScroll) {
//			holder.image.setImageResource(android.R.color.transparent);
//
//			convertView.setTag(holder);
//			super.setImageHeight(pos, convertView);
//
//			return convertView;
//		}
		if(matches.get(pos).isPublished()){
			holder.publishBtn.setImageResource(android.R.color.transparent);
		}else{
			holder.publishBtn.setImageResource(R.drawable.publish_btn);
		}
		String imageUrl = getImage(pos);

		ImageView imageView = holder.image;

		imageView.setTag(pos);

		Drawable cachedImage = localAsyncImageLoader.loadDrawable(pos, imageUrl,
				new ImageCallback() {

					public void imageLoaded(Drawable imageDrawable,
							int pos) {

						ImageView imageViewByTag = (ImageView) listView
								.findViewWithTag(pos);
						Log.i("aaa","call back set image");
						if (imageViewByTag != null) {
							Log.i("aaa","set image");
							imageViewByTag.setImageDrawable(imageDrawable);

						}

					}

				});

		if (cachedImage == null) {

			imageView.setImageResource(android.R.color.transparent);

		} else {

			imageView.setImageDrawable(cachedImage);

		}
		//holder.image.setImageBitmap(singleImage);
		
		super.setImageHeight(pos, convertView);
		
		holder.publishBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.i("bbb","publish btn clicked");
				Intent intent = new Intent(context,
						AddTagForSingleActivity.class);
				MyOutfit thisOutfit = matches.get(pos);
				intent.putExtra("url", thisOutfit.getOutfitUrl());
				intent.putExtra("outfitId", thisOutfit.getOutfitID());
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	
	
}
