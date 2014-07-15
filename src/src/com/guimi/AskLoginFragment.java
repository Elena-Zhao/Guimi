package com.guimi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class AskLoginFragment extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
			View view = inflater.inflate(R.layout.ask_login_page, container, false);
			view.findViewById(R.id.login_btn).setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(AskLoginFragment.this.getActivity(),LoginActivity.class);
					startActivity(intent);
					//Tab2Fragment.this.getActivity().finish();
				}
				
			});
			view.findViewById(R.id.signup_btn).setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(AskLoginFragment.this.getActivity(),SignupActivity.class);
					startActivity(intent);
					//Tab2Fragment.this.getActivity().finish();
				}
				
			});
			return view;
		}

}
