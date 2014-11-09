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

/**
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
}