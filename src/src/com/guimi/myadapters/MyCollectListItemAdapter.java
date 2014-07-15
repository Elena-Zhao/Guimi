package com.guimi.myadapters;

import java.util.Vector;

import com.guimi.AddTagForSingleActivity;
import com.guimi.MainActivity;
import com.guimi.R;
import com.guimi.ShowItemInfoActivity;
import com.guimi.entities.FavoriteOutfit;
import com.guimi.entities.Match;
import com.guimi.entities.MatchTag;
import com.guimi.entities.MyOutfit;
import com.guimi.myadapters.MatchListItemAdapter.GetMatchImage;
import com.guimi.myadapters.MyMatchListItemAdapter.OtherViewHolder;
import com.guimi.sqlite.GuiMiDB;
import com.guimi.sqlite.ImageHandeller;
import com.guimi.util.LocalAsyncImageLoader;
import com.guimi.util.LocalAsyncImageLoader.ImageCallback;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MyCollectListItemAdapter extends FlowListAdapter{
	private final String TAG = "com.guimi.myadapters.MyMatchListItemAdapter";
	private int colNum;
	public Vector<FavoriteOutfit> matches;
	private ListView listView;
	private Context context;
	public ImageHandeller imageHandeller;
	public OnCancelFavourateListener myCancelFavourateListener;
	
	public void setOnCancelFavourateListener(OnCancelFavourateListener listener){
		this.myCancelFavourateListener = listener;
	}
	
	public ListView getListView() {
		return listView;
	}

	public void setListView(ListView listView) {
		this.listView = listView;
	}
	
	public void deleteItemAt(int pos){
		matches.remove(pos);
		notifyDataSetChanged();
		if(myCancelFavourateListener!=null){
			myCancelFavourateListener.onCancelFavourate();
		}
	}
	public MyCollectListItemAdapter(Context context,int col){
		super(context);
		colNum = col;
		matches = new Vector<FavoriteOutfit>();
		setupFlowListAdapter(context, 0, new Vector<String>());
		imageHandeller = ImageHandeller.getInstance(context);
		this.context = context;
	}
	
	public void addItems(Vector<FavoriteOutfit> vector){
		for(int i = 0; i < vector.size(); i++){
			addItem(vector.get(i));
		}
		super.notifyDataSetChanged();
	}
	
	public void addItem(FavoriteOutfit favoriteOutfit){
		super.addItem(favoriteOutfit.getOutfitUrl(),favoriteOutfit.getPictureWidth(),favoriteOutfit.getPictureHeight());
		this.matches.add(favoriteOutfit);
	}

	class OtherViewHolder{
		public ImageView image;
		public ImageButton addBtn;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return matches.size()+1;
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
			holder.addBtn = (ImageButton)convertView.findViewById(R.id.add_image);
			convertView.setTag(holder);
			
		}else{
			holder = (OtherViewHolder)convertView.getTag();
		}
		
//		//return a null image view when scroll
//		if (isScroll) {
//			holder.addBtn.setImageResource(R.drawable.add_true);
//
//			convertView.setTag(holder);
//			super.setImageHeight(pos, convertView);
//
//			return convertView;
//		}
		
		holder.addBtn.setImageResource(R.drawable.add_true);
		// Set the image tag
		ImageView imageView = holder.image;
		String imageUrl = null;
		try {
			imageUrl = matches.get(pos).getOutfitUrl();
			imageView.setTag(pos);
		} catch (ArrayIndexOutOfBoundsException e) {
			return convertView;
		}
		// load image, if not exist in local, show a default picture
		imageView.setImageResource(android.R.color.transparent);
		new GetMatchImage(pos).execute(imageUrl);
		
		super.setImageHeight(pos, convertView);
		
		holder.addBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(context)
				.setMessage("删除单品")
				.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {
								GuiMiDB.getInstance(context).cancelFavorite(matches.get(pos).getOutfitID());
								matches.remove(pos);
								
								notifyDataSetChanged();
								if(myCancelFavourateListener!=null){
									myCancelFavourateListener.onCancelFavourate();
								}
							}
						}).setNegativeButton("取消", null).show();
	}
				
		});
		return convertView;
	}

	public interface OnCancelFavourateListener{
		public void onCancelFavourate();
	};
	class GetMatchImage extends AsyncTask<String,Void,Bitmap> {
		private int tag;
		public GetMatchImage(int tag){
			this.tag = tag;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bmp = null;
			try {
				bmp = imageHandeller.download(params[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bmp;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if ( result != null) {
				ImageView imageViewByTag = (ImageView) listView
						.findViewWithTag(tag);
				Log.i("image", "call back set image");
				if (imageViewByTag != null) {
					Log.i("url", "set image at:"+tag);
					imageViewByTag.setImageBitmap(result);
				}
			}
		}
		
	}
	
}
