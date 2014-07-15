package com.guimi.myadapters;

import java.util.Vector;

import com.guimi.R;
import com.guimi.entities.Match;
import com.guimi.entities.Outfit;
import com.guimi.entities.PublishedOutfit;
import com.guimi.myadapters.MyMatchListItemAdapter.OtherViewHolder;
import com.guimi.sqlite.GuiMiDB;
import com.guimi.sqlite.ImageHandeller;
import com.guimi.util.LocalAsyncImageLoader;
import com.guimi.util.LocalAsyncImageLoader.ImageCallback;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MyPublishListItemAdapter extends FlowListAdapter{
	private final String TAG = "com.guimi.myadapters.MyMatchListItemAdapter";
	public Vector<PublishedOutfit> matches;
	private int colNum;
	private LocalAsyncImageLoader localAsyncImageLoader = null;
	private ListView listView;
	private Context context;
	
	public void setLocalAsyncImageLoader(
			LocalAsyncImageLoader imageLoader) {
		// TODO Auto-generated method stub
		this.localAsyncImageLoader = imageLoader;
	}
	
	public void setListView(ListView list) {
		// TODO Auto-generated method stub
		listView = list;
	}

	public MyPublishListItemAdapter(Context context, int col) {

		super(context);
		colNum = col;
		matches = new Vector<PublishedOutfit>();
		setupFlowListAdapter(context, 0, new Vector<String>());

	}
	
	public void addItems(Vector<PublishedOutfit> vector){
		for(int i = 0; i < vector.size(); i++){
			addItem(vector.get(i));
		}
		super.notifyDataSetChanged();
	}
	
	public void addItem(PublishedOutfit publishedOutfit){
		super.addItem(publishedOutfit.getOutfitUrl(),publishedOutfit.getPictureWidth(),publishedOutfit.getPictureHeight());
		this.matches.add(publishedOutfit);
	}


	class OtherViewHolder{
		public ImageView image;
		public TextView describe;
		public ImageButton upBtn;
		public TextView upNum;
	}

	
	@SuppressWarnings("null")
	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(pos == matches.size()){
			return getPlaceHolderView();
		}
		Log.i(TAG,"list"+(colNum+1)+"  getView at "+ pos + " isScroll:"+isScroll);
		OtherViewHolder holder = null;
		
		
		
		if(convertView == null||convertView.equals(getPlaceHolderView())){
			
			convertView = mInflater
					.inflate(R.layout.my_publish_item, null);
			holder = new OtherViewHolder();
			holder.image = (ImageView)convertView.findViewById(R.id.image);
			holder.describe = (TextView)convertView.findViewById(R.id.describe);
			
			holder.upBtn = (ImageButton)convertView.findViewById(R.id.up_image);
			
			holder.upNum = (TextView)convertView.findViewById(R.id.up_num);
			
			convertView.setTag(holder);
			
		} else {
			holder = (OtherViewHolder) convertView.getTag();
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
		holder.upNum.setText(String.valueOf(matches.get(pos).getLikes()));
		//holder.downNum.setText(matches.get(pos).get);
		
		holder.describe.setText(String.valueOf(matches.get(pos).getOutfitDescription()));
		
		super.setImageHeight(pos, convertView);
		return convertView;
	}
	
	
}
