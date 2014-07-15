package com.guimi.myadapters;

import java.util.zip.Inflater;

import com.guimi.R;
import com.guimi.myviews.HorizontalListView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class AddTypeButtonAdapter extends BaseAdapter{
	
	public String[] name = {"鞋","饰品","裤子","包","上衣","裙装"};
	public int[] image = {R.drawable.shoe,R.drawable.accessories,R.drawable.trousers,R.drawable.bag,
			R.drawable.shirts,R.drawable.dresses};
	public int[] bg = {R.drawable.shoe_bg,R.drawable.accessories_bg,R.drawable.trousers_bg,R.drawable.bag_bg,
			R.drawable.shirts_bg,R.drawable.dresses_bg};
	
	public int[] color = {0xffe52c2c,0xfff46969,0xffffc52a,0xff44d377,0xff95d9e0,0xffd0d2d3};
	private int count;
	private int currentSelected;
	private LayoutInflater mInflater;
	private OnClickListener listener;
	//private HorizontalListView list;
	
	public AddTypeButtonAdapter(OnClickListener click_listener, Context context){
		count = name.length;
		currentSelected = -1;
		//this.list  = list2;
		listener = click_listener;
		this.mInflater = LayoutInflater.from(context);
	}
	
	class ViewHolder{
		public TextView name;
		public ImageButton image;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return count;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = mInflater
					.inflate(R.layout.add_type_btn, null);
			holder = new ViewHolder();
			holder.name = (TextView)convertView.findViewById(R.id.type_name);
			holder.image = (ImageButton)convertView.findViewById(R.id.type_image);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.name.setText(name[pos]);
		holder.name.setTextColor(color[pos]);
		holder.image.setImageResource(image[pos]);
		holder.image.setBackgroundResource(bg[pos]);
		holder.image.setOnClickListener(listener);
		holder.image.setTag(name[pos]);
		
		return convertView;
	}
	

}
