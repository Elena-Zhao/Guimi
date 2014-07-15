package com.guimi;

import com.guimi.myviews.TagGroup;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.LinearLayout;

public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		
		LinearLayout selectTagPanel = (LinearLayout)findViewById(R.id.select_tag_panel);
		String[] color = {"红色","蓝色","绿色","黄色","黑色","白色","卡其色","粉色"};
//		selectTagPanel.addView(new TagGroup(this,"颜色1",color));
//		selectTagPanel.addView(new TagGroup(this,"颜色2",color));
//		selectTagPanel.addView(new TagGroup(this,"颜色3",color));
//		selectTagPanel.addView(new TagGroup(this,"颜色4",color));
//		selectTagPanel.addView(new TagGroup(this,"颜色5",color));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}

}
