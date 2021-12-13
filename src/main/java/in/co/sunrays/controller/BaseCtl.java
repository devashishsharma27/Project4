package in.co.sunrays.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.sunrays.beans.BaseBean;
import in.co.sunrays.util.DataUtility;
import in.co.sunrays.util.DataValidator;
import in.co.sunrays.util.ServletUtility;

public abstract class BaseCtl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static final String OP_SAVE = "Save";
	public static final String OP_UPDATE = "Update";
	public static final String OP_CANCEL = "Cancel";
	public static final String OP_RESET = "Reset";
	public static final String OP_DELETE = "Delete";
	public static final String OP_LIST = "List";
	public static final String OP_SEARCH = "Search";
	public static final String OP_VIEW = "View";
	public static final String OP_NEXT = "Next";
	public static final String OP_PREVIOUS = "Previous";
	public static final String OP_NEW = "New";
	public static final String OP_GO = "Go";
	public static final String OP_BACK = "Back";
	public static final String OP_LOG_OUT = "Logout";  
	
	
	
	public static final String OP_EDIT = "Edit";
	public static final String OP_ADD = "Add"; 
	
	public static final String MSG_SUCCESS = "success";
	public static final String MSG_ERROR = "error";

	protected abstract String getView();

	protected boolean validate(HttpServletRequest request) {

		return true;
	}

	protected void preload(HttpServletRequest request) {

	}

	protected BaseBean populateBean(HttpServletRequest request) {
		return null;
	}

	protected BaseBean populateDTO(BaseBean dto, HttpServletRequest request) {
		return dto;

	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		System.out.println("----Base Controller-----------");
		preload(request);
		String operation = DataUtility.getString(request.getParameter("operation"));
		if (DataValidator.isNotNull(operation) && !OP_CANCEL.equalsIgnoreCase(operation)
				&& !OP_VIEW.equalsIgnoreCase(operation) && !OP_DELETE.equalsIgnoreCase(operation)
				&& !OP_EDIT.equalsIgnoreCase(operation)) {
			if (!validate(request)) {
				System.out.println("Validation Failed");
				BaseBean bean = (BaseBean) populateBean(request);
				ServletUtility.setBean(bean, request);
				request.setAttribute("requestedURI", request.getParameter("requestedURI"));
				ServletUtility.forward(getView(), request, response);
				return;
			}
		}
		super.service(request, response);

	}

}
