package com.guimi.myviews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class NonInterreptImageBtn extends ImageButton{

	private final String TAG = "com.guimi.myviews.NonInterreptImageBtn";
	private OnClickListener onClick;
	public NonInterreptImageBtn(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public NonInterreptImageBtn(Context context, AttributeSet attrs){  
        super(context, attrs);  
    }  
       
    public NonInterreptImageBtn(Context context, AttributeSet attrs, int defStyle){  
        super(context, attrs, defStyle);  
    }  
    @Override   
    public boolean onTouchEvent(MotionEvent ev) {
    	Log.i("bbb","NonInterreptImageBtn:onTouchEvent");
    	super.onTouchEvent(ev);
		return true;   
    }
    
//    @Override 
//    public void setOnClickListener(OnClickListener listener){
//    	this.onClick = listener;
//    }
//    public void  performButtonClick(){
//    	Log.i("bbb","NonInterreptImageBtn:performButtonClick");
//    	if(onClick != null)
//    		onClick.onClick(this);
//    }
}
