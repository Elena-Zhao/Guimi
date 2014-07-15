package com.guimi;

import com.guimi.sqlite.GuiMiDB;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class Tab3Fragment extends Fragment {
	private ImageButton matchesBtn;
	private ImageButton starsBtn;
	private ImageButton publicsBtn;
	private TextView matchsCount;
	private TextView favoritesCount;
	private TextView publishCount;
	
	private GuiMiDB dbOperation;
	
	@Override 
	public View onCreateView(LayoutInflater inflater,  
			ViewGroup container, Bundle savedInstanceState) {  
			View view;
//		if(!PersonInfo.checkLogin()){
//			view = inflater.inflate(R.layout.ask_login_page, container, false);
//			view.findViewById(R.id.login_btn).setOnClickListener(new OnClickListener(){
//
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					Intent intent = new Intent(Tab3Fragment.this.getActivity(),LoginActivity.class);
//					startActivity(intent);
//					//Tab3Fragment.this.getActivity().finish();
//				}
//				
//			});
//			view.findViewById(R.id.signup_btn).setOnClickListener(new OnClickListener(){
//
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					Intent intent = new Intent(Tab3Fragment.this.getActivity(),SignupActivity.class);
//					startActivity(intent);
//					//Tab3Fragment.this.getActivity().finish();
//				}
//				
//			});
//			return view;
//		}
		dbOperation = GuiMiDB.getInstance(getActivity());
		
		view = inflater.inflate(R.layout.tab3_fragment, container,false);
		matchesBtn =(ImageButton)view.findViewById(R.id.my_matches_btn);
		starsBtn =(ImageButton)view.findViewById(R.id.my_stars_btn);
		publicsBtn =(ImageButton)view.findViewById(R.id.my_publics_btn);
		matchsCount =(TextView)view.findViewById(R.id.my_outfit_count);
		matchsCount.setText(dbOperation.getMyOutfitCount()+"");
		favoritesCount =(TextView)view.findViewById(R.id.my_favorite_count);
		favoritesCount.setText(dbOperation.getMyFavoriteCount()+"");
		publishCount =(TextView)view.findViewById(R.id.my_publish_count);
		publishCount.setText(dbOperation.getMyPublishCount()+"");		
		
		matchesBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Tab3Fragment.this.getActivity() ,MyMatchesActivity.class);
				startActivityForResult(intent,1);
			}
			
		});
		
		starsBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Tab3Fragment.this.getActivity() ,MyCollectActivity.class);
				startActivityForResult(intent,2);
			}
			
		});

		publicsBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Tab3Fragment.this.getActivity() ,MyPublishActivity.class);
				startActivityForResult(intent,3);
			}
			
		});
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		matchsCount.setText(dbOperation.getMyOutfitCount()+"");
		favoritesCount.setText(dbOperation.getMyFavoriteCount()+"");
		publishCount.setText(dbOperation.getMyPublishCount()+"");		
	}
	
	public void notifyTabChanged (){
		// TODO Auto-generated method stub
		super.onResume();
		matchsCount.setText(dbOperation.getMyOutfitCount()+"");
		favoritesCount.setText(dbOperation.getMyFavoriteCount()+"");
		publishCount.setText(dbOperation.getMyPublishCount()+"");		
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			matchsCount.setText(dbOperation.getMyOutfitCount() + "");
			break;
		case 2:
			favoritesCount.setText(dbOperation.getMyFavoriteCount() + "");
			break;
		case 3:
			publishCount.setText(dbOperation.getMyPublishCount() + "");
			break;

		default:
			break;
		}

	}

}
