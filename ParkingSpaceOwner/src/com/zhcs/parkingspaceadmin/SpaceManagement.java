package com.zhcs.parkingspaceadmin;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.zhcs.ownerBean.OwnerInfo;
import com.zhcs.ownerBean.SpaceInfoBean;
import com.zhcs.regAndLog.Login;
import com.zhcs.regAndLog.R;
import com.zhcs.regAndLog.Register;

public class SpaceManagement extends Activity{
	
	private Button publish;
	private ListView mListView;
	private static ArrayList<SpaceInfoBean> list = OwnerInfo.getList();
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//去除标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_spacemanage);
		
		mListView = (ListView)findViewById(R.id.listView);
		publish = (Button)findViewById(R.id.publishSpace);
		
		mListView.setAdapter(new SpaceListAdapter());
		publish.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(SpaceManagement.this, ParkingPublish.class);
				Bundle bundle = new Bundle();
				bundle.putInt("lat", 0);
				bundle.putInt("lng", 0);
				bundle.putString("title", "");
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}
	
	private class SpaceListAdapter extends BaseAdapter{
		public SpaceListAdapter(){
			super();
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			convertView = View.inflate(SpaceManagement.this, R.layout.spaceinfo_item, null);
			TextView title = (TextView)convertView.findViewById(R.id.title);
			TextView time = (TextView)convertView.findViewById(R.id.time);
			TextView price = (TextView)convertView.findViewById(R.id.price);
			//title.setText(String.valueOf(list.get(position).getLat()));
			int num = position + 1;
			title.setText("车位" + num);
			time.setText("时间："+list.get(position).getStart()+":00-"+list.get(position).getEnd()+":00");
			price.setText("价格："+list.get(position).getPrice()+"元/小时");
			return convertView;
		}
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}


