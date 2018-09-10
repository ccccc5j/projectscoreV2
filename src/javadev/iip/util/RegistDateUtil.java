package javadev.iip.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javadev.core.util.MapUtil;

public class RegistDateUtil {
	

	
	/**
	 * 给定一个String时间值，给地一个月份，在原来时间的基础上，增加给定的月份
	 * 返回String时间值
	 */
	public static String dateADDmonth(String stringdate,int month){
		
		try {
			//字符串转化为date
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = sdf.parse(stringdate);
			
			//date转化Calendar  在增加相应的月份
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date1);
			calendar.add(Calendar.MONTH,month);
			Date date2 = calendar.getTime();
			
			//最后格式化
			DateFormat dateFormat = DateFormat.getDateInstance();
			return dateFormat.format(date2);
			
			
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	/**
	 * 给地一个String时间值，给地一个月份，在原来时间的基础上，增加给定的天数
	 * 返回String时间值
	 */
	public static String dateADDday(String stringdate,int day){
		
		try {
			//字符串转化为date
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = sdf.parse(stringdate);
			
			//date转化Calendar  在增加相应的月份
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date1);
			calendar.add(Calendar.DAY_OF_YEAR,day);
			Date date2 = calendar.getTime();
			
			//最后格式化
			DateFormat dateFormat = DateFormat.getDateInstance();
			return dateFormat.format(date2);
			
			
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 字符串时间格式化  yyyy-mm-dd  格式化为   yyyy年mm月dd日
	 */
	public static String Stringformat(String strDate){
			//字符串转化为date
		int indexOf = strDate.indexOf("-");
		int lastIndexOf = strDate.lastIndexOf("-");
		strDate = strDate.replace(strDate.charAt(indexOf),'年');
		strDate = strDate.replace(strDate.charAt(lastIndexOf),'月');
		strDate += "日";
		
		return strDate;
	}
	
	
	/**
	 * 字符串转化为date
	 */
	public static Date String2Date(String strDate){
		try {
			//字符串转化为date
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(strDate);
			return date;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	

}
