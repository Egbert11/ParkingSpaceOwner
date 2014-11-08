package com.zhcs.parkingspaceadmin;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.squareup.timessquare.CalendarPickerView;
import com.zhcs.ownerBean.ParkingShareDate;
import com.zhcs.regAndLog.R;
/**
 * 
 * @author huangjiaming
 * 车位共享日期选择页面
 */
public class ShareDateSelect extends Activity {
	/**
	 * 共享日期
	 */
	List<Calendar> shareDate;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sharecal);
		//获取共享日期数据
		shareDate = ParkingShareDate.getShareDate();
		
		Calendar nextYear = Calendar.getInstance();
		nextYear.add(Calendar.YEAR, 1);
		CalendarPickerView calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
		calendar.init(new Date(), new Date(), nextYear.getTime(), shareDate);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_sharecal, menu);
		return true;
	}
	
	/**
	 * @param view
	 * 确认按钮触发函数
	 */
	public void confirmShareDate(View view){
		//保存共享日期数据
		ParkingShareDate.setShareDate(shareDate);
		Intent intent = new Intent(ShareDateSelect.this, ParkingPublish.class);
		startActivity(intent);
		finish();
	}
}

