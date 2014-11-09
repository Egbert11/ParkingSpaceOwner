package com.zhcs.parkingspaceadmin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.zhcs.ownerBean.Community;
import com.zhcs.ownerBean.OwnerInfo;
import com.zhcs.ownerBean.ParkingShareDate;
import com.zhcs.ownerBean.SpaceInfoBean;
import com.zhcs.regAndLog.Login;
import com.zhcs.regAndLog.R;

import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.view.View;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ModifySpace extends Activity implements View.OnTouchListener{
	//�޸ĳ�λ��ť
	private Button modify;
	/**
	 * �޸ĳ�λ��������
	 */
	private Button shareDate;
	//��λ����С����γ��
	private TextView pos;
	//��λ��
	private TextView num;
	//��λÿСʱ���
	private EditText price;
	//��λÿСʱΥԼ�۸�
	private EditText fine;
	//��λ������ʼʱ��
	private EditText start;
	//��λ���Ž���ʱ��
	private EditText end;
	//��λ�ľ�γ��
	private int lat;
	private int lng;
	//��λ����С�����
	private int index;
	//��ַ��Ϣ
	private String address = "";
	Date startDate = new Date();
	Date endDate = new Date();
	List<Calendar> shareDateList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//ȥ������
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_modifyspace);
		
		//���ʵ������
		modify = (Button)findViewById(R.id.modify);
		shareDate = (Button)findViewById(R.id.mod_sharedate);
		pos = (TextView)findViewById(R.id.mod_position);
		num = (TextView)findViewById(R.id.mod_number);
		price = (EditText)findViewById(R.id.mod_price);
		fine = (EditText)findViewById(R.id.mod_fine);
		start = (EditText)findViewById(R.id.mod_start);
		end = (EditText)findViewById(R.id.mod_end);
		
		//���ü����¼�
		start.setOnTouchListener(this); 
        end.setOnTouchListener(this); 
		
		Bundle data = this.getIntent().getExtras();
		int index = data.getInt("index");
		ArrayList<SpaceInfoBean> list = OwnerInfo.getList();
		final SpaceInfoBean bean = list.get(index);
		getAddress(bean.getCommunityid());
		//������ʾ�ĳ�λ��Ϣ
		//pos.setText(address);
		num.setText(String.valueOf(bean.getNum()));
		price.setText(String.valueOf(bean.getPrice()));
		fine.setText(String.valueOf(bean.getFine()));
		
		startDate = bean.getStart();
		endDate = bean.getEnd();
		SimpleDateFormat df=new SimpleDateFormat("HH:mm");
		String startTime=df.format(startDate);
		String endTime=df.format(endDate);
		start.setText(startTime);
		end.setText(endTime);
		ParkingShareDate.setShareDate(bean.getShareTime());
		
		/**
		 * �޸ĳ�λ���ɹ��޸ĵ�Ϊ�������ڣ��۸񣬷����ʼʱ�䣬����ʱ��
		 */
		modify.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isValid())
					return;
				if(bean.getState() != 0) {
					Toast.makeText(ModifySpace.this,"��ǰ��λ�����ڶ��Ļ�ʹ��״̬�������޸�", Toast.LENGTH_SHORT).show();
				}
				else {
					AVQuery<AVObject> query = new AVQuery<AVObject>("SpaceInfo");
					Log.d("objectid", bean.getSpaceid());
					
					query.whereEqualTo("objectId", bean.getSpaceid());
					query.findInBackground(new FindCallback<AVObject>() {
						@Override
					    public void done(List<AVObject> avObjects, AVException e) {
					        if (e == null) {
					            Log.d("�޸ĳɹ�", "��ѯ��" + avObjects.size() + " ����������������");
					            AVObject mod = avObjects.get(0);
					            //�޸Ĺ������ڣ��۸񣬷����ʼʱ�䣬����ʱ��
					            //���ƿգ������
					            List<Calendar> nullShareDate = new ArrayList<Calendar>();
								mod.put("shareDate", nullShareDate);
					            mod.addAllUnique("shareDate", ParkingShareDate.getShareDate());
								mod.put("price", Integer.parseInt(price.getText().toString()));
								mod.put("fine", Integer.parseInt(fine.getText().toString()));
								mod.put("start", startDate);
								mod.put("end", endDate);
								mod.saveInBackground(new SaveCallback() {
									@Override
									public void done(AVException arg0) {
										if (arg0 == null) {
											Toast.makeText(ModifySpace.this,"�޸ĳ�λ�ɹ�", Toast.LENGTH_SHORT).show();
											ParkingShareDate.setShareDate(null);
											Intent intent=new Intent(ModifySpace.this, SpaceManagement.class);
											startActivity(intent);
											finish();
								        } else {
								        	Toast.makeText(ModifySpace.this,"�޸ĳ�λʧ��", Toast.LENGTH_SHORT).show();
								        	Log.e("modify", arg0.getMessage());
									}
								}
					        });
					        }else {
					        	Log.d("modify", "������Ϣ"+e.getMessage());
					        }
						}
					});
					
				}
			}
		});
		
	}
	
	/**
	 * @param 
	 * �޸ĳ�λ��������
	 */
	public void shareDateSelect(View v){
		Intent intent = new Intent(ModifySpace.this, ShareDateSelect.class);
		startActivity(intent);
	}
	
	/**
	 * @param objectId ��λ��id
	 * ���ݳ�λid��ȡ���ַ��Ϣ
	 */
	private void getAddress(String objectId) {
		AVQuery<AVObject> space = new AVQuery<AVObject>("Community");
		space.whereEqualTo("objectId", objectId);
		space.findInBackground(new FindCallback<AVObject>() {
			@Override
		    public void done(List<AVObject> avObjects, AVException e) {
		        if (e == null) {
		        	AVObject modState = avObjects.get(0);
		        	address = modState.getString("address");
		        	pos.setText(address);
		        } else {
		        	Log.d("SpaceInfo", "����״̬ʧ��"+e.getMessage());
		        }
			}
		});
	}
	
	//�ж����������Ƿ�Ϸ�
	private boolean isValid(){
		if(num.getText().toString().trim().equals("") || Integer.parseInt(num.getText().toString()) == 0){
			num.setError(Html.fromHtml("<font color=#808183>"
                    + "��λ�Ų�����Ҫ��"+ "</font>"));
			return false;
		}				
		else if(price.getText().toString().trim().equals("") || Integer.parseInt(price.getText().toString()) <= 0){
			price.setError(Html.fromHtml("<font color=#808183>"
                    + "�۸񲻷���Ҫ��"+ "</font>"));
			return false;
		}
		else if(fine.getText().toString().trim().equals("") || Integer.parseInt(fine.getText().toString()) <= 0){
			fine.setError(Html.fromHtml("<font color=#808183>"
                    + "�۸񲻷���Ҫ��"+ "</font>"));
			return false;
		}
		else if(start.getText().toString().compareTo(end.getText().toString()) >= 0){
			start.setError(Html.fromHtml("<font color=#808183>"
                    + "����ʱ��Ҫ���ڿ�ʼʱ��"+ "</font>"));
			return false;
		}
		else{
			return true;
		}
	}
