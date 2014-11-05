package com.zhcs.parkingspaceadmin;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import com.zhcs.regAndLog.R;

public class SoftwareInformation extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//È¥³ý±êÌâ
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.softwareinfo);
	}
}
