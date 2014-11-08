package com.zhcs.ownerBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 
 * @author huangjiaming
 *	记录发布车位时的共享车位时间
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
