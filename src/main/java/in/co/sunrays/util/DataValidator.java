package in.co.sunrays.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataValidator {

	public static boolean isNull(String val) {
		if (val == null || val.trim().length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNotNull(String val) {
		return !isNull(val);
	}

	public static boolean isValidName(String name) {
		if (!(isNull(name))) {

			String regex = "^[a-zA-Z]*$";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(name);
			return m.matches();

			/*
			 * if (name.matches("^[a-zA-Z]$")) { return true; } else { return false; }
			 */
		} else {
			return false;
		}
	}

	public static boolean isInteger(String val) {
		if (!(isNull(val))) {
			try {
				int i = Integer.parseInt(val);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}

		} else {
			return false;
		}
	}

	public static boolean isLong(String val) {
		if (!(isNull(val))) {
			try {
				long i = Long.parseLong(val);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}

		} else {
			return false;
		}
	}

	public static boolean isValidMobNo(String mob) {
		if (!(isNull(mob))) {

			String regex = "(0/91)?[7-9][0-9]{9}";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(mob);
			return m.matches();

		} else {
			return false;
		}
	}

	public static boolean isEmail(String val) {
		if (!(isNull(val))) {
			if (val.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static boolean isDate(String date) {

		if (!(isNull(date))) {
			// for yyyy-mm-dd 
			//String dateRgx = "^(19|20)\\d\\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])$";
			// mm/dd/yyyy
			String dateRgx = "^(0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])[- /.](19|20)\\d\\d$";

			if (date.matches(dateRgx)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if value is valid Name
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isName(String val) {
		String namereg = "([A-Za-z]+)*";

		if (isNotNull(val)) {
			try {
				return val.matches(namereg);
			} catch (NumberFormatException e) {
				return false;
			}
		} else {
			return false;
		}

	}
	/**
	 * Checks if value is valid Email ID
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isMobileNumber(String val) {
		String mobilereg = "(0|91)?[6-9][0-9]{9}";

		if (isLong(val)) {
			try {
				return val.matches(mobilereg);
			} catch (NumberFormatException e) {
				return false;
			}
		} else {
			return false;
		}

	}

}