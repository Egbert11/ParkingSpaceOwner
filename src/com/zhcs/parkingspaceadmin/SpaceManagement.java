package com.zhcs.parkingspaceadmin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zhcs.ownerBean.OwnerInfo;
import com.zhcs.ownerBean.SpaceInfoBean;
import com.zhcs.regAndLog.R;

public class SpaceManagement extends SlidingFragmentActivity{
	
	private CanvasTransformer mTransformer;
	private ListView mListView;
	private static ArrayList<SpaceInfoBean> list = OwnerInfo.getList();
	private final static String TAG = "SpaceManagement";
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//去除标题
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_spacemanage);
		setTitle("车位管理");
		
		initAnimation();
		
		initSlidingMenu();
		// 给左上角图标的左边加上一个返回的图标 
		getActionBar().setDisplayHomeAsUpEnabled(true);
//		ActionBar actionBar = this.getActionBar();
//		actionBar.setCustomView(R.layout.custom_title);
//		actionBar.setDisplayShowCustomEnabled(true);
//		actionBar.show();

		 // 给左上角图标的左边加上一个返回的图标 
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//获取最新已发布车位信息
		GetPublicSpaceData.getSpaceInfo(OwnerInfo.getId());
		
		
		mListView = (ListView)findViewById(R.id.listView);
		
		mListView.setAdapter(new SpaceListAdapter());
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SpaceManagement.this, ModifySpace.class);
				Bundle bun = new Bundle();
				bun.putInt("index", position);
				intent.putExtras(bun);
				startActivity(intent);
				finish();
			}
		});
	}
	
	private class SpaceListAdapter extends BaseAdapter{
		public SpaceListAdapter(){
			super();
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			convertView = View.inflate(SpaceManagement.this, R.layout.spaceinfo_item, null);
			TextView title = (TextView)convertView.findViewById(R.id.title);
			TextView time = (TextView)convertView.findViewById(R.id.time);
			TextView price = (TextView)convertView.findViewById(R.id.price);
			//title.setText(String.valueOf(list.get(position).getLat()));
			int num = position + 1;
			title.setText("车位" + num);
			SimpleDateFormat df=new SimpleDateFormat("HH:mm");
			String startTime=df.format(list.get(position).getStart());
			String endTime=df.format(list.get(position).getEnd());
			time.setText("时间：" + startTime + "--" + endTime);
			price.setText("价格：" + list.get(position).getPrice() + "元/小时");
			return convertView;
		}
		
	}
	
	/**
	 * 初始化滑动菜单
	 */
	private void initSlidingMenu(){
		// 设置主界面视图
		//getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new SampleListFragment()).commit();
				
		// 设置滑动菜单视图
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new SampleListFragment()).commit();

		// 设置滑动菜单的属性值
		SlidingMenu sm = getSlidingMenu();		
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.layout.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setBehindWidth(400);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setBehindScrollScale(0.0f);
		sm.setBehindCanvasTransformer(mTransformer);
		
		setSlidingActionBarEnabled(true);
	}

	/**
	 * 初始化动画效果
	 */
	private void initAnimation(){
		mTransformer = new CanvasTransformer(){
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				canvas.scale(percentOpen, 1, 0, 0);				
			}
			
		};
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onRestart() {
		//GetPublicSpaceData.getSpaceInfo(OwnerInfo.getId(), SpaceManagement.this);
		mListView.setAdapter(new SpaceListAdapter());
		Log.e(TAG, "onRestart");
		Log.e(TAG, "size:"+list.size());
		super.onRestart();
	}
	
	/**
	 * 刷新页面
	 */
	public void refreshUI(View view){
		finish();  
        Intent intent = new Intent(SpaceManagement.this, SpaceManagement.class);  
        startActivity(intent); 
	}
//	@Override
//	protected void onResume() {
//		super.onResume();
//		GetPublicSpaceData.getSpaceInfo(OwnerInfo.getId(), SpaceManagement.this);
//		Log.e(TAG, "onResume");
//	}

}


