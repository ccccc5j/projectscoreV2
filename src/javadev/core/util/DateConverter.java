package javadev.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import ognl.DefaultTypeConverter;

import com.opensymphony.xwork2.XWorkException;

/**
 * 用于WebWork自动转化String to Date
 */
public class DateConverter extends DefaultTypeConverter {
	private static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public Object convertValue(Map ognlContext, Object value, Class toType) {
		Object result = null;
		if (toType == Date.class) {
			result = doConvertToDate(value);
		} else if (toType == String.class) {
			result = doConvertToString(value);
		}
		return result;
	}

	private Date doConvertToDate(Object value) {
		Date result = null;
		if (value instanceof String) {
			String strValue = (String) value;
			try {
				if (strValue.indexOf(":") == -1) {
					result = sdfDate.parse(strValue);
				} else {
					result = sdfDateTime.parse(strValue);
				}
			} catch (ParseException e) {
				throw new XWorkException("Could not parse date", e);
			}
		} else if (value instanceof Object[]) {
			// let's try to convert the first element only
			Object[] array = (Object[]) value;
			if ((array != null) && (array.length >= 1)) {
				value = array[0];
				result = doConvertToDate(value);
			}
		} else if (Date.class.isAssignableFrom(value.getClass())) {
			result = (Date) value;
		}
		return result;
	}

	private String doConvertToString(Object value) {
		String result = null;
		if (value instanceof Date) {
			result = sdfDateTime.format(value);
		}
		return result;
	}

}
