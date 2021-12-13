package in.co.sunrays.controller;

import java.io.IOException;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.sunrays.beans.RoleBean;
import in.co.sunrays.beans.UserBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.exception.DuplicateRecordException;
import in.co.sunrays.model.UserModel;
import in.co.sunrays.util.DataUtility;
import in.co.sunrays.util.ServletUtility;

import javax.servlet.annotation.WebServlet; 

/**
 * User registration functionality Controller. Performs operation for User
 * Registration
 *
 * @author  Devashish Sharma
 * @version 1.0
 * @Copyright (c) SunilOS
 */
@WebServlet("/UserRegistrationCtl") 
public class UserRegistrationCtl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String OP_REGISTER = "Register";   
   
    public UserRegistrationCtl() {
        super();
        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("in registration ");
		ServletUtility.forward(ORSView.USER_REGISTRATION_VIEW, request, response);
		return;
	//	response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String firstname = request.getParameter("fname");
		String lastName = request.getParameter("lname");
		String loginId = request.getParameter("loginId");
		String password = request.getParameter("pwd");
		String gender = request.getParameter("gender");
		String dob = request.getParameter("dob");		
		String mobNo = request.getParameter("mobNo");
		
		
		System.out.println("firstname  " + firstname);
		System.out.println("lastName  " + lastName);
		System.out.println("loginId  " + loginId);
		System.out.println("password  " + password);
		System.out.println("gender  " + gender);
		System.out.println("dob  " + dob);
		System.out.println("mobNo  " + mobNo);  	
		
		String operation =request.getParameter("operation");
		System.out.println("Operation  " + operation);	
		boolean flag = validate(request);
		if(flag) {
			
			UserBean user  = populateBean(request);				
			if(OP_REGISTER.equalsIgnoreCase(operation)) {
				UserModel model = new UserModel();
				try {
					model.addUser(user);
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DuplicateRecordException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Success");
				ServletUtility.forward(ORSView.LOGIN_VIEW, request, response);
				return;
			}
		}else {
			System.out.println("Registration Failed");
			ServletUtility.forward(ORSView.USER_REGISTRATION_VIEW, request, response);
			return;
		}		
		
	}
	
	
	public boolean validate(HttpServletRequest request) {
		boolean flag = true ;
				
		if(DataUtility.getString(request.getParameter("fname"))==null || DataUtility.getString(request.getParameter("fname"))=="") {
			flag = false ;
			request.setAttribute("firstNameErr", "First name is required");
		}
		if(DataUtility.getString(request.getParameter("lname"))==null || DataUtility.getString(request.getParameter("lname"))=="") {
			flag = false ;
			request.setAttribute("lastNameErr", "Last name is required");
		}
		if(DataUtility.getString(request.getParameter("loginId"))==null || DataUtility.getString(request.getParameter("loginId"))=="") {
			flag = false ;
			request.setAttribute("loginIdErr", "Login Id is required");
		}
		if(DataUtility.getString(request.getParameter("pwd"))==null || DataUtility.getString(request.getParameter("pwd"))=="") {
			flag = false ;
			request.setAttribute("passwordErr", "Password is required");
		}
		if(request.getParameter("cpwd")==null || request.getParameter("cpwd")=="") {
			flag = false ;
			request.setAttribute("confirmPasswordErr", "Confirm Password is required");
		}		
		if(DataUtility.getString(request.getParameter("gender"))==null || DataUtility.getString(request.getParameter("gender"))=="") {
			flag = false ;
			request.setAttribute("genderErr", "Gender is required");
		}
		if(DataUtility.getString(request.getParameter("dob"))==null || DataUtility.getString(request.getParameter("dob"))=="") {
			flag = false ;
			request.setAttribute("dobErr", "Date of Birth is required");
		}
		if(DataUtility.getString(request.getParameter("mobNo"))==null || DataUtility.getString(request.getParameter("mobNo"))=="") {
			flag = false ;
			request.setAttribute("MobileErr", "Mobile No is required");
		}
		
		return flag ;
	}
	
	public UserBean populateBean(HttpServletRequest request) {
		
		UserBean user  = new UserBean();			
		user.setFirstName(DataUtility.getString(request.getParameter("fname")).toUpperCase());
		user.setLastName(DataUtility.getString(request.getParameter("lname")).toUpperCase());
		user.setLoginId(DataUtility.getString(request.getParameter("loginId")));
		user.setPassword(DataUtility.getString(request.getParameter("pwd")));
		user.setGender(DataUtility.getString(request.getParameter("gender")));
		user.setDob(DataUtility.getDate(request.getParameter("dob")));
		user.setMobNo(DataUtility.getString(request.getParameter("mobNo")));
		user.setCreatedBy(request.getParameter("loginId"));
		user.setModifiedBy(request.getParameter("loginId"));
		user.setCreatedDateTime(new Timestamp(new Date().getTime()));
		user.setModifiedDateTime(new Timestamp(new Date().getTime()));
		user.setRoleId(RoleBean.STUDENT);
		
		return user ;
	}
}
