package in.co.sunrays.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import in.co.sunrays.beans.UserBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.model.UserModel;
import in.co.sunrays.util.DataUtility;
import in.co.sunrays.util.ServletUtility;
/**
 * User List functionality Controller. Performs operation for list, search and
 * delete operations of User
 *
 * @author  Devashish Sharma
 * @version 1.0
 * @Copyright (c) SunilOS
 */

@WebServlet("/ctl/UserListCtl")
public class UserListCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(UserListCtl.class);    
    
    public UserListCtl() {
        super();       
    }
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
    	System.out.println("----User List Controller-----------");
    	
    	UserModel model = new UserModel();
		ArrayList<UserBean> list = null;
		int pageNo = 1;
		try {
			if (request.getParameter("pageNo") != null) {
				pageNo = Integer.parseInt(request.getParameter("pageNo"));
			}
			list = model.userList(pageNo, ServletUtility.getPageSize());
			request.setAttribute("list", list);
			request.setAttribute("pageNo", 1);
			request.setAttribute("pageSize", ServletUtility.getPageSize());
			request.setAttribute("recordCount", list.size());			
			ServletUtility.forward(getView(), request, response);
			return;
		} catch (ApplicationException e) {
			e.printStackTrace();
			ServletUtility.handleException(e, request, response);
			return;
		}
	}

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
    	System.out.println("----User List Controller-----------");
		ArrayList<UserBean> list = null;
		UserModel model = new UserModel();
		UserBean bean = new UserBean();
		
		String operation = request.getParameter("operation");
		log.debug("Operation " + operation);
		request.setAttribute("operation", operation);
		try {
			int pageNo = ServletUtility.getPageNo(request);
			int pageSize = ServletUtility.getPageSize();
			
			if (OP_ADD.equalsIgnoreCase(operation)) {
				if (request.getAttribute("addRecordMsg") == null) {				
					ServletUtility.redirect(ORSView.USER_CTL, request, response);
					return;
				}
				list = model.userList(pageNo, pageSize);
			}
			else if (OP_DELETE.equalsIgnoreCase(operation)) {
				String[] ids = request.getParameterValues("id");
				if (ids != null && ids.length > 0) {
				//	System.out.println("user ids " + ids.toString());
					if (model.deleteUsers(ids)) {
						request.setAttribute("deleteRecordMsg", "Record(s) Successfully Deleted");
					} else {
						ServletUtility.setErrorMessage("Record(s) Cannot be Deleted", request);
					}
				}else{
					ServletUtility.setErrorMessage("Select at least One Record", request);
				}
				list = model.userList(pageNo, pageSize);
			}else if (OP_SEARCH.equalsIgnoreCase(operation) || OP_PREVIOUS.equalsIgnoreCase(operation)
					|| OP_NEXT.equalsIgnoreCase(operation)) {

				request.setAttribute("firstNameSearch", request.getParameter("fname"));
				request.setAttribute("lastNameSearch", request.getParameter("lname"));
				request.setAttribute("loginSearch", request.getParameter("loginId"));
				
				
				bean = populateBean(request);				
				list = model.search(bean, pageNo, pageSize);

				if (list == null || list.size() == 0) {
					ServletUtility.setErrorMessage("No Record Found", request);
				}
			}
			else {
				list = model.userList(pageNo, pageSize);
			}
		} catch (ApplicationException e) {
			log.error("Exception in " + operation +" Operation", e);
			ServletUtility.handleException(e, request, response);
			return;
		}		
		request.setAttribute("list", list);
		request.setAttribute("recordCount", list.size());
		request.setAttribute("pageSize", ServletUtility.getPageSize());
		ServletUtility.forward(getView(), request, response);
		return;
	}


	@Override
	protected String getView() {
		return ORSView.USER_LIST_VIEW;
	}
	
	protected boolean validate(HttpServletRequest request) {
		log.debug("UserListCtl Validate");
		return true;
	}
	
	protected void preload(HttpServletRequest request) {
		log.debug("UserListCtl Preload");		
	}

	public UserBean populateBean(HttpServletRequest request) {
		log.debug("UserListCtl populateBean");
		UserBean bean  = new UserBean();			
		bean.setFirstName(DataUtility.getString(request.getParameter("fname")).toUpperCase());
		bean.setLastName(DataUtility.getString(request.getParameter("lname")).toUpperCase());
		bean.setLoginId(DataUtility.getString(request.getParameter("loginId")));
		bean.setPassword(DataUtility.getString(request.getParameter("pwd")));
		bean.setRoleId(DataUtility.getLong(request.getParameter("roleId")));
		bean.setGender(DataUtility.getString(request.getParameter("gender")));
		bean.setDob(DataUtility.getDate(request.getParameter("dob")));
		bean.setMobNo(DataUtility.getString(request.getParameter("mobNo")));
		bean.setCreatedBy(request.getParameter("loginId"));
		bean.setModifiedBy(request.getParameter("loginId"));
		bean.setCreatedDateTime(new Timestamp(new Date().getTime()));
		bean.setModifiedDateTime(new Timestamp(new Date().getTime()));		
		return bean;
	}
	

}
