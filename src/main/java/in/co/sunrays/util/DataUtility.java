package in.co.sunrays.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

public class DataUtility {

//	public static final String APP_DATE_FORMAT = "yyyy-MM-dd";
	public static final String APP_DATE_FORMAT = "MM/dd/yyyy";
	public static final String APP_TIME_FORMAT = "MM/dd/yyyy HH:mm:ss";
	private static final SimpleDateFormat timeFormatter = new SimpleDateFormat(APP_TIME_FORMAT);
	private static final SimpleDateFormat formatter = new SimpleDateFormat(APP_DATE_FORMAT);
	public static DataValidator validator = new DataValidator();

	public static int getInt(String string) {
		if (DataValidator.isInteger(string)) {
			return Integer.parseInt(string);
		} else {
			return 0;
		}
	}

	public static String getString(String str) {
		if (validator.isNull(str)) {
			return str;
		} else {
			return str.trim();
		}
	}

	public static Date getDate(String str) {
		Date date = null;
		
		try {
			if (str != null && str.length()>0) {
				SimpleDateFormat formatter = new SimpleDateFormat(APP_DATE_FORMAT);
				date = formatter.parse(str);
			}
		} catch (Exception e) {
			System.out.println("Exception :: String Cannot be Parse Into Date");
			e.printStackTrace();
		}
		return date;
	}

	public static String getDateFormat(Date date) {
		String dob = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(APP_DATE_FORMAT);
			dob = formatter.format(date);
		} catch (Exception e) {
			System.out.println("Exception :: Date cannot be formatted into String");
			e.printStackTrace();
		}
		return dob;
	}

	public static long getLong(String val) {
		if (DataValidator.isLong(val)) {
			return Long.parseLong(val);
		} else {
			return 0;
		}
	}
	
	
	public static Timestamp getTimestamp(String val) {

		Timestamp timeStamp = null;
		try {
			timeStamp = new Timestamp((timeFormatter.parse(val)).getTime());
		} catch (Exception e) {
			return null;
		}
		return timeStamp;
	}
	
	public static long getTimestamp(Timestamp tm) {
		try {
			return tm.getTime();
		} catch (Exception e) {
			return 0;
		}
	}
	
	
	public static String getStringData(Object val) {
		if (val != null) {
			return val.toString();
		} else {
			return "";
		}
	}
	
	/**
	 * Converts Date into String
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateString(Date date) {
		try {
			return formatter.format(date);
		} catch (Exception e) {
		}
		return "";
	}

	
}
