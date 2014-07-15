package com.guimi.myviews;

import java.util.Vector;

import com.guimi.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TagGroup extends LinearLayout{
	private TextView typeName;
	private Vector<Button> tags;
	private OnClickListener clickListener;
	private GridLayout container;
	private Context context;

	public TagGroup(Context context, String name, String[] tagGroup, OnClickListener clicklistener) {
		this(context,null);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.typeName = new TextView(context);
		typeName.setText(name);
		typeName.setTextColor(Color.parseColor("#702044"));
		
		tags = new Vector<Button>();
		for(int i = 0; i<tagGroup.length; i++){
			tags.add(new Button(context));
			tags.get(i).setText(tagGroup[i]);
			tags.get(i).setBackgroundResource(R.drawable.tag_add_btn);
			tags.get(i).setTextSize(15);
			tags.get(i).setTextColor(Color.parseColor("#702044"));
			tags.get(i).setOnClickListener(clicklistener);
			tags.get(i).setTag(i);
		}
		
		this.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams lp = new  LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		lp.setMargins(50, 0, 50, 15);
		this.setLayoutParams(lp);
		
		container = new GridLayout(context);
		container.setColumnCount(3);
		for(int i = 0; i<tags.size(); i++){
			container.addView(tags.get(i));
		}
		
		this.addView(typeName);
		this.addView(container);
		this.clickListener = clicklistener;
	}
	
	public TagGroup(Context context, String name, OnClickListener clicklistener, boolean showRemoveBtn, OnClickListener onRemove) {
		this(context,null);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.clickListener = clicklistener;
		this.typeName = new TextView(context);
		
		typeName.setText(name);
		typeName.setTextColor(Color.parseColor("#702044"));
		typeName.setTextSize(18);
		
		tags = new Vector<Button>();
		
		this.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams lp = new  LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		lp.setMargins(50, 0, 50, 15);
		this.setLayoutParams(lp);
		
		container = new GridLayout(context);
		container.setColumnCount(3);
		
		if(!showRemoveBtn){
			this.addView(typeName);
		}else{
			RelativeLayout titleBar = new RelativeLayout(context);
			titleBar.setGravity(Gravity.CENTER_VERTICAL);
			RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
			rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			titleBar.addView(typeName,rp);

			rp = new RelativeLayout.LayoutParams(72,72);
			rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			Button removeBtn = new Button(context);
			removeBtn.setText("");
			removeBtn.setBackgroundResource(R.drawable.delete_btn);
			removeBtn.setTag(this);
			removeBtn.setOnClickListener(onRemove);
			titleBar.addView(removeBtn,rp);
			this.addView(titleBar);
		}
		this.addView(container);
	}
	
	public void addTag(String name, int index){
		Button tag = new Button(context);
		tag.setText(name);
		tag.setBackgroundResource(R.drawable.tag_delete_btn);
		tag.setTextSize(15);
		tag.setTextColor(Color.parseColor("#702044"));
		tag.setOnClickListener(clickListener);
		tag.setTag(index);
		tags.add(tag);
		container.addView(tag);
	}
	
	public void removeTag(View tag){
		tags.remove(tag);
		container.removeView(tag);
	}
	public TagGroup(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		// TODO Auto-generated constructor stub
	}
	public TagGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

}
