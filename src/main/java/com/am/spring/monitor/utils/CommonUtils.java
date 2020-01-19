package com.am.spring.monitor.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class CommonUtils {
	
	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private CommonUtils() {
	}

	

public static boolean isEmpty(final String str) {
    return str == null || str.length() == 0;
}

public static boolean isEmpty(final Object[] array) {
    return array == null || array.length == 0;
}

public static boolean isEmpty(final Collection<?> collection) {
    return collection == null || collection.isEmpty();
}

public static boolean isEmpty(final Map<?, ?> map) {
    return map == null || map.isEmpty();
}


public static String Date2String(Date date)
{
	return format.format(date);
}

public static Date String2Date(String sdate)
{
	try {
		return format.parse(sdate);
	} catch (ParseException e) {
	
		e.printStackTrace();
	}
	return null;
}



public static Long dateDifferenceinLong (String d1, String d2)
{
	Calendar c1 = Calendar.getInstance();
	c1.setTime(String2Date(d1));
	
	Calendar c2 = Calendar.getInstance();
	c2.setTime(String2Date(d2));
	
	return (c2.getTimeInMillis() - c1.getTimeInMillis()/1000);

}



}
