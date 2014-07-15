package com.guimi.myviews;

import com.guimi.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UploadDialog extends Dialog implements View.OnClickListener{
	private int contextViewId;
	private View.OnClickListener onclick;
	public UploadDialog(Context context, int layoutId, View.OnClickListener onclick) {
		super(context, R.style.UploadDialog);
		this.contextViewId = layoutId;
		this.onclick = onclick;
		// TODO Auto-generated constructor stub
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(contextViewId);
        View btn;
        btn = findViewById(R.id.upload_single_from_local);
		if (btn != null) {
			btn.setOnClickListener(this);
		}
		btn = findViewById(R.id.upload_single_from_camera);
		if (btn != null) {
			btn.setOnClickListener(this);
		}
		btn = findViewById(R.id.upload_single_from_chest);
		if (btn != null) {
			btn.setOnClickListener(this);
		}
		btn = findViewById(R.id.login_btn);
		if (btn != null) {
			btn.setOnClickListener(this);
		}
		btn = findViewById(R.id.signup_btn);
		if (btn != null) {
			btn.setOnClickListener(this);
		}
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		this.dismiss();
		onclick.onClick(v);
	}

}
