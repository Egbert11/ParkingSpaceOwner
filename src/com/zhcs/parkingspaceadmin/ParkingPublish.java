package com.zhcs.parkingspaceadmin;

import java.util.ArrayList;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zhcs.ownerBean.Community;
import com.zhcs.ownerBean.CommunityInfoBean;
import com.zhcs.ownerBean.OwnerInfo;
import com.zhcs.regAndLog.R;

import android.text.Html;
import android.view.View.OnClickListener;
import android.view.MenuItem;
import android.view.View;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ParkingPublish extends SlidingFragmentActivity{
	//选取按钮
	private Button select;
	//发布车位按钮
	private Button publishSpace;
	//车位所在小区经纬度
	private TextView pos;
	//车位号
	private EditText num;
	//车位每小时租金
	private EditText price;
	//车位每小时违约价格
	private EditText fine;
	//车位开放起始时间
	private EditText start;
	//车位开放结束时间
	private EditText end;
	//车位所在小区序号
	private int index;
	private CanvasTransformer mTransformer;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//去除标题
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_parkingpublish);
		setTitle("车位发布");
		
		initAnimation();
		
		initSlidingMenu();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		//获得实例对象
		select = (Button)findViewById(R.id.select);
		publishSpace = (Button)findViewById(R.id.publish);
		pos = (TextView)findViewById(R.id.position);
		num = (EditText)findViewById(R.id.number);
		price = (EditText)findViewById(R.id.price);
		fine = (EditText)findViewById(R.id.fine);
		start = (EditText)findViewById(R.id.startTime);
		end = (EditText)findViewById(R.id.endTime);
		
		
		if(Community.getIndex() != -1) {
			index = Community.getIndex();
			pos.setText(Community.getList().get(index).getAddress());
		} 
		
		//选取车位位置监听事件
		select.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ParkingPublish.this, BaiduMapForLatLng.class);
				startActivity(intent);
			}
		});
		
		//发布车位
		publishSpace.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isValid())
					return;
				else{
//					PublishSpaceClass pub = new PublishSpaceClass();
//					new Thread(pub, "发布车位线程").start();
					ArrayList<CommunityInfoBean> list = Community.getList();
					int index = Community.getIndex();
					AVObject publish = new AVObject("SpaceInfo");
					publish.put("ownerid", OwnerInfo.getId());
					publish.put("communityid", list.get(index).getCommunityId());
					publish.put("lat", list.get(index).getLatitude());
					publish.put("lng", list.get(index).getLongitude());
					publish.put("num", Integer.parseInt(num.getText().toString()));
					publish.put("price", Integer.parseInt(price.getText().toString()));
					publish.put("fine", Integer.parseInt(fine.getText().toString()));
					publish.put("start", Integer.parseInt(start.getText().toString()));
					publish.put("end", Integer.parseInt(end.getText().toString()));
					publish.put("state", 0);
					publish.saveInBackground(new SaveCallback() {
						@Override
						public void done(AVException arg0) {
							if (arg0 == null) {
								Toast.makeText(ParkingPublish.this,"发布车位成功", Toast.LENGTH_SHORT).show();
								//获取最新已发布车位信息
								GetPublicSpaceData.getSpaceInfo(OwnerInfo.getId());
					        } else {
					        	Toast.makeText(ParkingPublish.this,"发布车位失败", Toast.LENGTH_SHORT).show();
						}
					}
		        });
				}
			}
		});
		
		//车位管理
//		manage.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				GetPublicSpaceData.getSpaceInfo(OwnerInfo.getId());
//			}
//		});
		
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
	
	
	//判断输入数据是否合法
	private boolean isValid(){
		if(num.getText().toString().trim().equals("") || Integer.parseInt(num.getText().toString()) == 0)
		{
			num.setError(Html.fromHtml("<font color=#808183>"
                    + "车位号不符合要求"+ "</font>"));
			return false;
		}				
		else if(price.getText().toString().trim().equals("") || Integer.parseInt(price.getText().toString()) <= 0)
		{
			price.setError(Html.fromHtml("<font color=#808183>"
                    + "价格不符合要求"+ "</font>"));
			return false;
		}
		else if(fine.getText().toString().trim().equals("") || Integer.parseInt(fine.getText().toString()) <= 0)
		{
			fine.setError(Html.fromHtml("<font color=#808183>"
                    + "价格不符合要求"+ "</font>"));
			return false;
		}
		else if(start.getText().toString().trim().equals("") || Integer.parseInt(start.getText().toString()) >= 24)
		{
			start.setError(Html.fromHtml("<font color=#808183>"
                    + "开始时间不符合要求"+ "</font>"));
			return false;
		}
		else if(end.getText().toString().trim().equals("") || Integer.parseInt(end.getText().toString()) > 24)
		{
			end.setError(Html.fromHtml("<font color=#808183>"
                    + "结束时间不符合要求"+ "</font>"));
			return false;
		}
		else if(Integer.parseInt(start.getText().toString()) >= Integer.parseInt(end.getText().toString()))
		{
			start.setError(Html.fromHtml("<font color=#808183>"
                    + "开始时间不能大于结束时间"+ "</font>"));
			return false;
		}
		else
		{
			return true;
		}
	}
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			moveTaskToBack(false);
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
}

