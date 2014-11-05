package com.zhcs.parkingspaceadmin;

import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.zhcs.ownerBean.Community;
import com.zhcs.ownerBean.CommunityInfoBean;
import com.zhcs.regAndLog.R;
import com.zhcs.regAndLog.Register;

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class BaiduMapForLatLng extends Activity{
	/**
	 *  MapView �ǵ�ͼ���ؼ�
	 */
	private MapView mMapView = null;
	/**
	 *  ��MapController��ɵ�ͼ���� 
	 */
	private MapController mMapController = null;
	/**
	 *  MKMapViewListener ���ڴ����ͼ�¼��ص�
	 */
	MKMapViewListener mMapListener = null;
	
	String tag = "������λ��ͼ��ʾ";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * ʹ�õ�ͼsdkǰ���ȳ�ʼ��BMapManager.
         * BMapManager��ȫ�ֵģ���Ϊ���MapView���ã�����Ҫ��ͼģ�鴴��ǰ������
         * ���ڵ�ͼ��ͼģ�����ٺ����٣�ֻҪ���е�ͼģ����ʹ�ã�BMapManager�Ͳ�Ӧ������
         */
        DemoApplication app = (DemoApplication)this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(getApplicationContext());
            /**
             * ���BMapManagerû�г�ʼ�����ʼ��BMapManager
             */
            app.mBMapManager.init(new DemoApplication.MyGeneralListener());
        }
        /**
          * ����MapView��setContentView()�г�ʼ��,��������Ҫ��BMapManager��ʼ��֮��
          */
        setContentView(R.layout.activity_baidumapforlatlng);
        /*
         * ��ȡС����Ϣ
         */
        AVQuery<AVObject> query = new AVQuery<AVObject>("Community");
		query.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> arg0, AVException e) {
				if (e == null) {
		            Log.d(tag, "��ѯ��" + arg0.size() + " ��С������");
		            int size = arg0.size();
		            if(size > 0) {
		            	//����С����Ϣ
		            	Community.setList(arg0);
		            }
		            initCommunity();
		        } else {
		            Log.d("ʧ��", "��ѯ����: " + e.getMessage());
		        }
			}
		});
        mMapView = (MapView)findViewById(R.id.bmapView);
        /**
         * ��ȡ��ͼ������
         */
        mMapController = mMapView.getController();
        /**
         *  ���õ�ͼ�Ƿ���Ӧ����¼�  .
         */
        mMapController.enableClick(false);
        /**
         * ���õ�ͼ���ż���
         */
        mMapController.setZoom(12);
       
        /**
         * ����ͼ�ƶ���ָ����
         * ʹ�ðٶȾ�γ�����꣬����ͨ��http://api.map.baidu.com/lbsapi/getpoint/index.html��ѯ��������
         * �����Ҫ�ڰٶȵ�ͼ����ʾʹ����������ϵͳ��λ�ã��뷢�ʼ���mapapi@baidu.com��������ת���ӿ�
         */
        GeoPoint p ;
        Intent  intent = getIntent();
        if ( intent.hasExtra("x") && intent.hasExtra("y") ){
        	//����intent����ʱ���������ĵ�Ϊָ����
        	Bundle b = intent.getExtras();
        	p = new GeoPoint(b.getInt("y"), b.getInt("x"));
        }else{
        	p =new GeoPoint((int)(39.915507* 1E6),(int)(116.408042* 1E6));
        }
        
        mMapController.setCenter(p);
        
        /**
         * ��ʾС����λ��Ϣ
         */
        initCommunity();
        
        /**
    	 *  MapView������������Activityͬ������activity����ʱ�����MapView.onPause()
    	 */
        mMapListener = new MKMapViewListener() {
			@Override
			public void onMapMoveFinish() {
				/**
				 * �ڴ˴����ͼ�ƶ���ɻص�
				 * ���ţ�ƽ�ƵȲ�����ɺ󣬴˻ص�������
				 */
			}
			
			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				/**
				 * �ڴ˴����ͼpoi����¼�
				 * ��ʾ��ͼpoi���Ʋ��ƶ����õ�
				 * ���ù��� mMapController.enableClick(true); ʱ���˻ص����ܱ�����
				 * 
				 */
				/*final String title;
				if (mapPoiInfo != null){
					title = mapPoiInfo.strText;
					GeoPoint point = mapPoiInfo.geoPt;
					final int lat = point.getLatitudeE6();
					final int lng = point.getLongitudeE6();
					Toast.makeText(BaiduMapForLatLng.this,"lat:"+lat+" lng:"+lng,Toast.LENGTH_SHORT).show();
					mMapController.animateTo(mapPoiInfo.geoPt);
					//���ѳ�λ���Ƿ񽫵�ǰλ����Ϊ��λ����λ��
					new AlertDialog.Builder(BaiduMapForLatLng.this).setTitle("ѡȡ��λ����λ��").setMessage("ȷ����"+title+"����Ϊ��λ����λ�ã�")
					.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//����γ�ȴ��ظ�ParkingPublic.class
							Bundle bundle = new Bundle();
							bundle.putInt("lat", lat);
							bundle.putInt("lng", lng);
							bundle.putString("title",title);
							Intent intent = new Intent(BaiduMapForLatLng.this, ParkingPublish.class);
							intent.putExtras(bundle);
							startActivity(intent);
							finish();
						};
							}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
									
								}
							}).show();
				}*/
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {
				/**
				 *  �����ù� mMapView.getCurrentMap()�󣬴˻ص��ᱻ����
				 *  ���ڴ˱����ͼ���洢�豸
				 */
			}

			@Override
			public void onMapAnimationFinish() {
				/**
				 *  ��ͼ��ɴ������Ĳ�������: animationTo()���󣬴˻ص�������
				 */
			}
            /**
             * �ڴ˴����ͼ������¼� 
             */
			@Override
			public void onMapLoadFinish() {
				Toast.makeText(BaiduMapForLatLng.this, 
						       "��ͼ�������", 
						       Toast.LENGTH_SHORT).show();
				
			}
		};
		mMapView.regMapViewListener(DemoApplication.getInstance().mBMapManager, mMapListener);
    }
    
    /*
     * �ڵ�ͼ����ʾС��maker
     */
    private void initCommunity() {
    	Log.v(tag, ""+Community.getList().size());
    	if(Community.getList().isEmpty()) {
    		mMapView.getOverlays().clear();
            mMapView.refresh();
    		return;
    	}
    	ArrayList<CommunityInfoBean> list = Community.getList();
		Drawable drawable = getResources().getDrawable(R.drawable.icon_maker);
    	MyOverlay overlay = new MyOverlay(BaiduMapForLatLng.this, drawable, mMapView);
    	for(int i = 0; i < list.size(); i++){
        	OverlayItem overlayItem = new OverlayItem(new GeoPoint(list.get(i).getLatitude().intValue(), list.get(i).getLongitude().intValue()), 
        			"��ַ��"+list.get(i).getAddress(), String.valueOf(i));
        	overlayItem.setMarker(drawable);
        	overlay.addItem(overlayItem);
    	}
    	mMapView.getOverlays().clear();
        mMapView.getOverlays().add(overlay);
        mMapView.refresh();
    }
}
