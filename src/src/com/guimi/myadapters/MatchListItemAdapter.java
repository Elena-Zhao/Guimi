package com.guimi.myadapters;

import java.util.Vector;

import com.guimi.MainActivity;
import com.guimi.R;
import com.guimi.entities.Match;
import com.guimi.entities.PersonInfo;
import com.guimi.sqlite.GuiMiDB;
import com.guimi.sqlite.ImageHandeller;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MatchListItemAdapter extends FlowListAdapter {
	private final String TAG = "com.guimi.myadapters.MatchListItemAdapter";
	public Vector<Match> matches;
	private int colNum;
	private ListView listView;
	public GuiMiDB dbOperator;
	public ImageHandeller imageHandeller;

	public MatchListItemAdapter(Context context, int col, ListView list) {
		super(context);
		this.listView = list;
		colNum = col;
		matches = new Vector<Match>();
		setupFlowListAdapter(context, 0, new Vector<String>());
		dbOperator = GuiMiDB.getInstance(context);
		imageHandeller = ImageHandeller.getInstance(context);
	}

	public void addItems(Vector<Match> items) {
		for (int i = 0; i < items.size(); i++) {
			addItem(items.get(i));
		}
		super.notifyDataSetChanged();
	}

	public void addItem(Match item) {
		super.addItem(item.getImageUrl(), item.getImageWidth(),
				item.getImageHeight());
		this.matches.add(item);
	}
	
	public void insertItemsAtFirst(Vector<Match> items) {
		for (int i = items.size()-1; i >=0; i--) {
			insertItemAtFirst(items.get(i));
		}
		super.notifyDataSetChanged();
	}

	public void insertItemAtFirst(Match item) {
		super.insertItemAtFirst(item.getImageUrl(), item.getImageWidth(),
				item.getImageHeight());
		this.matches.insertElementAt(item,0);
	}

	public class OtherViewHolder {
		public TextView name;
		public ImageView image;
		public ImageButton addBtn;
		public ImageButton upBtn;
		public ImageButton downBtn;
		public TextView addNum;
		public TextView upNum;
		public TextView downNum;
	}

	@Override
	public View getView(final int pos, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(pos == matches.size()){
			return getPlaceHolderView();
		}
		Log.i(TAG, "list" + (colNum + 1) + "  getView at " + pos + " isScroll:"
				+ isScroll);

		//get the holder
		OtherViewHolder holder = null;
		if (convertView == null||convertView.equals(getPlaceHolderView())) {
			holder = new OtherViewHolder();
			convertView = mInflater.inflate(R.layout.match_item, null);
			//Initialize all elements in the sector
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.name = (TextView) convertView.findViewById(R.id.user_name);
			holder.addBtn = (ImageButton) convertView
					.findViewById(R.id.add_image);
			holder.upBtn = (ImageButton) convertView
					.findViewById(R.id.up_image);
			holder.downBtn = (ImageButton) convertView
					.findViewById(R.id.down_image);
			holder.addNum = (TextView) convertView.findViewById(R.id.add_num);
			holder.upNum = (TextView) convertView.findViewById(R.id.up_num);
			holder.downNum = (TextView) convertView.findViewById(R.id.down_num);
			
			//Set tag. Usage: ?
			convertView.setTag(holder);

		} else {
			holder = (OtherViewHolder) convertView.getTag();
		}
		
		
		//Set elements display
		setUpView(holder.upBtn, holder.downBtn, holder.addBtn,
				holder.upNum, holder.downNum, holder.name, pos);
		//Set action Listeners for "Praise" "Despise" and "My Collection"
		if (PersonInfo.userName != null) {
			holder.upBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (matches.get(pos).isMyLike() == false) {
						new HandleMatch("upImage" + pos, "upNum" + pos)
								.execute(0);
					} else {
						new HandleMatch("upImage" + pos, "upNum" + pos)
								.execute(1);
					}
				}
			});
			holder.downBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					if (matches.get(pos).isMyDislike() == false) {
						new HandleMatch("downImage" + pos, "downNum" + pos)
								.execute(2);
					} else {
						new HandleMatch("downImage" + pos, "downNum" + pos)
								.execute(3);
					}
				}
			});

			holder.addBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					if (matches.get(pos).isMyFavorite() == false) {
						new HandleMatch("addImage" + pos, "addNum" + pos)
								.execute(4);
					} else {
						new HandleMatch("addImage" + pos, "addNum" + pos)
								.execute(5);
					}
				}
			});

		}else{
			OnClickListener notLoginListener = new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(mInflater.getContext(), "你还没有登陆,不能进行该操作！",
							Toast.LENGTH_SHORT).show();
				}
			};
			holder.upBtn.setOnClickListener(notLoginListener);
			holder.downBtn.setOnClickListener(notLoginListener);
			holder.addBtn.setOnClickListener(notLoginListener);
		}

		// return a null image view when scroll
