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
 * ��λ��������ѡ��ҳ��
 */
public class ShareDateSelect extends Activity {
	/**
	 * ��������
	 */
	List<Calendar> shareDate;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sharecal);
		//��ȡ������������
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
	 * ȷ�ϰ�ť��������
	 */
	public void confirmShareDate(View view){
		//���湲����������
		ParkingShareDate.setShareDate(shareDate);
		Intent intent = new Intent(ShareDateSelect.this, ParkingPublish.class);
		startActivity(intent);
		finish();
	}
}

