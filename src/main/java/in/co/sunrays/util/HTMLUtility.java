
package in.co.sunrays.util;

import java.util.Collections;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import in.co.sunrays.beans.DropdownListBean;

/**
 * HTML Utility class to produce HTML contents like Dropdown List.
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */

public class HTMLUtility {

    /**
     * Create HTML SELECT list from MAP paramters values
     * 
     * @param name
     * @param selectedVal
     * @param map
     * @return
     */

    public static String getList(String name, String selectedVal,
            HashMap<String, String> map) {

        StringBuffer sb = new StringBuffer(
                //"<select style='style='width:173px; height:22px;' class='form-control' name='" + name + "'>");
        		"<select class='form-control' name='" + name + "'>");
        Set<String> keys = map.keySet();
        String val = null;
        
        boolean select=true;
        if (select) {

            sb.append("<option selected value=''>- - - - - - -Select- - - - - - - </option>");
        }
        for (String key : keys) {
            val = map.get(key);
            if (key.trim().equals(selectedVal)) {
                sb.append("<option selected value='" + key + "'>" + val
                        + "</option>");
               
            } else {
                sb.append("<option value='" + key + "'>" + val + "</option>");
            }
        }
        sb.append("</select>");
        return sb.toString();
    }

    /**
     * 
     * @param name
     * @param selectedVal
     * @param list
     * @return
     * Create HTML SELECT List from List parameter
     */
    public static String getList(String name, String selectedVal, List list) {
    	
        Collections.sort(list);

        List<DropdownListBean> dd = (List<DropdownListBean>) list;

        StringBuffer sb = new StringBuffer(
               // "<select style='width:173px; height:22px;' class='form-control' name='" + name + "'>");
        		"<select class='form-control' name='" + name + "'>");
        String key = null;
        String val = null;
        boolean select=true;
        if (select) {

            sb.append("<option selected value=''>- - - - - - -Select- - - - - - - </option>");
        }


        for (DropdownListBean obj : dd) {
            key = obj.getKey();
            val = obj.getValue();

            if (key.trim().equals(selectedVal)) {
            	
                sb.append("<option selected value='" + key + "'>" + val
                        + "</option>");
               // System.out.println("In if getlist");
            } else {
                sb.append("<option value='" + key + "'>" + val + "</option>");
               // System.out.println("In else getlist");
            }
        }
        sb.append("</select>");
        return sb.toString();
    }

    public static String getList(String name, String selectedVal,
            HashMap<String, String> map, boolean select) {

        StringBuffer sb = new StringBuffer(
              //  "<select style='width:173px; height:22px;' class='form-control' name='" + name + "'>");
        "<select class='form-control' name='" + name + "'>");
        Set<String> keys = map.keySet();
        String val = null;

        if (select) {

            sb.append("<option selected value=''>- - - - - - -Select- - - - - - - </option>");
        }

        for (String key : keys) {
            val = map.get(key);
            if (key.trim().equals(selectedVal)) {
                sb.append("<option selected value='" + key + "'>" + val
                        + "</option>");
                
            } else {
                sb.append("<option value='" + key + "'>" + val + "</option>");
               
            }
        }
        sb.append("</select>");
        return sb.toString();
    }

    public static String getInputErrorMessages(HttpServletRequest request) {

        Enumeration<String> e = request.getAttributeNames();

        StringBuffer sb = new StringBuffer("<UL>");
        String name = null;

        while (e.hasMoreElements()) {
            name = e.nextElement();
            if (name.startsWith("error.")) {
                sb.append("<LI class='error'>" + request.getAttribute(name)
                        + "</LI>");
            }
        }
        sb.append("</UL>");
        return sb.toString();
    }

    /**
     * Returns Error Message with HTML tag and CSS
     * 
     * @param request
     * @return
     */
    public static String getErrorMessage(HttpServletRequest request) {
        String msg = ServletUtility.getErrorMessage(request);
        if (!DataValidator.isNull(msg)) {
            msg = "<p class='st-error-header'>" + msg + "</p>";
        }
        return msg;
    }

    /**
     * Returns Success Message with HTML tag and CSS
     * 
     * @param request
     * @return
     */

    public static String getSuccessMessage(HttpServletRequest request) {
        String msg = ServletUtility.getSuccessMessage(request);
        if (!DataValidator.isNull(msg)) {
            msg = "<p class='st-success-header'>" + msg + "</p>";
        }
        return msg;
    }

    /**
     * Creates submit button if user has access permission.
     * 
     * @param label
     * @param access
     * @param request
     * @return
     */
    public static String getSubmitButton(String label, boolean access,
            HttpServletRequest request) {

        String button = "";

        if (access) {
            button = "<input type='submit' name='operation'    value='" + label
                    + "' >";
        }
        return button;
    }

}