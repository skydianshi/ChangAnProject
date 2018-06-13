package com.changan.changanproject.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeGetter {

	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"HH:mm:ss.SSS");

	public static String getTimeString(long nTime) {
		Date date = new Date(nTime);
		return simpleDateFormat.format(date);
	}
}