//		if (isScroll) {
//			holder.image.setImageResource(android.R.color.transparent);
//			super.setImageHeight(pos, convertView);
//			return convertView;
//		} else {

			// Set the image tag
			String imageUrl = matches.get(pos).getImageUrl();
			ImageView imageView = holder.image;
			imageView.setTag(pos);

			// load image, if not exist in local, show a default picture
			imageView.setImageResource(android.R.color.transparent);
			new GetMatchImage(pos).execute(imageUrl);
		//}
		
		
		//set the height of the view
		super.setImageHeight(pos, convertView);
		return convertView;
	}

	
	private void setUpView(ImageButton upButton, ImageButton downButton,
			ImageButton addButton, TextView upText, TextView downText,
			TextView name, final int pos) {
		Match match = matches.get(pos);
		if(match.isMyLike()){
			upButton.setImageResource(R.drawable.up_true);
		}else {
			upButton.setImageResource(R.drawable.up_false);
		}
		
		if(match.isMyDislike()){
			downButton.setImageResource(R.drawable.down_true);
		}else {
			downButton.setImageResource(R.drawable.down_false);
		}
		
		if(match.isMyFavorite()){
			addButton.setImageResource(R.drawable.add_true);
		}else {
			addButton.setImageResource(R.drawable.add_false);
		}
		
		
		upText.setText(String.valueOf(match.getLikeNum()));
		downText.setText(String.valueOf(match.getDislikeNum()));
		
		name.setText(match.getUserName());

		
		upButton.setTag("upImage" + pos);
		upText.setTag("upNum" + pos);
		
		downButton.setTag("downImage" + pos);
		downText.setTag("downNum" + pos);
		
		addButton.setTag("addImage"+pos);
		
	}
	
	

	//HandleMatch.execute(int type)
	//param type:
	//0 like
	//1 cancel like
	//2 dislike
	//3 cancel dislike
	//4 collect
	//5 cancel collect
	class HandleMatch extends AsyncTask <Integer,Void,Integer>{
		private int type;
		private int pos;
		private String imageTag;
		private String textTag;
		
		public HandleMatch(String image, String text){
			this.imageTag = image;
			this.textTag = text;
			pos = Integer.parseInt(imageTag.substring(imageTag.indexOf('e')+1));
		}
		@Override
		protected Integer doInBackground(Integer... params) {
			type = params[0];
			int result = Integer.MIN_VALUE;
			int id = matches.get(pos).getMatchId();
			String outFitId = matches.get(pos).getUserId();
			try {
				switch (type) {
				case 0:
					result = dbOperator.like(id,outFitId);
					break;
				case 1:
					result = dbOperator.cancelLike(id);
					break;
				case 2:
					result = dbOperator.dislike(id,outFitId);
					break;
				case 3:
					result = dbOperator.cancelDislike(id);
					break;
				case 4:
					result = dbOperator.addToFavorite(id,matches.get(pos).getUserId());
					break;
				case 5:
					result = dbOperator.cancelFavorite(id);
					break;
				default:
					result = -1;
					break;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			//成功
			if (result == 0) {
				TextView num = null;
				
					if (type < 4) {
						num = (TextView) listView.findViewWithTag(textTag);
					}
					ImageButton image = (ImageButton) listView
							.findViewWithTag(imageTag);
				
				try {
					Match match = matches.get(pos);
					switch (type) {
					case 0:
						match.setLikeNum(match.getLikeNum()+1);
						match.setMyLike(true);
						//num.setText(num.getText().toString() + 1);
						break;
					case 1:
						match.setLikeNum(match.getLikeNum()-1);
						match.setMyLike(false);
						//num.setText(num.getText().toString() +(-1));
						break;
					case 2:
						match.setDislikeNum(match.getDislikeNum()+1);
						match.setMyDislike(true);
						break;
					case 3:
						match.setDislikeNum(match.getDislikeNum()-1);
						match.setMyDislike(false);
						break;
					case 4:
						match.setMyFavorite(true);
						result = 0;
						break;
					case 5:
						match.setMyFavorite(false);
						result = 0;
						break;
					default:
						result = -1;
						break;
					}
				} catch (NullPointerException e) {
					// TODO: handle exception
				}
				notifyDataSetChanged();
			} else if(result == 2){
				Toast.makeText(mInflater.getContext(), "不能对自己的发布进行该操作！",Toast.LENGTH_SHORT).show();
			}
			
			//失败
			else {
				Toast.makeText(mInflater.getContext(), "网络连接超时，操作失败！",Toast.LENGTH_SHORT).show();
			}
		}
	}
	class GetMatchImage extends AsyncTask<String,Void,Bitmap> {
		private int tag;
		public GetMatchImage(int tag){
			this.tag = tag;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			
			Bitmap bmp = null;
			try {
				
				//Result = imageHandeller.getBitmap(matchImageUrl);
				//if(Result == null){
				bmp = imageHandeller.download(params[0]);
				//}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bmp;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if ( result != null) {
				
				ImageView imageViewByTag = (ImageView) listView
						.findViewWithTag(tag);
				Log.i("image", "call back set image");
				if (imageViewByTag != null) {
					Log.i("url", "set image at:"+tag);
					imageViewByTag.setImageBitmap(result);
				}
			}
            //matchImaBitmap = Result;
		}
	}

}
