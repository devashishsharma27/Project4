package in.co.sunrays.controller;

import java.io.IOException;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import in.co.sunrays.beans.UserBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.exception.DuplicateRecordException;

import in.co.sunrays.model.RoleModel;
import in.co.sunrays.model.UserModel;
import in.co.sunrays.util.DataUtility;
import in.co.sunrays.util.DataValidator;
import in.co.sunrays.util.ServletUtility;

/**
 * * User functionality Controller. Performs operation for add, update and get
 * User
 *
 * @author  Devashish Sharma
 * @version 1.0
 * @Copyright (c) SunilOS
 */

@WebServlet("/ctl/UserCtl")
public class UserCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(UserCtl.class);

	public UserCtl() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("----User Controller-----------");
		log.debug("Operation  " + request.getParameter("operation"));
		if (OP_EDIT.equalsIgnoreCase(request.getParameter("operation"))) {
			long id = Long.parseLong(request.getParameter("id"));
			UserBean bean = null;
			UserModel model = new UserModel();
			try {
				bean = model.findByPK(id);
			} catch (ApplicationException e) {
				ServletUtility.handleException(e, request, response);
				return;
			}
			request.setAttribute("bean", bean);
		}
		ServletUtility.forward(getView(), request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("----User Controller-----------");
		String operation = request.getParameter("operation");
		log.debug("Operation  " + operation);
		request.setAttribute("operation", operation);

		if (OP_CANCEL.equalsIgnoreCase(request.getParameter("operation"))) {
			log.debug("In Cancel Operation");
			ServletUtility.redirect(ORSView.USER_LIST_CTL, request, response);
			return;
		}

		boolean flag = validate(request);
		UserModel model = new UserModel();
		UserBean bean;
		bean = populateBean(request);
		try {
			if (flag) {
				if (OP_ADD.equalsIgnoreCase(request.getParameter("operation"))) {
					log.debug("In Add Operation");
					model.addUser(bean);
					request.setAttribute("addRecordMsg", "Record Has Been Successfully Added");
					log.info("Record Has Been Successfully Added");
					ServletUtility.forward(ServletUtility.trimRequestPattern(ORSView.USER_LIST_CTL), request,
							response);
				}
				if (OP_SAVE.equalsIgnoreCase(request.getParameter("operation"))) {
					log.debug("In Update Operation");
					model.updateUser(bean);
					request.setAttribute("updateRecordMsg", "Record Has Been Successfully Updated");
					log.info("Record Has Been Successfully Updated");
					ServletUtility.forward(ServletUtility.trimRequestPattern(ORSView.USER_LIST_CTL), request,
							response);
				}

			} else {
				long userId = Long.parseLong(DataUtility.getString(request.getParameter("id")));
				bean = model.findByPK(userId);
				request.setAttribute("bean", bean);
				ServletUtility.forward(getView(), request, response);
			}
		} catch (ApplicationException e) {
			log.error("Application Exception", e);
			ServletUtility.handleException(e, request, response);
			return;
		} catch (DuplicateRecordException e) {
			ServletUtility.setBean(bean, request);
			log.error("User Login Id Already Exists");
			ServletUtility.setErrorMessage("User Login Id Already Exists", request);
			ServletUtility.forward(getView(), request, response);
			return;
		}
	}
	
	protected void preload(HttpServletRequest request) {
		log.debug("UserCtl Preload");
		RoleModel model = new RoleModel();
		request.setAttribute("roleList", model.roleNameList());
	}
	
	
	public boolean validate(HttpServletRequest request) {
		boolean flag = true;

		log.debug("UserCtl Validate");

		String operation = DataUtility.getString(request.getParameter("operation"));

		if (OP_CANCEL.equalsIgnoreCase(operation)) {
			return flag;
		}

		if (DataValidator.isNull(request.getParameter("fname"))) {
			flag = false;
			request.setAttribute("firstNameErr", "First Name is Required");
		} else if (!DataValidator.isValidName(DataUtility.getString(request.getParameter("fname")))) {
			flag = false;
			request.setAttribute("firstNameErr", "Please Enter Valid First Name");
		}

		if (DataValidator.isNull(request.getParameter("lname"))) {
			flag = false;
			request.setAttribute("lastNameErr", "Last Name is Required");
		} else if (!DataValidator.isValidName(DataUtility.getString(request.getParameter("lname")))) {
			flag = false;
			request.setAttribute("lastNameErr", "Please Enter Valid Last Name");
		}

		if (DataValidator.isNull(request.getParameter("loginId"))) {
			flag = false;
			request.setAttribute("loginErr", "Login Id is Required");
		} else if (!DataValidator.isEmail(DataUtility.getString(request.getParameter("loginId")))) {
			flag = false;
			request.setAttribute("loginErr", "Please Enter Valid Login Id (abc@pqr.xyz)");
		}

		if (DataValidator.isNull(request.getParameter("pwd"))) {
			flag = false;
			request.setAttribute("pwdErr", "Password is Required");
		}

		if (DataValidator.isNull(request.getParameter("cpwd"))) {
			flag = false;
			request.setAttribute("cpwdErr", "Confirm Password is Required");
		}
		if (!request.getParameter("pwd").equals(request.getParameter("cpwd"))) {
			request.setAttribute("cpwdErr", "Password and Confirm Password should be Same");
			flag = false;
		}

		if (DataValidator.isNull(request.getParameter("mobNo"))) {
			flag = false;
			request.setAttribute("mobNoErr", "Mobile No is Required");
		} else if (!DataValidator.isValidMobNo(DataUtility.getString(request.getParameter("mobNo")))) {
			flag = false;
			request.setAttribute("mobNoErr", "Please Enter Valid 10 Digit Mobile Number");
		}

		if (DataValidator.isNull(request.getParameter("gender"))) {
			flag = false;
			request.setAttribute("genderErr", "Gender is Required");
		}

		if (DataValidator.isNull(request.getParameter("dob"))) {
			flag = false;
			request.setAttribute("dobErr", "Date of Birth is Required");
		} else if (!DataValidator.isDate(DataUtility.getString(request.getParameter("dob")))) {
			flag = false;
			request.setAttribute("dobErr", "Please Enter Valid Date of Birth");
		}
		return flag;
	}

	public UserBean populateBean(HttpServletRequest request) {

		log.debug("UserCtl PopulateBean");
		
		UserBean bean  = new UserBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setFirstName(DataUtility.getString(request.getParameter("fname")).toUpperCase());
		bean.setLastName(DataUtility.getString(request.getParameter("lname")).toUpperCase());
		bean.setLoginId(DataUtility.getString(request.getParameter("loginId")));
		bean.setPassword(DataUtility.getString(request.getParameter("pwd")));
		bean.setRoleId(DataUtility.getLong(request.getParameter("roleId")));
		bean.setGender(DataUtility.getString(request.getParameter("gender")));
		bean.setDob(DataUtility.getDate(request.getParameter("dob")));
		bean.setMobNo(DataUtility.getString(request.getParameter("mobNo")));	
		
		
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");

		if (OP_ADD.equalsIgnoreCase(request.getParameter("operation"))) {
			bean.setCreatedBy(String.valueOf(user.getId()));
			bean.setModifiedBy(String.valueOf(user.getId()));
			bean.setCreatedDateTime(new Timestamp(new Date().getTime()));
			bean.setModifiedDateTime(new Timestamp(new Date().getTime()));
		}

		if (OP_SAVE.equalsIgnoreCase(request.getParameter("operation"))) {
			bean.setModifiedBy(String.valueOf(user.getId()));
			bean.setModifiedDateTime(new Timestamp(new Date().getTime()));
		}

		return bean;
	}

	protected String getView() {
		return ORSView.USER_VIEW;
	}

	public static String getDateFormat(Object dob) {
		Date date = (Date) dob;
		return DataUtility.getDateFormat(date);
	}

}
