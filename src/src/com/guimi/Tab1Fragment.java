package com.guimi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Vector;

import org.kobjects.base64.Base64;

import com.guimi.entities.Match;
import com.guimi.entities.Outfit;
import com.guimi.myadapters.MatchListItemAdapter;
import com.guimi.sqlite.GuiMiDB;
import com.guimi.util.DensityUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

public class Tab1Fragment extends Fragment {
	private final String TAG = "com.guimi.Tab1Fragment";
	private final int PAGE_SIZE = 20;

	// which page to loading
	private enum LoadType {
		REFRESH, // refresh when pull down
		NEXT_PAGE, // load next page when pull up
		FIRST_PAGE// load first when create fragment
	};

	// the left and right ListView
	private ListView list1;
	private ListView list2;

	// adapters
	private MatchListItemAdapter adapter1;
	private MatchListItemAdapter adapter2;

	//
	private int myScrollY = 0;

	// processBar
	private LinearLayout loadBar;

	// hander to loader page
	private Handler UIhandler;

	// the width of each ListView
	private float listWidth = 0;

	// is loading page
	private boolean isLoading = false;

	// DB instanse
	private GuiMiDB dbOperator;

	// store a page for handle
	private Vector<Match> page;

	// store first page of matchs
	private Vector<Match> firstPage;

	// true if no more page
	private boolean noMorePage;

	// first item id and last item id
	private int firstMatchId = 0;
	private int lastMatchId = 0;

	// scroll to update
	private LinearLayout updateStateView;
	private TextView updateStateText;
	private boolean isFirstRow = false;
	private final int UPDATE_MIN_SCROLL = 350;
	private int UPDATE_VIEW_HEIGHT;
	private View fragmentcontainer;
	

