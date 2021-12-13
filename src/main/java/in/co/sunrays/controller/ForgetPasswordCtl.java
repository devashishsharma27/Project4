package in.co.sunrays.controller;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.sunrays.beans.UserBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.exception.RecordNotFoundException;
import in.co.sunrays.model.UserModel;
import in.co.sunrays.util.DataUtility;
import in.co.sunrays.util.DataValidator;
import in.co.sunrays.util.ServletUtility;

@WebServlet("/ForgetPasswordCtl")
public class ForgetPasswordCtl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String OP_ForgetPassword = "Go";
	private static Logger log = Logger.getLogger(ForgetPasswordCtl.class);

	public ForgetPasswordCtl() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ServletUtility.forward(ORSView.FORGET_PASSWORD_VIEW, request, response);
		return;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String operation = DataUtility.getString(request.getParameter("operation"));
		log.debug("Operation :: " + operation);
		log.debug("Email   " + request.getParameter("emailId"));

		UserBean user = populateBean(request);
		if (OP_ForgetPassword.equalsIgnoreCase(operation)) {
			try {
				UserModel model = new UserModel();
				model.forgetPassword(user.getLoginId());
				
				log.info("Password has been sent Successfully");
				request.setAttribute("password", "Your Password is sent to registered Email ID");

			} catch (RecordNotFoundException e) {
				log.info("Email ID does not exists !");
				request.setAttribute("invalidEmailErrorMsg", "Email ID does not exists !");
				ServletUtility.setErrorMessage(e.getMessage(), request);
				log.error(e);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		}

		ServletUtility.forward(ORSView.FORGET_PASSWORD_VIEW, request, response);

	}

	
	public boolean validate(HttpServletRequest request) {
		boolean flag = true;

		if (DataUtility.getString(request.getParameter("emailId")) == null
				|| DataUtility.getString(request.getParameter("emailId")) == "") {
			flag = false;
			request.setAttribute("usernameErr", "Email Id is required");
		} else if (!DataValidator.isEmail(DataUtility.getString(request.getParameter("userName")))) {
			flag = false;
			request.setAttribute("usernameErr", "Please Enter Valid Username (abc@pqr.xyz)");
		}

		return flag;
	}

	public UserBean populateBean(HttpServletRequest request) {
		UserBean user = new UserBean();
		user.setLoginId(DataUtility.getString(request.getParameter("emailId")));
		return user;
	}
}
