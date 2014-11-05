package com.zhcs.parkingspaceadmin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyCustomReceiver extends BroadcastReceiver{
	private static final String TAG = "MyCustomReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		Log.e(TAG, action);
	}

}
