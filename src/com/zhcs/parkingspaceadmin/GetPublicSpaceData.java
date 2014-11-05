/*
 * 车位主获取自身已发布车位信息
 */
package com.zhcs.parkingspaceadmin;

import java.util.List;
import android.util.Log;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.zhcs.ownerBean.OwnerInfo;

/*
 * 获取车位主已发布的车位信息
 */
public class GetPublicSpaceData{
	public static void getSpaceInfo(String ObjectId) {
		AVQuery<AVObject> query = new AVQuery<AVObject>("SpaceInfo");
		query.whereEqualTo("ownerid", ObjectId);
		
		query.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> arg0, AVException e) {
				if (e == null) {
		            Log.d("获取已发布车位数据", "查询到" + arg0.size() + " 条符合条件的数据");
		            OwnerInfo.initializeList(arg0);
		        } else {
		            Log.d("获取已发布车位数据", "查询错误: " + e.getMessage());
		        }
			}
		});
	}
	
//	public static void getSpace(String ObjectId) {
//		AVQuery<AVObject> query = new AVQuery<AVObject>("SpaceInfo");
//		query.whereEqualTo("ownerid", ObjectId);
//		
//		query.findInBackground(new FindCallback<AVObject>() {
//			@Override
//			public void done(List<AVObject> arg0, AVException e) {
//				if (e == null) {
//		            Log.d("获取已发布车位数据", "查询到" + arg0.size() + " 条符合条件的数据");
//		            OwnerInfo.initializeList(arg0);
//		        } else {
//		            Log.d("获取已发布车位数据", "查询错误: " + e.getMessage());
//		        }
//			}
//		});
//	}
}
	
//	public void run()
//	{
//		Map<String,String> map=new HashMap<String,String>();
//		map.put("id", ""+OwnerInfo.getId());
//		String url=HttpUtil.URL+"GetPublishSpaceInfo";
//		JSONObject obj;
//		String result = "";
//		JSONArray jArr = new JSONArray();
//		try {
//			obj = new JSONObject(HttpUtil.postRequest(url, map));
//			result = obj.getString("result");
//			jArr = obj.getJSONArray("addresses");
//			Log.v("array",jArr.toString());
////			list = (ArrayList<SpaceInfoBean>) obj.get("list").toString();
//		} catch (JSONException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		if(result.equals("succeed"))
//		{
//			OwnerInfo.initializeList(jArr);
//		}
//	}
//}