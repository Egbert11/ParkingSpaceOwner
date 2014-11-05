package com.zhcs.parkingspaceadmin;

import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.zhcs.ownerBean.OwnerInfo;
import com.zhcs.ownerBean.SpaceInfoBean;
import com.zhcs.regAndLog.R;

import android.text.Html;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ModifySpace extends Activity{
	//修改车位按钮
	private Button modify;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//去除标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_modifyspace);
		
		//获得实例对象
		modify = (Button)findViewById(R.id.modify);
		pos = (TextView)findViewById(R.id.mod_position);
		num = (TextView)findViewById(R.id.mod_number);
		price = (EditText)findViewById(R.id.mod_price);
		fine = (EditText)findViewById(R.id.mod_fine);
		start = (EditText)findViewById(R.id.mod_start);
		end = (EditText)findViewById(R.id.mod_end);
		
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
		start.setText(String.valueOf(bean.getStart()));
		end.setText(String.valueOf(bean.getEnd()));
		
		//修改车位，可供修改的为价格，开始时间，结束时间
		modify.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isValid())
					return;
				if(bean.getState() != 0) {
					Toast.makeText(ModifySpace.this,"当前车位正处于订阅或使用状态，不能修改", Toast.LENGTH_SHORT).show();
				}
				else {
//					AVObject mod = new AVObject("SpaceInfo");
					AVQuery<AVObject> query = new AVQuery<AVObject>("SpaceInfo");
					Log.d("objectid", bean.getSpaceid());
					
					query.whereEqualTo("objectId", bean.getSpaceid());
					query.findInBackground(new FindCallback<AVObject>() {
						@Override
					    public void done(List<AVObject> avObjects, AVException e) {
					        if (e == null) {
					            Log.d("修改成功", "查询到" + avObjects.size() + " 条符合条件的数据");
					            AVObject mod = avObjects.get(0);
					          //修改价格，开始时间，结束时间
								mod.put("price", Integer.parseInt(price.getText().toString()));
								mod.put("fine", Integer.parseInt(fine.getText().toString()));
								mod.put("start", Integer.parseInt(start.getText().toString()));
								mod.put("end", Integer.parseInt(end.getText().toString()));
								mod.saveInBackground(new SaveCallback() {
									@Override
									public void done(AVException arg0) {
										if (arg0 == null) {
											Toast.makeText(ModifySpace.this,"修改车位成功", Toast.LENGTH_SHORT).show();
											//获取最新已发布车位信息
											GetPublicSpaceData.getSpaceInfo(OwnerInfo.getId());
								        } else {
								        	Toast.makeText(ModifySpace.this,"修改车位失败", Toast.LENGTH_SHORT).show();
									}
								}
					        });
					        }else {
					        	Log.d("失败", "错误信息"+e.getMessage());
					        }
						}
					});
					
				}
			}
		});
		
	}
	
	private void getAddress(String objectId) {
		//获取某个车位的地址
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
		if(num.getText().toString().trim().equals("") || Integer.parseInt(num.getText().toString()) == 0)
		{
			num.setError(Html.fromHtml("<font color=#808183>"
                    + "车位号不符合要求"+ "</font>"));
			return false;
		}				
		else if(price.getText().toString().trim().equals("") || Integer.parseInt(price.getText().toString()) == 0)
		{
			price.setError(Html.fromHtml("<font color=#808183>"
                    + "价格不符合要求"+ "</font>"));
			return false;
		}
		else if(start.getText().toString().trim().equals("") || Integer.parseInt(start.getText().toString()) >= 24)
		{
			start.setError(Html.fromHtml("<font color=#808183>"
                    + "开始时间不符合要求"+ "</font>"));
			return false;
		}
		else if(end.getText().toString().trim().equals("") || Integer.parseInt(end.getText().toString()) >= 24)
		{
			end.setError(Html.fromHtml("<font color=#808183>"
                    + "结束时间不符合要求"+ "</font>"));
			return false;
		}
		else if(Integer.parseInt(start.getText().toString()) >= Integer.parseInt(end.getText().toString()))
		{
			start.setError(Html.fromHtml("<font color=#808183>"
                    + "开始时间不能大于结束时间"+ "</font>"));
			return false;
		}
		else
		{
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
}

