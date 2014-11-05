package com.zhcs.parkingspaceadmin;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.zhcs.ownerBean.Community;
import com.zhcs.ownerBean.CommunityInfoBean;

public class MyOverlay extends ItemizedOverlay<OverlayItem> {
    
    private static Activity activity;
    private static ArrayList<CommunityInfoBean> list;
    private static int index;
    private static OverlayItem item;

    public MyOverlay(Activity activity, Drawable drawable, MapView mapView) {
        super(drawable, mapView);
        MyOverlay.activity = activity;
    }

    @Override
	protected boolean onTap(int i) {
        item = this.getItem(i);
        //Toast.makeText(activity, item.getTitle()+" hah "+item.getSnippet(), Toast.LENGTH_LONG).show();
        index = Integer.parseInt(item.getSnippet());
        list = Community.getList();
        new AlertDialog.Builder(activity).setTitle("ѡ��С��").setMessage("�㵱ǰѡ���λ����" + 
        list.get(i).getAddress() + ",ȷ��ѡ��ǰС�����г�λ������")
        		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
        			
        			@Override
        			public void onClick(DialogInterface dialog, int which) {
//        		        Toast.makeText(activity, item.getTitle()+" hah "+item.getSnippet(), Toast.LENGTH_LONG).show();
        				//���õ�ǰ������λ����С��index
        		        Community.setIndex(index);
        		        Intent intent = new Intent(activity, ParkingPublish.class);
						activity.startActivity(intent);
						activity.finish();
        			};
        				}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
        					
        					@Override
        					public void onClick(DialogInterface dialog, int which) {
        						// TODO Auto-generated method stub
        						dialog.dismiss();
        						
        					}
        				}).show();
        return true;
    }
}
