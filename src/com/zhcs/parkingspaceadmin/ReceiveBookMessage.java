package com.zhcs.parkingspaceadmin;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVInstallation;
import com.zhcs.regAndLog.R;

public class ReceiveBookMessage extends Activity{
	private TextView text;
	private BroadcastReceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//È¥³ý±êÌâ
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_getmsgfromdriver);
		text = (TextView) findViewById(R.id.textView1);
		
		IntentFilter intentFilter = new IntentFilter();
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //extract our message from intent
                String msg_for_me = intent.getStringExtra("msg");
                //log our message value
                Log.e("InchooTutorial1", msg_for_me);
                String msg = intent.getExtras().getString("msg");
                //log our message value
                Log.e("InchooTutorial2", msg_for_me);
                text.setText(msg_for_me);
            }
        };
        //registering our receiver
        this.registerReceiver(mReceiver, intentFilter);
		
	}

}
