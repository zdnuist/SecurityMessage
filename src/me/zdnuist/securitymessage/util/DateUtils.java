package me.zdnuist.securitymessage.util;

import java.text.SimpleDateFormat;
import java.util.Date;



public class DateUtils {
	
	private DateUtils(){}
	
	public static String formateDate(String timeText){
		Date date = new Date(Long.valueOf(timeText));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

}
