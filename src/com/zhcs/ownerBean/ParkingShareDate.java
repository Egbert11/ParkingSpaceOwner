package com.zhcs.ownerBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 
 * @author huangjiaming
 *	��¼������λʱ�Ĺ���λʱ��
 */
public class ParkingShareDate {

	private static List<Calendar> shareDate = new ArrayList<Calendar>();

	public static List<Calendar> getShareDate() {
		return shareDate;
	}

	public static void setShareDate(List<Calendar> shareDate) {
		ParkingShareDate.shareDate = shareDate;
	}

}
