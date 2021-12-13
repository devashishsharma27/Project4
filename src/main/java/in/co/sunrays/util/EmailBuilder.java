package in.co.sunrays.util;

import java.util.HashMap;

public class EmailBuilder {

	public static String getForgetPasswordMessage(HashMap<String, String> map) {
        StringBuilder msg = new StringBuilder();

        msg.append("<HTML><BODY>");
        msg.append("<H1>Your password is reccovered !! " + map.get("firstName")
                + " " + map.get("lastName") + "</H1>");
        /*
         * msg.append("<P>To access account user login ID : " + map.get("login")
         * + " and password " + map.get("password") + "</P>");
         */
        msg.append("<P><B>To access account user Login Id : "
                + map.get("login") + "<BR>" + " Password : "
                + map.get("password") + "</B></p>");
        msg.append("</BODY></HTML>");

        return msg.toString();
    }
	
	
	 public static String getChangePasswordMessage(HashMap<String, String> map) {
	        StringBuilder msg = new StringBuilder();

	        msg.append("<HTML><BODY>");
	        msg.append("<H1>Your Password has been changed Successfully !! "
	                + map.get("firstName") + " " + map.get("lastName") + "</H1>");
	        /*
	         * msg.append("<P>To access account user login ID : " + map.get("login")
	         * + " and password " + map.get("password") + "</P>");
	         */
	        msg.append("<P><B>To access account user Login Id : "
	                + map.get("login") + "<BR>" + " Password : "
	                + map.get("password") + "</B></p>");
	        msg.append("</BODY></HTML>");

	        return msg.toString();
	    }

}
