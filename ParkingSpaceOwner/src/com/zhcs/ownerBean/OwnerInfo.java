package com.zhcs.ownerBean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
public class OwnerInfo {
	private static int id;
	private static String phone;
	private static ArrayList<SpaceInfoBean> list = new ArrayList<SpaceInfoBean>();
	public static int getId() {
		return id;
	}
	public static void setId(int id) {
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
	public static void initializeList(JSONArray jArr) {
		// TODO Auto-generated method stub
		if(!OwnerInfo.list.isEmpty())
			OwnerInfo.list.clear();
		for(int i = 0; i < jArr.length(); i++){
			SpaceInfoBean bean = new SpaceInfoBean();
			try {
				bean.setSpace_id(jArr.getJSONObject(i).getInt("space_id"));
				bean.setOwner_id(jArr.getJSONObject(i).getInt("owner_id"));
				bean.setLat(jArr.getJSONObject(i).getInt("lat"));
				bean.setLng(jArr.getJSONObject(i).getInt("lng"));
				bean.setNum(jArr.getJSONObject(i).getInt("num"));
				bean.setPrice(jArr.getJSONObject(i).getInt("price"));
				bean.setStart(jArr.getJSONObject(i).getInt("start"));
				bean.setEnd(jArr.getJSONObject(i).getInt("end"));
				bean.setState(jArr.getJSONObject(i).getInt("state"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			OwnerInfo.list.add(bean);
		}
	}
}
