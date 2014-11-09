/*
 * ��λ����ȡ�����ѷ�����λ��Ϣ
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
 * ��ȡ��λ���ѷ����ĳ�λ��Ϣ
 */
public class GetPublicSpaceData{
	public static void getSpaceInfo(String ObjectId) {
		AVQuery<AVObject> query = new AVQuery<AVObject>("SpaceInfo");
		query.whereEqualTo("ownerid", ObjectId);
		
		query.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> arg0, AVException e) {
				if (e == null) {
		            Log.d("��ȡ�ѷ�����λ����", "��ѯ��" + arg0.size() + " ����������������");
		            OwnerInfo.initializeList(arg0);
		        } else {
		            Log.d("��ȡ�ѷ�����λ����", "��ѯ����: " + e.getMessage());
		        }
			}
		});
	}
}