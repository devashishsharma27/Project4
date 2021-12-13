package in.co.sunrays.controller;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import in.co.sunrays.beans.UserBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.model.RoleModel;
import in.co.sunrays.model.UserModel;
import in.co.sunrays.util.DataUtility;
import in.co.sunrays.util.DataValidator;
import in.co.sunrays.util.ServletUtility;

import javax.servlet.annotation.WebServlet;
import org.apache.log4j.Logger;

@WebServlet("/LoginCtl")
public class LoginCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;
	public static final String OP_SIGN_IN = "SignIn";
	public static final String OP_SIGN_UP = "SignUp";
	public static final String OP_LOG_OUT = "logout";

	private static Logger log = Logger.getLogger(LoginCtl.class);

	public LoginCtl() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String operation = DataUtility.getString(request.getParameter("operation"));
		log.debug("Operation :: " + operation);
		try {
		if (OP_SIGN_UP.equalsIgnoreCase(operation)) {
			ServletUtility.redirect(ORSView.USER_REGISTRATION_CTL, request, response);
		} else if (OP_LOG_OUT.equalsIgnoreCase(operation)) {
			HttpSession session = request.getSession();
			session.invalidate();
			log.debug("Logout Successfully");
			ServletUtility.forward(getView(), request, response);
		} else {
			ServletUtility.forward(getView(), request, response);
		} }catch(Exception e) {
			ServletUtility.handleException(e, request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
		String operation = DataUtility.getString(request.getParameter("operation"));
		log.debug("Operation :: " + operation);
	
		if (OP_SIGN_IN.equalsIgnoreCase(operation)) {

			UserModel modelObj = new UserModel();
			UserBean user = populateBean(request);
			user = modelObj.authenticate(user);
			
			if (user.getLoginId() != null && user.getPassword() != null) {

				HttpSession session = request.getSession(true);
				session.setAttribute("user", user);

				log.info("Authenication Passed");
				if (request.getParameter("requestedURI").equalsIgnoreCase("null")) {		
					ServletUtility.forward(ORSView.WELCOME_VIEW, request, response);
				}else {		
					ServletUtility.forward(ServletUtility.trimRequestPattern(request.getParameter("requestedURI")), request, response);				
				}
			} else {
				
				request.setAttribute("NoRecordErr", "Invalid Username or Password");
				request.setAttribute("requestedURI", request.getParameter("requestedURI"));
				log.info("Authenication Failed");				
				ServletUtility.forward(getView(), request, response);
			}
		} else {
			ServletUtility.forward(getView(), request, response);
		}
		}catch(ApplicationException e) {
			ServletUtility.handleException(e, request, response);
		}catch(Exception e) {
			ServletUtility.handleException(e, request, response);
		}
	}

	public boolean validate(HttpServletRequest request) {
		boolean flag = true;
		
		log.debug("LoginCtl Validate");
		String operation = DataUtility.getString(request.getParameter("operation"));

		if (OP_SIGN_UP.equalsIgnoreCase(operation) || OP_LOG_OUT.equalsIgnoreCase(operation)) {
			return flag;
		}
		if (DataUtility.getString(request.getParameter("userName")) == null
				|| DataUtility.getString(request.getParameter("userName")) == "") {
			flag = false;
			request.setAttribute("usernameErr", "Username is required");
		} else if (!DataValidator.isEmail(DataUtility.getString(request.getParameter("userName")))) {
			flag = false;
			request.setAttribute("usernameErr", "Please Enter Valid Username (abc@pqr.xyz)");
		}
		if (DataUtility.getString(request.getParameter("password")) == null
				|| DataUtility.getString(request.getParameter("password")) == "") {
			flag = false;
			request.setAttribute("passwordErr", "Password is required");
		}
		return flag;
	}

	public UserBean populateBean(HttpServletRequest request) {
		log.debug("LoginCtl PopulateBean");
		UserBean user = new UserBean();
		user.setLoginId(DataUtility.getString(request.getParameter("userName")));
		user.setPassword(DataUtility.getString(request.getParameter("password")));
		return user;
	}

	@Override
	protected String getView() {
		return ORSView.LOGIN_VIEW;
	}
}