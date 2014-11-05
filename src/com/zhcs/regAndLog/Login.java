package com.zhcs.regAndLog;

import java.util.*;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.zhcs.ownerBean.OwnerInfo;
import com.zhcs.parkingspaceadmin.GetPublicSpaceData;
import com.zhcs.parkingspaceadmin.ReceiveBookMessage;
import com.zhcs.parkingspaceadmin.SpaceManagement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Login extends Activity{
	private EditText phoneNum;
	private EditText password;
	private CheckBox rem_pw, auto_login; 
	private Button login;
	private ImageButton reg;
	private SharedPreferences sp;
	private ProgressDialog proDialog;
	private final static String TAG = "LoginActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//ȥ������
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		
		PushService.setDefaultPushCallback(this, ReceiveBookMessage.class);
	    // ����Ƶ��������Ƶ����Ϣ������ʱ�򣬴򿪶�Ӧ�� Activity
	    PushService.subscribe(this, "public", ReceiveBookMessage.class);
		//���ʵ������
		sp=this.getSharedPreferences("Userinfo", Context.MODE_WORLD_READABLE);
//		phoneNum=(EditText)findViewById(R.id.Phone);
//		password=(EditText)findViewById(R.id.password);
//		rem_pw = (CheckBox) findViewById(R.id.cb_mima);  
//        auto_login = (CheckBox) findViewById(R.id.cb_auto);
//		login=(Button)findViewById(R.id.log);
//		reg=(Button)findViewById(R.id.reg);
		loginInti();
		
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
					loginToServer();
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
					loginToServer();
				}
			}
		});
	}
	
	//��ʼ���ؼ�
	private void loginInti() {
		phoneNum=(EditText)findViewById(R.id.Phone);
		password=(EditText)findViewById(R.id.password);
		//���������checkbox
		rem_pw = (CheckBox) findViewById(R.id.cb_mima);  
		//�Զ���½��checkbox
		auto_login = (CheckBox) findViewById(R.id.cb_auto);
		login=(Button)findViewById(R.id.log);
		reg=(ImageButton)findViewById(R.id.reg);
	}
	/*
	 * ��½��������
	 */
	private void loginToServer(){
		//��ʾ���ڵ�¼��
		proDialog = new ProgressDialog(Login.this);
		proDialog.setMessage("���ڵ�¼�����Ժ�...");
		proDialog.show();
		//��֤�û�������
		final String phone=phoneNum.getText().toString();
		final String pass=password.getText().toString();
		AVQuery<AVObject> query = new AVQuery<AVObject>("OwnerInfo");
		query.whereEqualTo("phone", phone);
		query.findInBackground(new FindCallback<AVObject>() {
			@Override
		    public void done(List<AVObject> avObjects, AVException e) {
				Message msg = new Message();
		        if (e == null) {
		            Log.d("�ɹ�", "��ѯ��" + avObjects.size() + " ����������������");
		            AVObject obj = avObjects.get(0);
		            if(pass.equals(obj.getString("password"))) {
		            	if(rem_pw.isChecked())  
			            {  
			             //��ס�û���������
			              Editor editor = sp.edit();  
			              editor.putString("PHONENUM",phone);  
			              editor.putString("PASSWORD",pass);  
			              editor.commit();  
			            }
		            	// ���� installation ��������
		                AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
		                  @Override
		                  public void done(AVException e) {
		                    AVInstallation.getCurrentInstallation().saveInBackground();
		                  }
		                });
						obj.put("installationid",AVInstallation.getCurrentInstallation().getInstallationId());
						obj.saveInBackground();
						//���ó�λ����id���ֻ�
						OwnerInfo.setId(obj.getObjectId());
						OwnerInfo.setPhone(phone);	
						//��ȡ�ѷ�����λ��Ϣ
			            GetPublicSpaceData.getSpaceInfo(OwnerInfo.getId());
			            //��¼�ɹ�
						msg.what = 0x00;
						handler.sendMessage(msg);
						
		            }else {
		            	//�������
		            	msg.what = 0x01;
						handler.sendMessage(msg);
		            }
		        } else {
		            Log.d("ʧ��", "��½ʧ��: " + e.getMessage());
		            //�û���������
		            msg.what = 0x02;
					handler.sendMessage(msg);
		        }
		    }
		});
	
	}
	
	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==0x00){
				proDialog.dismiss();
				Toast.makeText(Login.this,"��¼�ɹ�", Toast.LENGTH_SHORT).show();
				Intent intent=new Intent(Login.this,SpaceManagement.class);
				startActivity(intent);
			}
			else if(msg.what==0x01){
				proDialog.dismiss();
				Toast.makeText(Login.this, "��¼ʧ��", Toast.LENGTH_SHORT).show();
			}
			else if(msg.what==0x02){
				proDialog.dismiss();
				Toast.makeText(Login.this, "�û���������", Toast.LENGTH_SHORT).show();
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
	
	@Override
	public void onBackPressed() {
	    //ʵ��Home��Ч������Ҫ���Ȩ��:<uses-permission android:name="android.permission.RESTART_PACKAGES" />
	    //super.onBackPressed();��仰һ��Ҫע��,��Ȼ��ȥ����Ĭ�ϵ�back����ʽ��
	    Intent i= new Intent(Intent.ACTION_MAIN);
	    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    i.addCategory(Intent.CATEGORY_HOME);
	    startActivity(i); 
	}
}

