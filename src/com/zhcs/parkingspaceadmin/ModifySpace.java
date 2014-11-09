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
	//修改车位按钮
	private Button modify;
	/**
	 * 修改车位共享日期
	 */
	private Button shareDate;
	//车位所在小区经纬度
	private TextView pos;
	//车位号
	private TextView num;
	//车位每小时租金
	private EditText price;
	//车位每小时违约价格
	private EditText fine;
	//车位开放起始时间
	private EditText start;
	//车位开放结束时间
	private EditText end;
	//车位的经纬度
	private int lat;
	private int lng;
	//车位所在小区序号
	private int index;
	//地址信息
	private String address = "";
	Date startDate = new Date();
	Date endDate = new Date();
	List<Calendar> shareDateList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//去除标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_modifyspace);
		
		//获得实例对象
		modify = (Button)findViewById(R.id.modify);
		shareDate = (Button)findViewById(R.id.mod_sharedate);
		pos = (TextView)findViewById(R.id.mod_position);
		num = (TextView)findViewById(R.id.mod_number);
		price = (EditText)findViewById(R.id.mod_price);
		fine = (EditText)findViewById(R.id.mod_fine);
		start = (EditText)findViewById(R.id.mod_start);
		end = (EditText)findViewById(R.id.mod_end);
		
		//设置监听事件
		start.setOnTouchListener(this); 
        end.setOnTouchListener(this); 
		
		Bundle data = this.getIntent().getExtras();
		int index = data.getInt("index");
		ArrayList<SpaceInfoBean> list = OwnerInfo.getList();
		final SpaceInfoBean bean = list.get(index);
		getAddress(bean.getCommunityid());
		//设置显示的车位信息
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
		 * 修改车位，可供修改的为共享日期，价格，罚款，开始时间，结束时间
		 */
		modify.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isValid())
					return;
				if(bean.getState() != 0) {
					Toast.makeText(ModifySpace.this,"当前车位正处于订阅或使用状态，不能修改", Toast.LENGTH_SHORT).show();
				}
				else {
					AVQuery<AVObject> query = new AVQuery<AVObject>("SpaceInfo");
					Log.d("objectid", bean.getSpaceid());
					
					query.whereEqualTo("objectId", bean.getSpaceid());
					query.findInBackground(new FindCallback<AVObject>() {
						@Override
					    public void done(List<AVObject> avObjects, AVException e) {
					        if (e == null) {
					            Log.d("修改成功", "查询到" + avObjects.size() + " 条符合条件的数据");
					            AVObject mod = avObjects.get(0);
					            //修改共享日期，价格，罚款，开始时间，结束时间
					            //先制空，再添加
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
											Toast.makeText(ModifySpace.this,"修改车位成功", Toast.LENGTH_SHORT).show();
											ParkingShareDate.setShareDate(null);
											Intent intent=new Intent(ModifySpace.this, SpaceManagement.class);
											startActivity(intent);
											finish();
								        } else {
								        	Toast.makeText(ModifySpace.this,"修改车位失败", Toast.LENGTH_SHORT).show();
								        	Log.e("modify", arg0.getMessage());
									}
								}
					        });
					        }else {
					        	Log.d("modify", "错误信息"+e.getMessage());
					        }
						}
					});
					
				}
			}
		});
		
	}
	
	/**
	 * @param 
	 * 修改车位共享日期
	 */
	public void shareDateSelect(View v){
		Intent intent = new Intent(ModifySpace.this, ShareDateSelect.class);
		startActivity(intent);
	}
	
	/**
	 * @param objectId 车位的id
	 * 根据车位id获取其地址信息
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
		        	Log.d("SpaceInfo", "更改状态失败"+e.getMessage());
		        }
			}
		});
	}
	
	//判断输入数据是否合法
	private boolean isValid(){
		if(num.getText().toString().trim().equals("") || Integer.parseInt(num.getText().toString()) == 0){
			num.setError(Html.fromHtml("<font color=#808183>"
                    + "车位号不符合要求"+ "</font>"));
			return false;
		}				
		else if(price.getText().toString().trim().equals("") || Integer.parseInt(price.getText().toString()) <= 0){
			price.setError(Html.fromHtml("<font color=#808183>"
                    + "价格不符合要求"+ "</font>"));
			return false;
		}
		else if(fine.getText().toString().trim().equals("") || Integer.parseInt(fine.getText().toString()) <= 0){
			fine.setError(Html.fromHtml("<font color=#808183>"
                    + "价格不符合要求"+ "</font>"));
			return false;
		}
		else if(start.getText().toString().compareTo(end.getText().toString()) >= 0){
			start.setError(Html.fromHtml("<font color=#808183>"
                    + "结束时间要大于开始时间"+ "</font>"));
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
           
                builder.setTitle("选取起始时间"); 
                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() { 
   
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
                
                builder.setTitle("选取结束时间"); 
                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() { 
   
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

