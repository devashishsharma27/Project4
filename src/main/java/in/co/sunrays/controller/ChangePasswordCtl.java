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
import in.co.sunrays.exception.RecordNotFoundException;
import in.co.sunrays.model.RoleModel;
import in.co.sunrays.model.UserModel;
import in.co.sunrays.util.DataUtility;
import in.co.sunrays.util.DataValidator;
import in.co.sunrays.util.ServletUtility;

@WebServlet("/ctl/ChangePasswordCtl")
public class ChangePasswordCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ChangePasswordCtl.class);   
	  public static final String OP_CHANGE_MY_PROFILE = "Change My Profile";
    public ChangePasswordCtl() {
        super();
        
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		ServletUtility.forward(getView(), request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String operation = request.getParameter("operation");
		log.debug("Operation  " + operation);
	

		if (OP_CANCEL.equalsIgnoreCase(request.getParameter("operation"))) {
			log.debug("In Cancel Operation");
			ServletUtility.redirect(ORSView.WELCOME_CTL, request, response);
			return;
		}
		if (OP_CHANGE_MY_PROFILE.equalsIgnoreCase(request.getParameter("operation"))) {
			log.debug("In Change Profile Operation");					
			ServletUtility.redirect(ORSView.MY_PROFILE_CTL, request,
                    response);
            return;
		}
		
		boolean flag = validate(request);
		UserBean bean;
		bean = populateBean(request);
		
		HttpSession session = request.getSession(true);
	    UserBean userBean = (UserBean) session.getAttribute("user");
		
	    UserModel model = new UserModel();	    
	//    String newPassword = (String) request.getParameter("newPassword");

        long id = userBean.getId();
		
		try {
			if (flag) {
				
				if (OP_SAVE.equalsIgnoreCase(request.getParameter("operation"))) {
					log.debug("In Update Operation");
				     boolean isValid = model.changePassword(id, bean.getPassword(),bean.getConfirmPassword());
				     if (isValid == true) {
		                    bean = model.findByLogin(userBean.getLoginId());
		                    session.setAttribute("user", bean);
		                    ServletUtility.setBean(bean, request);
//		                    ServletUtility.setSuccessMessage(
//		                            "Password has been changed Successfully.", request);
		                } 					
					request.setAttribute("updateRecordMsg", "Password Has Been Successfully Updated");
					log.info("Password Has Been Successfully Updated");
					ServletUtility.forward(getView(), request, response);
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
		}catch (RecordNotFoundException e) {
            ServletUtility.setErrorMessage("Invalid Old Password",request);
            ServletUtility.forward(getView(), request, response);
			return;
        }
	}
	
	
	
	
	public boolean validate(HttpServletRequest request) {
		boolean flag = true;

		log.debug("Change Password Validate");

		String operation = DataUtility.getString(request.getParameter("operation"));

		if (OP_CANCEL.equalsIgnoreCase(operation) || OP_CHANGE_MY_PROFILE.equalsIgnoreCase(operation)) {
			return flag;
		}		

		if (DataValidator.isNull(request.getParameter("oldPassword"))) {
			flag = false;
			request.setAttribute("oldPasswordErr", "Old Password is Required");
		}
		
		if (DataValidator.isNull(request.getParameter("newPassword"))) {
			flag = false;
			request.setAttribute("newPasswordErr", "New Password is Required");
		}

		if (DataValidator.isNull(request.getParameter("confirmPassword"))) {
			flag = false;
			request.setAttribute("confirmPasswordErr", "Confirm Password is Required");
		}
		if (!request.getParameter("newPassword").equals(request.getParameter("confirmPassword"))) {
			request.setAttribute("confirmPasswordErr", "Password and Confirm Password should be Same");
			flag = false;
		}

		
		return flag;
	}

	public UserBean populateBean(HttpServletRequest request) {

		log.debug("ChangePasswordCtl PopulateBean");		
		UserBean bean = new UserBean();
	    bean.setPassword(DataUtility.getString(request.getParameter("oldPassword")));
        bean.setConfirmPassword(DataUtility.getString(request.getParameter("confirmPassword")));		
		return bean;
	}

	protected String getView() {
		return ORSView.CHANGE_PASSWORD_VIEW;
	}	
}
