package com.zhcs.parkingspaceadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhcs.net.HttpUtil;
import com.zhcs.ownerBean.OwnerInfo;
import com.zhcs.ownerBean.SpaceInfoBean;
import com.zhcs.regAndLog.R;

import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.view.View;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ParkingPublish extends Activity{
	//ѡȡ��ť
	private Button select;
	//������λ��ť
	private Button publishSpace;
	private Button manage;
	//��λ����С����γ��
	private TextView pos;
	//��λ��
	private EditText num;
	//��λÿСʱ���
	private EditText price;
	//��λ������ʼʱ��
	private EditText start;
	//��λ���Ž���ʱ��
	private EditText end;
	//��λ�ľ�γ��
	private int[] latlng;
	//��λ��λ��
	private String title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//ȥ������
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_parkingpublish);
		
		//���ʵ������
		select = (Button)findViewById(R.id.select);
		publishSpace = (Button)findViewById(R.id.publish);
		manage = (Button)findViewById(R.id.manage);
		pos = (TextView)findViewById(R.id.position);
		num = (EditText)findViewById(R.id.number);
		price = (EditText)findViewById(R.id.price);
		start = (EditText)findViewById(R.id.startTime);
		end = (EditText)findViewById(R.id.endTime);
		
		//��ȡ��γ�����ݺ͵�ַ
		latlng = this.getLatLng();
		title = this.gettitle();
		if(latlng[0] != 0){
			pos.setText(title);
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
		
		//ѡȡ��λλ�ü����¼�
		publishSpace.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isValid())
					return;
				else{
					PublishSpaceClass pub = new PublishSpaceClass();
					new Thread(pub, "������λ�߳�").start();
				}
			}
		});
		
		manage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ParkingPublish.this, SpaceManagement.class);
				startActivity(intent);
			}
		});
		
	}
	
	class PublishSpaceClass implements Runnable{
		@Override
		public void run(){
			Map<String, String> map=new HashMap<String, String>();
			map.put("id", String.valueOf(OwnerInfo.getId()));
			map.put("lat", String.valueOf(latlng[0]));
			map.put("lng", String.valueOf(latlng[1]));
			map.put("num", num.getText().toString());
			map.put("price", price.getText().toString());
			map.put("start", start.getText().toString());
			map.put("end", end.getText().toString());
			
			//���巢�������URL
			String url=HttpUtil.URL+"ParkingPublic";
			String result="";
			//��������
			try {
				result=new JSONObject(HttpUtil.postRequest(url, map)).getString("result");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Message msg=new Message();
			if(result.equals("succeed"))
			{
				//������λ�ɹ�
				msg.what=0x00;
			}
			else if(result.equals("fail"))
			{
				//������λʧ��
				msg.what=0x01;
			}
			else
			{
				//δ֪������
				msg.what=0x02;
			}
			handler.sendMessage(msg);
			
		}
	}
	
	//��Ϣ������
	final Handler handler=new Handler()
	{
		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message msg)
		{
			if(msg.what == 0x00)
			{
				Toast.makeText(ParkingPublish.this, "������λ�ɹ�", Toast.LENGTH_SHORT).show();
				getData log=new getData();
				new Thread(log,"��ȡ�����߳�").start();//������½�߳�
			}
			else if(msg.what == 0x01){
				Toast.makeText(ParkingPublish.this, "������λʧ��", Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(ParkingPublish.this, "δ֪�������", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	class getData implements Runnable
	{
		@SuppressWarnings("unchecked")
		@Override
		public void run()
		{
			Map<String,String> map=new HashMap<String,String>();
			map.put("id", ""+OwnerInfo.getId());
			String url=HttpUtil.URL+"GetPublishSpaceInfo";
			JSONObject obj;
			String result = "";
			int id = 0;
			JSONArray jArr = new JSONArray();
			ArrayList<SpaceInfoBean> list = null;
			try {
				obj = new JSONObject(HttpUtil.postRequest(url, map));
				result = obj.getString("result");
				jArr = obj.getJSONArray("addresses");
				Log.v("array",jArr.toString());
//				list = (ArrayList<SpaceInfoBean>) obj.get("list").toString();
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(result.equals("succeed"))
			{
				OwnerInfo.initializeList(jArr);
			}
		}
	}
	
	//�����Ƿ�������BaiduMapForLatLng.class������
	private int[] getLatLng(){
		Bundle bundle = this.getIntent().getExtras();
		//latΪlatlng[0],lngΪlatlng[1]
		int[] latlng = new int[2];
		latlng[0] = bundle.getInt("lat");
		latlng[1] = bundle.getInt("lng");
//		Log.v("lat", String.valueOf(latlng[0]));
//		Log.v("lng", String.valueOf(latlng[1]));
		return latlng;
		
	}
	
	//��ȡ��λ��λ��
	private String gettitle(){
		Bundle bundle = this.getIntent().getExtras();
		title = bundle.getString("title");
		return title;
	}
	
	//�ж����������Ƿ�Ϸ�
	private boolean isValid(){
		if(latlng[0] == 0 || latlng[1] == 0)
		{
			pos.setError(Html.fromHtml("<font color=#808183>"
                    + "�޳�λλ����Ϣ "+ "</font>"));
			return false;
		}
		else if(num.getText().toString().trim().equals("") || Integer.parseInt(num.getText().toString()) == 0)
		{
			num.setError(Html.fromHtml("<font color=#808183>"
                    + "��λ�Ų�����Ҫ��"+ "</font>"));
			return false;
		}				
		else if(price.getText().toString().trim().equals("") || Integer.parseInt(price.getText().toString()) == 0)
		{
			price.setError(Html.fromHtml("<font color=#808183>"
                    + "�۸񲻷���Ҫ��"+ "</font>"));
			return false;
		}
		else if(start.getText().toString().trim().equals("") || Integer.parseInt(start.getText().toString()) >= 24)
		{
			start.setError(Html.fromHtml("<font color=#808183>"
                    + "��ʼʱ�䲻����Ҫ��"+ "</font>"));
			return false;
		}
		else if(end.getText().toString().trim().equals("") || Integer.parseInt(end.getText().toString()) >= 24)
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(false);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}

