package com.zhcs.ownerBean;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;

import com.avos.avoscloud.AVObject;
import com.zhcs.parkingspaceadmin.SpaceManagement;
/**
 * 
 * @author huangjiaming
 * ��¼ҵ���Ļ�����Ϣ������id, phone�� �绰, list: �ѷ�����λ��Ϣ
 */
public class OwnerInfo {
	private static String id;
	private static String phone;
	private static ArrayList<SpaceInfoBean> list = new ArrayList<SpaceInfoBean>();
	public static String getId() {
		return id;
	}
	public static void setId(String id) {
		OwnerInfo.id = id;
	}
	public static String getPhone() {
		return phone;
	}
	public static void setPhone(String phone) {
		OwnerInfo.phone = phone;
	}
	public static ArrayList<SpaceInfoBean> getList() {
		return list;
	}
	public static void setList(ArrayList<SpaceInfoBean> list) {
		OwnerInfo.list = list;
	}
	
	public static void initializeList(List<AVObject> obj) {
		// TODO Auto-generated method stub
		if(!OwnerInfo.list.isEmpty())
			OwnerInfo.list.clear();
		for(int i = 0; i < obj.size(); i++){
			SpaceInfoBean bean = new SpaceInfoBean();
			bean.setSpaceid(obj.get(i).getObjectId());
			bean.setOwnerid(obj.get(i).getString("ownerid"));
			bean.setCommunityid(obj.get(i).getString("communityid"));
			bean.setLat(obj.get(i).getInt("lat"));
			bean.setLng(obj.get(i).getInt("lng"));
			bean.setNum(obj.get(i).getInt("num"));
			bean.setPrice(obj.get(i).getInt("price"));
			bean.setFine(obj.get(i).getInt("fine"));
			bean.setStart(obj.get(i).getInt("start"));
			bean.setEnd(obj.get(i).getInt("end"));
			bean.setState(obj.get(i).getInt("state"));
			OwnerInfo.list.add(bean);
		}
	}
}
