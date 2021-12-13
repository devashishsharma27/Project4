package in.co.sunrays.util;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.sunrays.beans.BaseBean;
import in.co.sunrays.controller.BaseCtl;
import in.co.sunrays.controller.ORSView;

public class ServletUtility {

	public static void forward(String page, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		RequestDispatcher rd = request.getRequestDispatcher(page);
		rd.forward(request, response);
	}

	public static void redirect(String page, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.sendRedirect(page);
	}

	public static String trimRequestPattern(String page) {
	//	System.out.println("Old URL " + page);
		int length = ORSView.APP_CONTEXT.length();
		page = page.substring(length, page.length());
	//	System.out.println("New URL is  " + page);
		return page;
	}

	public static int getPageSize() {
		ResourceBundle rbObj = ResourceBundle.getBundle("config");
		return DataUtility.getInt(rbObj.getString("page.size"));

	}

	public static void handleException(Exception e, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		request.setAttribute("exception", e);
		RequestDispatcher rd = request.getRequestDispatcher(trimRequestPattern(ORSView.ERROR_CTL));
		rd.forward(request, response);
	}

	public static void setBean(BaseBean bean, HttpServletRequest request) {
		request.setAttribute("bean", bean);
	}

	public static void setSuccessMessage(String msg, HttpServletRequest request) {
		request.setAttribute(BaseCtl.MSG_SUCCESS, msg);
	}

	public static void setErrorMessage(String msg, HttpServletRequest request) {
		request.setAttribute(BaseCtl.MSG_ERROR, msg);

	}
	
	public static String getErrorMessage(HttpServletRequest request) {
		String val = (String) request.getAttribute(BaseCtl.MSG_ERROR);
		if (val == null) {
			return "";
		} else {
			return val;
		}
	}
	
	public static String getSuccessMessage(HttpServletRequest request) {
		String val = (String) request.getAttribute(BaseCtl.MSG_SUCCESS);
		if (val == null) {
			return "";
		} else {
			return val;
		}
	}
	
	
	public static final String OP_SEARCH = "Search";
	public static final String OP_PREVIOUS = "Previous";
	public static final String OP_NEXT = "Next";
	
	public static int getPageNo(HttpServletRequest request) {
		int pageNo = 1;
		
		String operation = request.getParameter("operation");

		if (request.getParameter("pageNo") == null || request.getParameter("operation") == null
				|| OP_SEARCH.equalsIgnoreCase(operation)) {
			pageNo = 1;
		} else {
			pageNo = Integer.parseInt(request.getParameter("pageNo"));
		}

		if (OP_PREVIOUS.equalsIgnoreCase(operation)) {
			pageNo = (pageNo > 1) ? pageNo - 1 : 1;
		}

		if (OP_NEXT.equalsIgnoreCase(operation)) {
			int recordCount = Integer.parseInt(request.getParameter("recordCount"));
			pageNo = (recordCount < 10) ? pageNo : pageNo + 1;
		}
		request.setAttribute("pageNo", pageNo);
		return pageNo;
	}
	
	/**
	 * Sets default List to request
	 * 
	 * @param list
	 * @param request
	 */
	public static void setList(List list, HttpServletRequest request) {
		request.setAttribute("list", list);
	}

	/**
	 * Gets default list from request
	 * 
	 * @param request
	 * @return
	 */
	public static List getList(HttpServletRequest request) {
		return (List) request.getAttribute("list");
	}

	/**
	 * Sets Page Number for List pages
	 * 
	 * @param pageNo
	 * @param request
	 */
	public static void setPageNo(int pageNo, HttpServletRequest request) {
		request.setAttribute("pageNo", pageNo);
	}

	/**
	 * Gets Page Number for List pages
	 * 
	 * @param request
	 * @return
	 */
	/*
	 * public static int getPageNo(HttpServletRequest request) { return (Integer)
	 * request.getAttribute("pageNo"); }
	 */

	/**
	 * Sets Page Size for list pages
	 * 
	 * @param pageSize
	 * @param request
	 */
	public static void setPageSize(int pageSize, HttpServletRequest request) {
		request.setAttribute("pageSize", pageSize);
	}

	/**
	 * Gets Page Size for List pages
	 * 
	 * @param request
	 * @return
	 */
	public static int getPageSize(HttpServletRequest request) {
		return (Integer) (request.getAttribute("pageSize"));
	}
	
	public static String getErrorMessage(String property, HttpServletRequest request) {
		String val = (String) request.getAttribute(property);
		if (val == null) {
			return "";
		} else {
			return val;
		}
	}
	public static String getParameter(String property, HttpServletRequest request) {
		String val = (String) request.getParameter(property);
		if (val == null) {
			return "";
		} else {
			return val;
		}
	}
	
	public static String getDateFormat(Object dob) { Date date = (Date)dob ;
	  return DataUtility.getDateFormat(date); }
}
