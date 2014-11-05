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
	//ѡȡ��ť
	private Button select;
	//������λ��ť
	private Button publishSpace;
	//��λ����С����γ��
	private TextView pos;
	//��λ��
	private EditText num;
	//��λÿСʱ���
	private EditText price;
	//��λÿСʱΥԼ�۸�
	private EditText fine;
	//��λ������ʼʱ��
	private EditText start;
	//��λ���Ž���ʱ��
	private EditText end;
	//��λ����С�����
	private int index;
	private CanvasTransformer mTransformer;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//ȥ������
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_parkingpublish);
		setTitle("��λ����");
		
		initAnimation();
		
		initSlidingMenu();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		//���ʵ������
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
		
		//ѡȡ��λλ�ü����¼�
		select.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ParkingPublish.this, BaiduMapForLatLng.class);
				startActivity(intent);
			}
		});
		
		//������λ
		publishSpace.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isValid())
					return;
				else{
//					PublishSpaceClass pub = new PublishSpaceClass();
//					new Thread(pub, "������λ�߳�").start();
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
								Toast.makeText(ParkingPublish.this,"������λ�ɹ�", Toast.LENGTH_SHORT).show();
								//��ȡ�����ѷ�����λ��Ϣ
								GetPublicSpaceData.getSpaceInfo(OwnerInfo.getId());
					        } else {
					        	Toast.makeText(ParkingPublish.this,"������λʧ��", Toast.LENGTH_SHORT).show();
						}
					}
		        });
				}
			}
		});
		
		//��λ����
//		manage.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				GetPublicSpaceData.getSpaceInfo(OwnerInfo.getId());
//			}
//		});
		
	}
	
	/**
	 * ��ʼ�������˵�
	 */
	private void initSlidingMenu(){
		// ������������ͼ
		//getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new SampleListFragment()).commit();
				
		// ���û����˵���ͼ
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new SampleListFragment()).commit();

		// ���û����˵�������ֵ
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
	 * ��ʼ������Ч��
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
	
	
	//�ж����������Ƿ�Ϸ�
	private boolean isValid(){
		if(num.getText().toString().trim().equals("") || Integer.parseInt(num.getText().toString()) == 0)
		{
			num.setError(Html.fromHtml("<font color=#808183>"
                    + "��λ�Ų�����Ҫ��"+ "</font>"));
			return false;
		}				
		else if(price.getText().toString().trim().equals("") || Integer.parseInt(price.getText().toString()) <= 0)
		{
			price.setError(Html.fromHtml("<font color=#808183>"
                    + "�۸񲻷���Ҫ��"+ "</font>"));
			return false;
		}
		else if(fine.getText().toString().trim().equals("") || Integer.parseInt(fine.getText().toString()) <= 0)
		{
			fine.setError(Html.fromHtml("<font color=#808183>"
                    + "�۸񲻷���Ҫ��"+ "</font>"));
			return false;
		}
		else if(start.getText().toString().trim().equals("") || Integer.parseInt(start.getText().toString()) >= 24)
		{
			start.setError(Html.fromHtml("<font color=#808183>"
                    + "��ʼʱ�䲻����Ҫ��"+ "</font>"));
			return false;
		}
		else if(end.getText().toString().trim().equals("") || Integer.parseInt(end.getText().toString()) > 24)
		{
			end.setError(Html.fromHtml("<font color=#808183>"
                    + "����ʱ�䲻����Ҫ��"+ "</font>"));
			return false;
		}
		else if(Integer.parseInt(start.getText().toString()) >= Integer.parseInt(end.getText().toString()))
		{
			start.setError(Html.fromHtml("<font color=#808183>"
                    + "��ʼʱ�䲻�ܴ��ڽ���ʱ��"+ "</font>"));
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

