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
	//�޸ĳ�λ��ť
	private Button modify;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//ȥ������
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_modifyspace);
		
		//���ʵ������
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
		//������ʾ�ĳ�λ��Ϣ
		//pos.setText(address);
		num.setText(String.valueOf(bean.getNum()));
		price.setText(String.valueOf(bean.getPrice()));
		fine.setText(String.valueOf(bean.getFine()));
		start.setText(String.valueOf(bean.getStart()));
		end.setText(String.valueOf(bean.getEnd()));
		
		//�޸ĳ�λ���ɹ��޸ĵ�Ϊ�۸񣬿�ʼʱ�䣬����ʱ��
		modify.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isValid())
					return;
				if(bean.getState() != 0) {
					Toast.makeText(ModifySpace.this,"��ǰ��λ�����ڶ��Ļ�ʹ��״̬�������޸�", Toast.LENGTH_SHORT).show();
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
					            Log.d("�޸ĳɹ�", "��ѯ��" + avObjects.size() + " ����������������");
					            AVObject mod = avObjects.get(0);
					          //�޸ļ۸񣬿�ʼʱ�䣬����ʱ��
								mod.put("price", Integer.parseInt(price.getText().toString()));
								mod.put("fine", Integer.parseInt(fine.getText().toString()));
								mod.put("start", Integer.parseInt(start.getText().toString()));
								mod.put("end", Integer.parseInt(end.getText().toString()));
								mod.saveInBackground(new SaveCallback() {
									@Override
									public void done(AVException arg0) {
										if (arg0 == null) {
											Toast.makeText(ModifySpace.this,"�޸ĳ�λ�ɹ�", Toast.LENGTH_SHORT).show();
											//��ȡ�����ѷ�����λ��Ϣ
											GetPublicSpaceData.getSpaceInfo(OwnerInfo.getId());
								        } else {
								        	Toast.makeText(ModifySpace.this,"�޸ĳ�λʧ��", Toast.LENGTH_SHORT).show();
									}
								}
					        });
					        }else {
					        	Log.d("ʧ��", "������Ϣ"+e.getMessage());
					        }
						}
					});
					
				}
			}
		});
		
	}
	
	private void getAddress(String objectId) {
		//��ȡĳ����λ�ĵ�ַ
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
		if(num.getText().toString().trim().equals("") || Integer.parseInt(num.getText().toString()) == 0)
		{
			num.setError(Html.fromHtml("<font color=#808183>"
                    + "��λ�Ų�����Ҫ��"+ "</font>"));
			return false;
		}				
		else if(price.getText().toString().trim().equals("") || Integer.parseInt(price.getText().toString()) == 0)
		{
			price.setError(Html.fromHtml("<font color=#808183>"
                    + "�۸񲻷���Ҫ��"+ "</font>"));
			return false;
		}
		else if(start.getText().toString().trim().equals("") || Integer.parseInt(start.getText().toString()) >= 24)
		{
			start.setError(Html.fromHtml("<font color=#808183>"
                    + "��ʼʱ�䲻����Ҫ��"+ "</font>"));
			return false;
		}
		else if(end.getText().toString().trim().equals("") || Integer.parseInt(end.getText().toString()) >= 24)
		{
			end.setError(Html.fromHtml("<font color=#808183>"
                    + "����ʱ�䲻����Ҫ��"+ "</font>"));
			return false;
		}
		else if(Integer.parseInt(start.getText().toString()) >= Integer.parseInt(end.getText().toString()))
		{
			start.setError(Html.fromHtml("<font color=#808183>"
                    + "��ʼʱ�䲻�ܴ��ڽ���ʱ��"+ "</font>"));
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

