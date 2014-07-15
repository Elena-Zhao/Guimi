package com.guimi.myviews;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.guimi.myadapters.FlowListAdapter;

public class BounceListViewForTab1 extends ListView{  
	private final String TAG = "com.guimi.myviews.BounceListView";
    private static final int MAX_Y_OVERSCROLL_DISTANCE = 0;  
    private FlowListAdapter adapter;
       
    private Context mContext;  
    private int mMaxYOverscrollDistance;  
    private OnItemClickListener onItemClickListener;
       
    private int lastY = 0;
    public BounceListViewForTab1(Context context){  
        super(context);  
        mContext = context;  
        initBounceListView();  
        this.requestDisallowInterceptTouchEvent(true);
    }  
       
    public BounceListViewForTab1(Context context, AttributeSet attrs){  
        super(context, attrs);  
        mContext = context;
        initBounceListView();  
    }  
    
    
       
    @Override
	public void setAdapter(ListAdapter adapter) {
		// TODO Auto-generated method stub
		super.setAdapter(adapter);
		this.adapter = (FlowListAdapter)adapter;
	}

	public BounceListViewForTab1(Context context, AttributeSet attrs, int defStyle){  
        super(context, attrs, defStyle);  
        mContext = context;  
        initBounceListView();  
        setOnScrollListener(new OnScrollListener() {
		    @Override
		    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		    	((FlowListAdapter)getAdapter()).setScroll(true);
		    }

		    @Override
		    public void onScrollStateChanged(AbsListView view, int scrollState) {
		    	if(OnScrollListener.SCROLL_STATE_IDLE == scrollState){
		    		((FlowListAdapter)getAdapter()).setScroll(false);
		    	}
		    }
		});
    }  
       
    private void initBounceListView(){  
        //get the density of the screen and do some maths with it on the max overscroll distance  
        //variable so that you get similar behaviors no matter what the screen size  
           
        final DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();  
        final float density = metrics.density;  
           
        mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);  
    }  
       
    @Override  
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent){   
        //This is where the magic happens, we have replaced the incoming maxOverScrollY with our own custom variable mMaxYOverscrollDistance;   
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxYOverscrollDistance, isTouchEvent);    
    }  
       
    @Override   
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	Log.i("bbb","BounceListView:InterceptTouchEvent");
    	super.onInterceptTouchEvent(ev);
		return true;   
    }
    
    //private int lastMotion = -1;
    private long lastDownTime = 0;
    private float lastDownX = 0;
    private float lastDownY = 0;
    
    @Override   
    public boolean onTouchEvent(MotionEvent ev) {
    	Log.i("bbb","BounceListView:onTouchEvent action="+ev.getAction());
    	super.onTouchEvent(ev);
    	if(MotionEvent.ACTION_MOVE==ev.getAction()){
    		Log.i(TAG,"FlowListView:scrollHeight="+Math.abs(ev.getY()-lastY));
    		if(Math.abs(ev.getY()-lastY)>400){
    			adapter.setScroll(true);
    		}else{
    			adapter.setScroll(false);
    		}
    	}else if(MotionEvent.ACTION_UP==ev.getAction()){
			if (Math.abs(lastDownY - ev.getY()) < 5 && Math.abs(lastDownX - ev.getX() )< 5
					&& System.currentTimeMillis() - lastDownTime <= 400) {
				performOnClick(ev);
			}
    	}else if(MotionEvent.ACTION_DOWN==ev.getAction()){
    		lastDownTime = System.currentTimeMillis();
    		lastDownX = ev.getX();
    		lastDownY = ev.getY();
    	}
		return false;   
    }
    
    
    private void performOnClick(MotionEvent e){
    	Log.i("bbb","BounceListView:performOnClick action="+e.getAction());
    	
    	//find which item is clicked
    	Rect viewRect = new Rect();
    	int x = (int)e.getX() - getLeft()-((LinearLayout)getParent()).getLeft();
    	int y = (int)e.getY() - getTop()-((LinearLayout)getParent()).getTop();
    	
		for(int i=0;i<getChildCount();i++){
			View child = getChildAt(i);
			int left = child.getLeft();
			int right = child.getRight();
			int top = child.getTop();
			int bottom = child.getBottom();
			viewRect.set(left, top, right, bottom);
			if(viewRect.contains(x, y)){
				//if click some button in item,return
		    	if(performOnItemClick(x, y, (ViewGroup)child))
		    		return;
		    	//else perform onItemClickListener
				if(onItemClickListener != null){
					Log.i("bbb","BounceListView:call OnItemClick action="+e.getAction());					
					onItemClickListener.onItemClick(BounceListViewForTab1.this, child,
							this.getFirstVisiblePosition() + i,
							this.getFirstVisiblePosition() + i);
				}
				break;
			}
			
		}
    	
    };
    
    private boolean performOnItemClick(int parentX, int parentY, ViewGroup item){
    	boolean result = false;
    	Rect viewRect = new Rect();
    	ViewGroup group = null;
    	View child = null;
    	int left,right,top,bottom;
    	try {
			group = (ViewGroup) item;
			result = true;
		} catch (ClassCastException cce) {
			result = false;
		}

		// find the button view
    	int x=0,y=0;
		if (result) {
			x = parentX - group.getLeft();
			y = parentY - group.getTop();
		}
		while (result) {
			result = false;
			for (int i = 0; i < group.getChildCount(); i++) {
				child = group.getChildAt(i);
				left = child.getLeft();
				right = child.getRight();
				top = child.getTop();
				bottom = child.getBottom();
				viewRect.set(left, top, right, bottom);
				if (viewRect.contains(x, y)) {
					try {
						NonInterreptImageBtn imageBtn = (NonInterreptImageBtn) child;
						result = false;
						break;
					} catch (ClassCastException cce) {
					}
					try {
						ViewGroup vg = (ViewGroup) child;
						result = true;
						group = vg;
						x -= group.getLeft();
						y -= group.getTop();
						break;
					} catch (ClassCastException cce) {

					}

				}else{
					child = null;
				}
			}
		}
		if(child==null){
			return false;
		}
		try {
			((NonInterreptImageBtn) child).performClick();
			result = true;
		} catch (ClassCastException cce) {
			result = false;
		}
			
		Log.i("bbb","BounceListView:performOnItemClick result="+result);
		return result;
    }
    
    @Override 
    public void setOnItemClickListener(OnItemClickListener listener){
    	this.onItemClickListener = listener;
    }
    
	
}  
