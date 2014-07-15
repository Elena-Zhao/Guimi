package com.guimi.myviews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class NonInterreptLinearLayout extends LinearLayout{

	private final String TAG = "com.guimi.myviews.NonInterreptLinearLayout";
	public NonInterreptLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public NonInterreptLinearLayout(Context context, AttributeSet attrs){  
        super(context, attrs);  
    }  
       
    public NonInterreptLinearLayout(Context context, AttributeSet attrs, int defStyle){  
        super(context, attrs, defStyle);  
    }  
    
    @Override   
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	Log.i(TAG,"NonInterreptLinearLayout:InterceptTouchEvent");
		return true;   
    }
    
    
}
