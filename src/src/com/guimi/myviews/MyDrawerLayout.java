package com.guimi.myviews;

import com.guimi.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


/*
 * To have a good experience with this widget, you could note this:
 * ----------------------------------------------------------------------------
 * 0.When you included this class in your project, it will happens with some errors,to fix them:
 * 	change the package name into your package name
 * 	import your R file instead of the original R file
 * 	Then follow the tips below:  
 * 
 * 1.The attrs.xml must be included, you can add it to your own attrs.xml, too
 * 
 * 
 * 2.This MyDrawerlayout must be put in a FrameLayout
 * 
 * 
 * 3.Add this line in your root layout: xmlns:my_drawer="http://schemas.android.com/apk/res/[yourpakage]" 
 * 
 * 
 * 4.These attributes must be defined:(look at 3)
 * 		my_drawer:handle_id="@+id/handle" 
 *   	my_drawer:content_id="@+id/content"
 *  	my_drawer:drawer_container_id="@+id/drawer_container"
 *  And my_drawer:drawer_mask_color (Optional, transparent by default)
 *  
 *  
 * 5.The background of the context would be single color for best performance
 * 	The drawer must contains these layer:
 *		 LinearLayout container with two child:
 * 				View handle :the handle button of the drawer
 * 				View context:the content of the drawer
 * 	The order of this two child depends on your gravity
 * 
 * 
 * 6.To custom the alignment of the drawer, 
 *  	you can define the gravity and orientation of this MyDrawerLayout
 *  
 *  
 * 7.he initDrawer of this drawer must be called in your activity/fragment
 * 
 * ----------------------------------------------------------------------------
 * 
 * @copyright: Jiayue Ren 2014.3.17
*/
public class MyDrawerLayout extends FrameLayout{
	private View handle;
	private View content;
	private View mask;
	private LinearLayout container;
	
	private int handleID;
	private int contentID;
	private int containerID;
	
	private int orientation;
	private int gravity;
	
	
	private boolean isOpened;
	private boolean isAnimating = false;
	
	private Animation animClose;
	private Animation animOpen;
	

	public MyDrawerLayout(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}
	
	public MyDrawerLayout(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		// TODO Auto-generated constructor stub
	}
	
	public MyDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		orientation = attrs.getAttributeIntValue("android","orientation",LinearLayout.VERTICAL);
		gravity = attrs.getAttributeIntValue("android","gravity",Gravity.START);
		
		if(orientation == LinearLayout.VERTICAL){
			if(gravity == Gravity.START){
				animOpen = new TranslateAnimation(0,0,25,0);
				animClose = new TranslateAnimation(0,0,0,25);
			}else{
				animOpen = new TranslateAnimation(0,0,-25,0);
				animClose = new TranslateAnimation(0,0,0,-25);
			}
			
		}else{
			if(gravity == Gravity.START){
				animOpen = new TranslateAnimation(25,0,0,0);
				animClose = new TranslateAnimation(0,25,0,0);
			}else{
				animOpen = new TranslateAnimation(-25,0,0,0);
				animClose = new TranslateAnimation(0,-25,0,0);
			}
		}
		
		
		animOpen.setInterpolator(AnimationUtils.loadInterpolator(context,android.R.anim.accelerate_interpolator));
		animClose.setInterpolator(AnimationUtils.loadInterpolator(context,android.R.anim.decelerate_interpolator));
		
		animOpen.setDuration(200);
		animOpen.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				openDrawer();
				isAnimating = false;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				Log.i("aaa","repeat");
			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				isAnimating = true;
			}
			
		});
		animClose.setDuration(200);
		animClose.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				Log.i("aaa", "animate end");
				closeDrawer();
				isAnimating = false;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				isAnimating = true;
			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				Log.i("aaa", "animate start");
				
			}
			
		});
		
		

		
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.MyDrawerLayout);
		handleID = mTypedArray.getResourceId(R.styleable.MyDrawerLayout_handle_id, -1);
		contentID = mTypedArray.getResourceId(R.styleable.MyDrawerLayout_content_id, -1);
		containerID = mTypedArray.getResourceId(R.styleable.MyDrawerLayout_drawer_container_id, -1);
		
		mask = new View(this.getContext());
		mask.setBackgroundResource(mTypedArray.getResourceId(R.styleable.MyDrawerLayout_mask_color,android.R.color.transparent));
		mask.setVisibility(View.GONE);
		mask.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				animateClose();
			}
			
		});
		this.addView(mask);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		
	}
	
	public void initDrawer(){
		//container = (LinearLayout)findViewById(containerID);
		
		handle = findViewById(handleID);
		handle.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(isOpened){
					animateClose();
				}else{
					animateOpen();
				}
			}
			
		});
		content = findViewById(contentID);
		content.setVisibility(View.GONE);
		
		container = (LinearLayout) findViewById(containerID);
		LayoutParams lp = (LayoutParams)container.getLayoutParams();
		lp.setMargins(0, -25, 0, 0);
		container.setLayoutParams(lp);
		
		View view = new View(this.getContext());
		view.setBackgroundDrawable(content.getBackground());
		if(orientation == LinearLayout.VERTICAL){
			if(gravity == Gravity.START){
				view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,25));
				container.addView(view,0);
			}else{
				view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,25));
				container.addView(view);
			}
			
		}else{
			if(gravity == Gravity.START){
				view.setLayoutParams(new LayoutParams(25,LayoutParams.FILL_PARENT));
				container.addView(view,0);
			}else{
				view.setLayoutParams(new LayoutParams(25,LayoutParams.FILL_PARENT));
				container.addView(view);
			}
		}
		
		
		
		
	    
	    view.setVisibility(View.VISIBLE);
	    
		isOpened = false;
	}
	
	
	public void animateOpen(){
		if(isAnimating||isOpened){
			return;
		}else{
			content.setVisibility(View.VISIBLE);
			container.setAnimation(animOpen);
			animOpen.start();
			//container.startAnimation(animOpen);
		}
	}
	
	public void animateClose(){
		if(isAnimating||!isOpened){
			return;
		}else{
			mask.setVisibility(View.GONE);
			container.startAnimation(animClose);
		}
	}
	
	private void closeDrawer() {
		// TODO Auto-generated method stub
		content.setVisibility(View.GONE);
		mask.setVisibility(View.GONE);
		handle.setSelected(false);
		isOpened = false;
	}
	private void openDrawer() {
		// TODO Auto-generated method stub
		mask.setVisibility(View.VISIBLE);
		Log.i("aaa", "opened");
		handle.setSelected(true);
		isOpened = true;
	}

}
