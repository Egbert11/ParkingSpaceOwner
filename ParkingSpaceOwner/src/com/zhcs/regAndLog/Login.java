package com.zhcs.regAndLog;

import java.util.*;

import com.zhcs.net.*;
import com.zhcs.ownerBean.OwnerInfo;
import com.zhcs.ownerBean.SpaceInfoBean;
import com.zhcs.parkingspaceadmin.HaveNotPublish;
import com.zhcs.parkingspaceadmin.SpaceManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity{
	private EditText phoneNum;
	private EditText password;
	private CheckBox rem_pw, auto_login; 
	private Button login;
	private Button reg;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//ȥ������
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		
		//���ʵ������
		sp=this.getSharedPreferences("Userinfo", Context.MODE_WORLD_READABLE);
		phoneNum=(EditText)findViewById(R.id.Phone);
		password=(EditText)findViewById(R.id.password);
		rem_pw = (CheckBox) findViewById(R.id.cb_mima);  
        auto_login = (CheckBox) findViewById(R.id.cb_auto);
		login=(Button)findViewById(R.id.log);
		reg=(Button)findViewById(R.id.reg);
		
		//�жϼ�ס�����ѡ���״̬  
        if(sp.getBoolean("ISCHECK", false))  
        {  
          //Ĭ��Ϊ��ס����
          rem_pw.setChecked(true);
          phoneNum.setText(sp.getString("PHONENUM", ""));  
          password.setText(sp.getString("PASSWORD", ""));  
          //�ж��Զ���½��ѡ��״̬  
          if(sp.getBoolean("AUTO_ISCHECK", false))  
          {  
        	  	// ִ������У��
				if(validate())
				{
					login log=new login();
					new Thread(log,"��½�߳�").start();//������¼�߳�
				}  
			}  
         }  
		
		
		//������ס�����ѡ��ť�¼�
		rem_pw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
				{
					sp.edit().putBoolean("ISCHECK", true).commit();
				}
				else{
					sp.edit().putBoolean("ISCHECK", false).commit();
				}
			}
		});
		
		//�����Զ���¼��ѡ��ť�¼�
		auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
				{
					sp.edit().putBoolean("AUTO_ISCHECK", true).commit();
				}
				else{
					sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
				}
			}
		});
		
		
		//ע�ᰴ�������¼�
		reg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// ��ת��ע�����
				Intent intent=new Intent(Login.this,Register.class);
				startActivity(intent);
			}
		});
		
		//��½���������¼�
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0){
				// ִ������У��
				if(validate())
				{
					login log=new login();
					new Thread(log,"��½�߳�").start();//������½�߳�
				}
			}
		});
	}
//	//�������̣߳����ڻ�ȡjson����
//	class JsonRequest implements Runnable
//	{
//		@Override
//		public void run()
//		{
////			String url = "http://192.168.1.102:8080/city.txt";
////			Log.v("aaaa",url);
//			JSONObject result, obj;
//			try {
//				//result =HttpUtil.doGet(url);
//				result = new JSONObject("{'request':{'province':'guangdong'}}");
//				Log.v("huang", result.toString());
//				obj = result.getJSONObject("request");
//				Log.v("province", obj.getString("province"));
//				//Toast.makeText(Login.this, obj.getString("province"), Toast.LENGTH_SHORT).show();
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}			
//			
//		}
//	}
	
	//�������̣߳������û���¼
	class login implements Runnable
	{
		@SuppressWarnings("unchecked")
		@Override
		public void run()
		{
			String phone=phoneNum.getText().toString();
			String pass=password.getText().toString();
			Map<String,String> map=new HashMap<String,String>();
			map.put("phone", phone);
			map.put("pass", pass);
			
			 
			//���巢�������URL
			String url=HttpUtil.URL+"OwnerLogin";
			JSONObject obj;
			String result = "";
			int id = 0;
			JSONArray jArr = new JSONArray();
			ArrayList<SpaceInfoBean> list = null;
			try {
				obj = new JSONObject(HttpUtil.postRequest(url, map));
				result = obj.getString("result");
				id = obj.getInt("owner_id");
				jArr = obj.getJSONArray("addresses");
				Log.v("arr",jArr.toString());
//				list = (ArrayList<SpaceInfoBean>) obj.get("list").toString();
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			Message msg=new Message();
			if(result.equals("succeed"))
			{
				if(rem_pw.isChecked())  
	            {  
	             //��ס�û���������
	              Editor editor = sp.edit();  
	              editor.putString("PHONENUM",phone);  
	              editor.putString("PASSWORD",pass);  
	              editor.commit();  
	            }
				//���ó�λ����id���ֻ�
				OwnerInfo.setId(id);
				OwnerInfo.setPhone(phone);
				OwnerInfo.initializeList(jArr);
				//Toast.makeText(Login.this,"��½�ɹ�", Toast.LENGTH_SHORT).show();			
				msg.what=0x01;//��½�ɹ�
			}
			else
			{
				msg.what=0x00;//��¼ʧ��
				//Toast.makeText(Login.this, "��½ʧ��", Toast.LENGTH_SHORT).show();
			}
			handler.sendMessage(msg);
		}
	}
	
	final Handler handler=new Handler()
	{
		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message msg)
		{
			if(msg.what == 0x01)
			{
				Toast.makeText(Login.this, "��½�ɹ�", Toast.LENGTH_SHORT).show();
				Bundle bundle=new Bundle();
				bundle.putString("phoneNUM", phoneNum.getText().toString());
				Intent intent=new Intent(Login.this,SpaceManagement.class);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
			else if(msg.what == 0x00){
				Toast.makeText(Login.this, "��½ʧ��", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	//���û�������ֻ��š��������У���Ƿ�Ϊ��
	private boolean validate()
	{
		String phone=phoneNum.getText().toString().trim();//trim������ȥ�ո�ķ���
		String pass=password.getText().toString().trim();//trim������ȥ�ո�ķ���
		if(phone.equals("")||pass.equals(""))
		{
			new AlertDialog.Builder(Login.this)
			.setIcon(getResources().getDrawable(R.drawable.login_error_icon))
			.setTitle("��¼ʧ��")
			.setMessage("�ֻ��Ż������벻��Ϊ�գ�\n������������룡")
			.create().show();
			return false;
		}
		return true;
	}

}