//	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			moveTaskToBack(false);
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) { 
			   
            AlertDialog.Builder builder = new AlertDialog.Builder(this); 
            View view = View.inflate(this, R.layout.activity_timedialog, null); 
            final TimePicker timePicker = (android.widget.TimePicker) view.findViewById(R.id.time_picker); 
            builder.setView(view); 
   
            Calendar cal = Calendar.getInstance(); 
            cal.setTimeInMillis(System.currentTimeMillis());  
   
            timePicker.setIs24HourView(true); 
//            timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY)); 
//            timePicker.setCurrentMinute(Calendar.MINUTE);
   
            if (v.getId() == R.id.mod_start) { 
                timePicker.setCurrentHour(Integer.parseInt(start.getText().toString().substring(0, 2))); 
                timePicker.setCurrentMinute(Integer.parseInt(start.getText().toString().substring(3, 5))); 
                final int inType = start.getInputType(); 
                start.setInputType(InputType.TYPE_NULL); 
                start.onTouchEvent(event); 
                start.setInputType(inType); 
                start.setSelection(start.getText().length()); 
           
                builder.setTitle("ѡȡ��ʼʱ��"); 
                builder.setPositiveButton("ȷ  ��", new DialogInterface.OnClickListener() { 
   
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
   
                        StringBuffer sb = new StringBuffer(); 
                        int hour = timePicker.getCurrentHour();
                        int minute = timePicker.getCurrentMinute();
                        if(hour < 10)
                        	sb.append(0);
                        sb.append(hour).append(":");
                        if(minute < 10)
                        	sb.append(0);
                        sb.append(minute); 
                        start.setText(sb); 
                        
                        Calendar c = Calendar.getInstance();
                        c.set(0, 0, 0, hour, minute, 0);
                        Log.e("startTime", hour + ":" + minute);
                        startDate = c.getTime();
                        end.requestFocus(); 
                           
                        dialog.cancel(); 
                    } 
                }); 
                   
            } else if (v.getId() == R.id.mod_end) { 
            	timePicker.setCurrentHour(Integer.parseInt(end.getText().toString().substring(0, 2))); 
                timePicker.setCurrentMinute(Integer.parseInt(end.getText().toString().substring(3, 5)));
                int inType = end.getInputType(); 
                end.setInputType(InputType.TYPE_NULL);     
                end.onTouchEvent(event); 
                end.setInputType(inType); 
                end.setSelection(end.getText().length()); 
                
                builder.setTitle("ѡȡ����ʱ��"); 
                builder.setPositiveButton("ȷ  ��", new DialogInterface.OnClickListener() { 
   
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
   
                        StringBuffer sb = new StringBuffer(); 
                        int hour = timePicker.getCurrentHour();
                        int minute = timePicker.getCurrentMinute();
                        if(hour < 10)
                        	sb.append(0);
                        sb.append(hour).append(":");
                        if(minute < 10)
                        	sb.append(0);
                        sb.append(minute); 
                        end.setText(sb); 
                        
                        Calendar c = Calendar.getInstance();
                        c.set(0, 0, 0, hour, minute, 0);
                        Log.e("endTime", hour + ":" + minute);
                        endDate = c.getTime();
                        dialog.cancel(); 
                    } 
                }); 
            } 
               
            Dialog dialog = builder.create(); 
            dialog.show(); 
        } 
   
        return true; 
	}
}

