/*
 * Author   wangheng 
 * Created  2005-5-28 & 15:17:15
 */
package javadev.core.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 说明：
 */
public class CurrentCalendar {
	private Calendar cal = Calendar.getInstance();

	public int getCurrentWeek() {
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	public int getCurrentHour() {
		return cal.get(Calendar.HOUR_OF_DAY);
	}
	public int getCurrentYear() {
		return cal.get(Calendar.YEAR);
	}
	public int getCurrentMonth() {
		return cal.get(Calendar.MONTH) + 1;
	}
	public int getCurrentDay() {
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	public String getCurrentDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		return formatter.format(new Date());
	}
	public String getCurrentUsualDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(new Date());
	}
}