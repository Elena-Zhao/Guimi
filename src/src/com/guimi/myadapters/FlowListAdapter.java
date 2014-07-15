package com.guimi.myadapters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.guimi.R;
import com.guimi.util.DensityUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FlowListAdapter extends BaseAdapter{
	//The string used to debug
	private final String TAG = "com.guimi.myadapters.FlowListAdapter";
	
	//is the list scrolling
	protected boolean isScroll = false;
	//dictionary for the picture height
	protected Map<Integer, Integer> picHeight;
	//the images in this list
	private Vector<String> images;
	//the number of the items
	protected int count;
	//inflater to get view from layout resourec
	protected LayoutInflater mInflater;
	//the width of the listview
	public float listWidth = 0;
	//the height of the list
	protected int height = 0;
	private Context context;
	
	//hold the null space at the end of the list
	private LinearLayout placeHolderView;
	
	
	//constructor
	public FlowListAdapter(Context context){
		WindowManager wm = (WindowManager)context
                .getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		this.context = context;
		listWidth = width/2 - DensityUtil.dip2px(context, 2);
		placeHolderView = new LinearLayout(context);
		AbsListView.LayoutParams aParams = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT,0);
		placeHolderView.setLayoutParams(aParams);
		count = 1;
	};
	public void setupFlowListAdapter(Context context, int pagesize, Vector<String> urls){
		count = pagesize;
		this.mInflater = LayoutInflater.from(context);
		picHeight = new HashMap<Integer, Integer>();
		this.images = urls;
	}
	
	
	
	/**this class is used to get the height of the item view
	 * because in the getView method, the view is not rendered, so it returns 0
	 * @param v
	 * this v is the view you want to measure 
	 *  
	**/
	private class MyOnGlobalLayoutListener implements OnGlobalLayoutListener{
		private View v;
		private int pos;
		public MyOnGlobalLayoutListener(View v,int pos){
			this.v = v;
			this.pos = pos;
		}
		@SuppressLint("NewApi")
		@Override
		public void onGlobalLayout() {
	        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
	            v.getViewTreeObserver().removeOnGlobalLayoutListener(this);
	        } else {
	            v.getViewTreeObserver().removeGlobalOnLayoutListener(this);
	        }

	        listWidth = v.getWidth();
	    }
		
	};
	
	public int getHeight(){
		return height;
	}
	
	protected void addItem(String url, float width, float height){
		images.add(url);
		int item_height = measureImageHeight(width,height);
		if(listWidth!=0){
			picHeight.put(count, item_height);
			height += item_height;
		}
		count++;
	}
	protected void insertItemAtFirst(String url, float width, float height){
		images.insertElementAt(url,0);
		int item_height = measureImageHeight(width,height);
		if(listWidth!=0){
			picHeight.put(count, item_height);
			height += item_height;
		}
		count++;
	}
	
	
	public void setPlaceHolderView(float height){
		AbsListView.LayoutParams aParams = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT,(int)height);
		placeHolderView.setLayoutParams(aParams);
	}
	
	public View getPlaceHolderView(){
		return placeHolderView;
	}
	
//	protected void addItems(String[] drawable){
//		for(int i = 0;i<drawable.length;i++){
//			addItem(drawable[i]);
//		}
//		this.notifyDataSetChanged();
//	}
	
	public boolean isScroll() {
		return isScroll;
	}

	public void setScroll(boolean isScroll) {
		this.isScroll = isScroll;
		if(!isScroll){
			//this.notifyDataSetChanged();
		}
	}
	
	class ViewHolder{
//		public TextView name;
		public ImageView image;
//		public ImageButton addBtn;
//		public ImageButton upBtn;
//		public ImageButton downBtn;
//		public TextView addNum;
//		public TextView upNum;
//		public TextView downNum;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return images.size()+1;
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return images.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.i(TAG,"list"+"  getView at "+ pos + " isScroll:"+isScroll);
		ViewHolder holder = null;
		if(pos == images.size()){
			return getPlaceHolderView();
		}
		
		
		if(convertView == null){
			
			convertView = mInflater
					.inflate(R.layout.my_match_item, null);
			holder = new ViewHolder();
			holder.image = (ImageView)convertView.findViewById(R.id.image);
			convertView.setTag(holder);
			
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		//return a null image view when scroll
				if(isScroll){
						
					holder.image.setImageResource(android.R.color.transparent);
					convertView.setTag(holder);
					
					setImageHeight(pos, convertView);
					return convertView;
				}
		
		setImageHeight(pos, convertView);
		
		return convertView;
	}
	
	public void setImageHeight(int pos, View convertView){
		if(picHeight.containsKey(new Integer(pos))){
			//set the view height
			Log.i(TAG,"getHeight at "+pos+":"+(Integer) picHeight.get(pos));
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams( LinearLayout.LayoutParams.FILL_PARENT, (Integer) picHeight.get(pos)); 
			convertView.setLayoutParams(lp);
		}
	}
	
	public int measureImageHeight(float width ,float height){
		if(listWidth!=0){
			return (int) (height*(listWidth/width)+DensityUtil.dip2px(context, 20));
		}else{
			return -1;
		}
	}
	
	public String getImage(int pos){
		return images.get(pos);
	}
}
