package com.zhcs.parkingspaceadmin;

import com.zhcs.regAndLog.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class HaveNotPublish extends Activity{
	private Button goPublic = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//ȥ������
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_havenotpublish);
		
		//���ʵ������
		goPublic = (Button)findViewById(R.id.btnPublic);
		
		//ע�ᰴ�������¼�
		goPublic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// ��ת��������λҳ��
				Intent intent=new Intent(HaveNotPublish.this, ParkingPublish.class);
				Bundle bundle = new Bundle();
				bundle.putInt("lat", 0);
				bundle.putInt("lng", 0);
				bundle.putString("title", "");
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
		});
		
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