	private int lastOpenedItemPos = -1;
	private int lastOpenedItenList = -1;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab1_fragment, container, false);
		
		dbOperator = GuiMiDB.getInstance(getActivity());
		page = new Vector<Match>();
		loadBar = (LinearLayout) view.findViewById(R.id.load_status);
		// loadBar.setVisibility(View.VISIBLE);
		
		UPDATE_VIEW_HEIGHT = DensityUtil.dip2px(getActivity(), 50);
		updateStateView = (LinearLayout)view.findViewById(R.id.update_state_view);
		updateStateText = (TextView)view.findViewById(R.id.update_state_text);
		
		fragmentcontainer = view.findViewById(R.id.dlist_container);
		
		fragmentcontainer.setOnTouchListener(
				new OnTouchListener() {
			
			float y = 0;
			float updateScrollY = 0;
			float updateViewY = 0;
			boolean isupdating = false;
			boolean isTouching = false;
			@Override
					public boolean onTouch(View view, MotionEvent m) {
						Log.i(TAG,
								"LinearLayout:onTouch action=" + m.getAction()
										+ "  isscroll:" + adapter1.isScroll());
						if(isLoading)
							return true;
						
						
						if (MotionEvent.ACTION_DOWN == m.getAction()) {
							updateScrollY = m.getY();
							y = m.getY();
							adapter1.setScroll(true);
							adapter2.setScroll(true);
							isTouching = true;
						} else if (MotionEvent.ACTION_UP == m.getAction()) {
							adapter1.setScroll(false);
							adapter2.setScroll(false);
							isTouching = false;
						}
						if (isFirstRow) {
							
							if (MotionEvent.ACTION_UP == m.getAction()) {
								isupdating = false;
								updateScrollY -= m.getY();
								Log.i("db", "" + updateScrollY);
								if ((-updateScrollY) >= UPDATE_MIN_SCROLL) {
									isLoading = true;
									
									TranslateAnimation tAnimi = new TranslateAnimation(
											0, 0, updateViewY, UPDATE_VIEW_HEIGHT);
									tAnimi.setAnimationListener(new AnimationListener() {
										
										@Override
										public void onAnimationStart(Animation arg0) {
											// TODO Auto-generated method stub
											
										}
										
										@Override
										public void onAnimationRepeat(Animation arg0) {
											// TODO Auto-generated method stub
											
										}
										
										@Override
										public void onAnimationEnd(Animation arg0) {
											// TODO Auto-generated method stub
											updateStateText.setText("正在刷新,请稍候...");
											new LoadNewPageTask().execute(LoadType.REFRESH);
										}
									});
									tAnimi.setFillAfter(true);
									tAnimi.setDuration(400);
									//updateStateView.startAnimation(tAnimi);
									fragmentcontainer.startAnimation(tAnimi);
									
								}else{
									TranslateAnimation tAnimi = new TranslateAnimation(
											0, 0, updateViewY,0);
									tAnimi.setFillAfter(true);
									//updateStateView.startAnimation(tAnimi);
									fragmentcontainer.startAnimation(tAnimi);
									updateStateText.setText("下拉刷新");
								}
								isFirstRow = false;
								updateViewY = 0;
							} else if (MotionEvent.ACTION_MOVE == m.getAction()) {
								float scroll = m.getY() - y;
								y = m.getY();
								float updateViewY1 = updateViewY + scroll / 3;
								if (updateViewY1 > UPDATE_VIEW_HEIGHT) {
									TranslateAnimation tAnimi = new TranslateAnimation(
											0, 0, updateViewY, updateViewY1);
									tAnimi.setFillAfter(true);
									//updateStateView.startAnimation(tAnimi);
									fragmentcontainer.startAnimation(tAnimi);
								}
								if (m.getY() - updateScrollY < UPDATE_MIN_SCROLL) {
									updateStateText.setText("下拉刷新");
								} else {
									updateStateText.setText("松开刷新");
									isupdating = true;
								}
								updateViewY = updateViewY1;
							}
						}
						if(!isupdating){
							list1.dispatchTouchEvent(m);
							list2.dispatchTouchEvent(m);
						}
						return true;
					}
				});
		
		
		
		
		
		
		
		list1 = (ListView) view.findViewById(R.id.list1);
		list2 = (ListView) view.findViewById(R.id.list2);

		adapter1 = new MatchListItemAdapter(this.getActivity(), 0, list1);

		adapter2 = new MatchListItemAdapter(this.getActivity(), 1, list2);

		list1.setAdapter(adapter1);
		list1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long id) {
				// TODO Auto-generated method stub
				lastOpenedItemPos =pos;
				lastOpenedItenList = 1;
				Intent intent = new Intent(getActivity(),
						MatchInfoActivity.class);
				intent.putExtra("item", adapter1.matches.get(pos));
				startActivity(intent);
			}

		});
		list2.setAdapter(adapter2);
		list2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long id) {
				// TODO Auto-generated method stub
				lastOpenedItemPos =pos;
				lastOpenedItenList = 2;
				Intent intent = new Intent(getActivity(),
						MatchInfoActivity.class);
				intent.putExtra("item", adapter2.matches.get(pos));
				startActivity(intent);
			}

		});

		listWidth = adapter1.listWidth;

		

		// 为list1、list2设置滑动时执行动作-loadNewPage
		list1.setOnScrollListener(new OnScrollListener() {
			boolean isLastRow = false;

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// 滚动时一直回调，直到停止滚动时才停止回调。单击时回调一次。
				// firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
				// visibleItemCount：当前能看见的列表项个数（小半个也算）
				// totalItemCount：列表项共数

				// 判断是否滚到最后一行
				if (firstVisibleItem + visibleItemCount == totalItemCount
						&& totalItemCount > 0) {
					isLastRow = true;
				}
				if (firstVisibleItem == 0) {
					try {
						if (list1.getChildAt(0).getTop() == 0) {
							isFirstRow = true;
						}
					} catch (NullPointerException e) {
						// TODO: handle exception
					}

				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

				if (isLastRow
						&& !isLoading
						&& scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
					isLastRow = false;
					if (!noMorePage) {
						isLoading = true;
						loadBar.setVisibility(View.VISIBLE);

						new LoadNewPageTask().execute(LoadType.NEXT_PAGE);
					} else {
						Toast.makeText(getActivity(), "已经没有更早的内容了！",
								Toast.LENGTH_SHORT).show();
					}
				}
			}

		});

		// because the following constructions spend too much time,
		// the UI keep nothing for several seconds
		// to avoid this, I use another thread to run it here:
		UIhandler = new Handler() {
			public void handleMessage(Message msg) {
				Log.i("aaa", "start handle message ");
				super.handleMessage(msg);
				PageData data = (PageData) msg.obj;
				if (data.type == 0) {
					adapter1.addItems(data.getFirst());
					adapter2.addItems(data.getSecond());
				}else{
					adapter1.insertItemsAtFirst(data.getFirst());
					adapter2.insertItemsAtFirst(data.getSecond());
				}

				float height = data.getPlaceHolderViewHeight();
				if (height > 0) {
					adapter2.setPlaceHolderView(height);
				} else if (data.getPlaceHolderViewHeight() < 0) {
					adapter1.setPlaceHolderView(-height);
				}
				Log.i("aaa", "get state: " + myScrollY);
				loadBar.setVisibility(View.GONE);
				isLoading = false;
			}
		};

		// Begin loading match items
		loadBar.setVisibility(View.VISIBLE);
		new LoadNewPageTask().execute(LoadType.FIRST_PAGE);
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.i("aaa", "onResume ");
		super.onResume();
		if(lastOpenedItemPos!=-1){
			
			
			new AsyncTask<Integer, Void, Outfit>(){
				int whichList =1;
				int pos = -1;
				Match match;
				@Override
				protected Outfit doInBackground(Integer... arg0) {
					// TODO Auto-generated method stub
					whichList = arg0[0];
					pos = arg0[1];
					if(whichList==1){
						match = adapter1.matches.get(pos);
					}else{
						match = adapter2.matches.get(pos);
					}try{
					return dbOperator.getOutfit(match.getMatchId());
					}catch(Exception e){
						
					}
					return null;
				}
				@Override
				protected void onPostExecute(Outfit outfit) {
					if (outfit == null) {
						return;
					}
					match.setLikeNum(outfit.getLikes());
					match.setMyLike(outfit.isLiked());
					match.setDislikeNum(outfit.getDislikes());
					match.setMyDislike(outfit.isDisliked());
					match.setMyFavorite(outfit.isFavorited());
					if(whichList==1){
						adapter1.notifyDataSetChanged();
					}else{
						adapter2.notifyDataSetChanged();
					}
				};
				
			}.execute(lastOpenedItenList,lastOpenedItemPos);
			lastOpenedItenList =-1;
			lastOpenedItemPos =-1;
		}
		adapter1.setScroll(false);
		adapter2.setScroll(false);
	}

	// read match items from database
	// private void testInitPage(Vector<Match> page) {
	// //
	// for (int i = 0; i < 20; i++) {
	// page.add(new Match(111,"http://expert.csdn.net/images/csdn.gif",
	// (float) (Math.random() * 600 + 100),
	// (float) (Math.random() * 600 + 200), "name", "describe", 1,
	// 1, 3));
	// }
	// new GetMatches().execute();
	// for(int i = 0; i<allMatchs.size();i++){
	// page.add(allMatchs.get(i));
	// }
	// }

	// dispatch items to list
	private void dispatchItems(Vector<Match> page, int type) {
		PageData data = new PageData(type);
		float increasement = 0;
		float height1 = adapter1.getHeight();
		float height2 = adapter2.getHeight();
		Match item;
		for (int i = 0; i < page.size(); i++) {
			item = page.get(i);
			increasement = item.getImageHeight() * listWidth
					/ item.getImageWidth()
					+ DensityUtil.dip2px(getActivity(), 20);
			Log.i(TAG, "heigth1:" + height1 + "     height2:" + height2
					+ "increasement:" + increasement);
			if (height1 > height2) {
				data.putInSecond(item);
				height2 += increasement;
			} else {
				data.putInFirst(item);
				height1 += increasement;
			}
		}
		Message msg = UIhandler.obtainMessage();
		data.setPlaceHolderViewHeight(height1 - height2);
		msg.obj = data;
		UIhandler.sendMessage(msg);
	}

	private class PageData {
		private Vector<Match> left;
		private Vector<Match> right;
		private float placeHolderViewHeight = 0;
		public int type;

		private void setPlaceHolderViewHeight(float height) {
			placeHolderViewHeight = height;
		}

		private float getPlaceHolderViewHeight() {
			return placeHolderViewHeight;
		}

		private PageData(int type) {
			left = new Vector<Match>();
			right = new Vector<Match>();
			this.type = type;
		}

		public void putInFirst(Match item) {
			left.add(item);
		}

		public void putInSecond(Match item) {
			right.add(item);
		}

		public Vector<Match> getFirst() {
			return left;
		}

		public Vector<Match> getSecond() {
			return right;
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("db", "destory");
		saveMatchs(firstPage);
	}

	class LoadNewPageTask extends
			AsyncTask<LoadType, java.lang.Void, List<Match>> {
		private int type = -1;
		@Override
		protected List<Match> doInBackground(LoadType... params) {
			List<Match> result = null;
			try {
				isLoading = true;
				Log.e("succeed!!! ", "begin getting matches");
				/*
				 * getMatchs(int orderType, int flagOutfitId, boolean
				 * beforeOrAfter) orderType，type=0是按时间排序，type = 1是按点赞数排序。
				 * beforeOrAfter是加载下一页还是下拉刷新。加载下一页是true，
				 * 这时候flagOutfitId是已加载的最后一个Match的matchId。 下拉刷新是false，
				 * 这时候flagOutfitId是已加载的第一个Match的MatchId
				 */
				if (0 == params[0].compareTo(LoadType.NEXT_PAGE)) {
					result = dbOperator.getMatchs(0, lastMatchId, true);
					if (result != null && result.size() != 0) {
						lastMatchId = result.get(result.size() - 1)
								.getMatchId();
					}
					type = 0;
				} else if (0 == params[0].compareTo(LoadType.REFRESH)) {
					result = dbOperator.getMatchs(0, firstMatchId, false);
					if (result != null && result.size() != 0){
						for (int i = 0; i < result.size(); i++) {
							firstPage.insertElementAt(result.get(i), i);
							if (firstPage.size() > 20)
								firstPage.remove(20);
						}
						firstMatchId = result.get(0).getMatchId();
					}
					Log.i("db", firstPage.toString());
					type = 1;
				} else {
//					SharedPreferences readedMatchs = getActivity()
//							.getSharedPreferences("readedMatchs",
//									Context.MODE_PRIVATE);
//					if (readedMatchs.getString("match1", null) == null)
						result = dbOperator.getMatchs(0, 0, true);
//					else
//						result = getMatchs();
					if (result != null && result.size() != 0) {
						firstPage = new Vector<Match>(result);
						firstMatchId = result.get(0).getMatchId();
						lastMatchId = result.get(result.size()-1).getMatchId();
						Log.i("db",firstPage.toString());
					}
				}

			} catch (Exception e) {
				Log.i("db", e.toString());
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<Match> result) {
			page = new Vector<Match>();
			if (type == 1) {
				if(result==null){
					updateStateText.setText("刷新失败，请检查网络连接！");
					Log.i("aaa", "onPostExecute type = "+type);
				}else if(result.size() == 0){
					updateStateText.setText("没有更多的内容了！");
				}else{
					updateStateText.setText("共有"+result.size()+"条更新");
					for (int i = 0; i < result.size(); i++) {
						page.add(result.get(i));
					}
					dispatchItems(page,1);
				}
				
				UIhandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TranslateAnimation tAnimi = new TranslateAnimation(0,
								0, UPDATE_VIEW_HEIGHT, 0);
						tAnimi.setFillAfter(true);
						tAnimi.setDuration(500);
						tAnimi.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation arg0) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onAnimationRepeat(Animation arg0) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onAnimationEnd(Animation arg0) {
								// TODO Auto-generated method stub
								isLoading = false;
							}
						});
						//updateStateView.startAnimation(tAnimi);
						fragmentcontainer.startAnimation(tAnimi);
					}
				}, 2000);
			} else {
				if (result == null) {
					
						Toast.makeText(getActivity(), "请求失败，请检查网络连接",
								Toast.LENGTH_SHORT).show();

				} else if (result.size() == 0) {
					
						// loadBar.setVisibility(View.GONE);
						noMorePage = true;
						 Toast.makeText(getActivity(), "已经没有更早的内容了！",
						 Toast.LENGTH_SHORT).show();
					

				} else {
					
						if (result.size() < PAGE_SIZE) {
							noMorePage = true;
						}
						for (int i = 0; i < result.size(); i++) {
							page.add(result.get(i));
							Log.i("bbb", ""+result.get(i).getMatchId());
						}
						dispatchItems(page,0);
					
				}
				loadBar.setVisibility(View.GONE);
			}
		}
	}

	public void saveMatchs(List<Match> matchs) {
		Log.i("db", "startSave");
		SharedPreferences readedMatchs = getActivity().getSharedPreferences(
				"readedMatchs", Context.MODE_PRIVATE);
		Editor editor = readedMatchs.edit();
		// 创建字节输出流
		try {
			// 创建对象输出流，并封装字节流
			//Log.i("db", matchs.size() + "");
			for (int i = 0; i < matchs.size() && i < 20; i++) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				// 将对象写入字节流
				oos.writeObject(matchs.get(i));
				// 将字节流编码成base64的字符窜
				String match_Base64 = new String(Base64.encode(baos
						.toByteArray()));
				editor.putString("match" + i, match_Base64);
			}
			editor.commit();
		} catch (IOException e) {
			// TODO Auto-generated
			Log.i("db", e.toString());
		}
	}

	public Vector<Match> getMatchs() {
		Vector<Match> result = new Vector<Match>();
		SharedPreferences readedMatchs = getActivity().getSharedPreferences(
				"readedMatchs", Context.MODE_PRIVATE);
		Editor editor = readedMatchs.edit();
		String match_Base64;
		for (int i = 0; i < 20; i++) {
			match_Base64 = readedMatchs.getString("match" + i, null);
			editor.remove("match"+i);
			if (match_Base64 == null)
				break;
			else {
				byte[] base64 = Base64.decode(match_Base64);
				// 封装到字节流
				ByteArrayInputStream bais = new ByteArrayInputStream(base64);
				try {
					// 再次封装
					ObjectInputStream bis = new ObjectInputStream(bais);
					Match match = (Match) bis.readObject();
					result.add(match);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.i("db", e.toString());
				}
			}
		}
		editor.commit();
		return result;
	}

}
