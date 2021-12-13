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

import in.co.sunrays.beans.StudentBean;
import in.co.sunrays.beans.UserBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.exception.DuplicateRecordException;
import in.co.sunrays.model.CollegeModel;
import in.co.sunrays.model.StudentModel;
import in.co.sunrays.util.DataUtility;
import in.co.sunrays.util.DataValidator;
import in.co.sunrays.util.ServletUtility;

/**
 * Student functionality Controller. Performs operation for add, update, delete
 * and get Student
 *
 * @author  Devashish Sharma
 * @version 1.0
 * @Copyright (c) SunilOS
 */

@WebServlet("/ctl/StudentCtl")
public class StudentCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(StudentCtl.class);
	public StudentCtl() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("Operation  " + request.getParameter("operation"));
		System.out.println("Operation  " + request.getParameter("operation"));

		if (OP_EDIT.equalsIgnoreCase(request.getParameter("operation"))) {
			long stuId = Long.parseLong(request.getParameter("id"));
			StudentBean bean = null;
			StudentModel model = new StudentModel();
			try {
				bean = model.findByPK(stuId);
			} catch (ApplicationException e) {
				ServletUtility.handleException(e, request, response);
				return;
			}
			request.setAttribute("bean", bean);
			request.setAttribute("operation", request.getParameter("operation"));
		}
		ServletUtility.forward(ORSView.STUDENT_VIEW, request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException { 
		
		String operation = request.getParameter("operation");
		log.debug("Operation  " + operation);
		System.out.println("Operation  " + operation);
		request.setAttribute("operation", operation);
		
		if (OP_CANCEL.equalsIgnoreCase(request.getParameter("operation"))) {
			log.debug("In Cancel Operation");
		//	ServletUtility.forward(ServletUtility.trimRequestPattern(ORSView.STUDENT_LIST_CTL), request, response);
			ServletUtility.redirect(ORSView.STUDENT_LIST_CTL, request,response);
			return;
		}
		
		boolean flag = validate(request);
		StudentModel model = new StudentModel();
		StudentBean bean;
		bean = populateBean(request);
		try {			
			if (flag) {				
				if (OP_ADD.equalsIgnoreCase(request.getParameter("operation"))) {					
					log.debug("In Add Operation");
					model.addStudent(bean);					
					request.setAttribute("addRecordMsg", "Record Has Been Successfully Added");
					log.info("Record Has Been Successfully Added");
					ServletUtility.forward(ServletUtility.trimRequestPattern(ORSView.STUDENT_LIST_CTL), request, response);
				} 				
				if (OP_SAVE.equalsIgnoreCase(request.getParameter("operation"))) {
					log.debug("In Update Operation");
					model.updateStudent(bean);
					request.setAttribute("updateRecordMsg", "Record Has Been Successfully Updated");
					log.info("Record Has Been Successfully Updated");
					ServletUtility.forward(ServletUtility.trimRequestPattern(ORSView.STUDENT_LIST_CTL), request, response);
				}
				
			}else{
				long stuId = Long.parseLong(DataUtility.getString(request.getParameter("studentId")));
				bean = model.findByPK(stuId);
				request.setAttribute("bean", bean);
				ServletUtility.forward(ORSView.STUDENT_VIEW, request, response);		
			}	
		}catch (ApplicationException e) {
			log.error("Application Exception", e);
			ServletUtility.handleException(e, request, response);
			return;
		}catch(DuplicateRecordException e){ 
		  ServletUtility.setBean(bean, request);
		  log.error("Student Email Id Already Exists");
		  ServletUtility.setErrorMessage("Student Email Id Already Exists", request); 
		  ServletUtility.forward(ORSView.STUDENT_VIEW, request, response);
		  return;
		  }		
	}
	
	
	
	protected void test(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// System.out.println("----------------in post------------------");
		
		  String studentId = request.getParameter("studentId"); String collegeId =
		  request.getParameter("collegeId"); String collegeName =
		  request.getParameter("collegeName"); String firstName =
		  request.getParameter("firstName"); String lastName =
		  request.getParameter("lastName"); String dob = request.getParameter("dob");
		  String mobNo = request.getParameter("mobNo"); String email =
		  request.getParameter("email");
		  
		  System.out.println("studentId  " + studentId);
		  System.out.println("collegeId  " + collegeId);
		  System.out.println("collegeName  " + collegeName);
		  System.out.println("firstName  " + firstName);
		  System.out.println("lastName  " + lastName); System.out.println("dob  " +
		  dob); System.out.println("mobNo  " + mobNo); System.out.println("email  " +
		  email);
		 
		String operation = request.getParameter("operation");
		System.out.println(" Operation  " + operation);
		
		boolean flag = validate(request);
		StudentModel model = new StudentModel();
		StudentBean bean;
		System.out.println("flag " + flag);
		if (flag) {

			bean = populateBean(request);

			if (OP_ADD.equalsIgnoreCase(request.getParameter("operation"))) {
				try {
					System.out.println("In Add Operation");
					model.addStudent(bean);
				} catch (ApplicationException e) {
					ServletUtility.handleException(e, request, response);
					return;
				} catch (DuplicateRecordException e){ 
				  ServletUtility.setBean(bean, request);
				  ServletUtility.setErrorMessage("Student Email Id Already Exists", request); 
				  ServletUtility.forward(ORSView.STUDENT_VIEW, request, response);
				  return;
				  } 
				request.setAttribute("addRecordMsg", "Record Has Been Successfully Added");
				ServletUtility.forward(ServletUtility.trimRequestPattern(ORSView.STUDENT_LIST_CTL), request, response);			
			} 
			

			if (OP_SAVE.equalsIgnoreCase(request.getParameter("operation"))) {
				System.out.println("In Update Operation");
				try {
					model.updateStudent(bean);
				}catch(ApplicationException e) {
						ServletUtility.handleException(e, request, response);
						return;
					} catch(DuplicateRecordException e){ 
						  ServletUtility.setBean(bean, request);
						  ServletUtility.setErrorMessage("Student Email Id Already Exists", request); 
						  ServletUtility.forward(ORSView.STUDENT_VIEW, request, response);
						  return;
						  } 
				// request.setAttribute("collegeList", clgModel.getCollegeList());
				request.setAttribute("updateRecordMsg", "Record Has Been Successfully Updated");
				ServletUtility.forward(ServletUtility.trimRequestPattern(ORSView.STUDENT_LIST_CTL), request, response);
			}

		} else {
			long stuId = Long.parseLong(DataUtility.getString(request.getParameter("studentId")));
			try {
				bean = model.findByPK(stuId);
			} catch (ApplicationException e) {
				ServletUtility.handleException(e, request, response);
				return;
			}
			// request.setAttribute("collegeList", clgModel.getCollegeList());
			request.setAttribute("bean", bean);
			ServletUtility.forward(ORSView.STUDENT_VIEW, request, response);
		}

	}

	protected void preload(HttpServletRequest request) {
		log.debug("StudentCtl Preload");
		CollegeModel clgModel = new CollegeModel();
		request.setAttribute("collegeList", clgModel.getCollegeList());
	}

	public boolean validate(HttpServletRequest request) {
		boolean flag = true;

		log.debug("StudentCtl Validate");
		/*
		 * if(DataUtility.validateString(DataUtility.getString(request.getParameter(
		 * "studentId")))) { flag = false; request.setAttribute("studentIdErr",
		 * "Student ID is required"); }
		 */
		/*
		 * if (!DataUtility.validateString(DataUtility.getString(request.getParameter(
		 * "collegeId")))) { flag = false; request.setAttribute("collegeIdErr",
		 * "College Id is required"); }
		 */
		/*
		 * if (!DataValidator.validateString(DataUtility.getString(request.getParameter(
		 * "collegeName")))) { flag = false; request.setAttribute("collegeNameErr",
		 * "College Name is required"); }
		 */
		
		String operation = DataUtility.getString(request.getParameter("operation"));
		
		if (OP_CANCEL.equalsIgnoreCase(operation)) {
			return flag;
		}
		if (DataValidator.isNull(request.getParameter("firstName"))) {
			flag = false;
			request.setAttribute("firstNameErr", "First Name is Required");
		} else if (!DataValidator.isValidName(DataUtility.getString(request.getParameter("firstName")))) {
			flag = false;
			request.setAttribute("firstNameErr", "Please Enter Valid First Name");
		}

		if (DataValidator.isNull(request.getParameter("lastName"))) {
			flag = false;
			request.setAttribute("lastNameErr", "Last Name is Required");
		} else if (!DataValidator.isValidName(DataUtility.getString(request.getParameter("lastName")))) {
			flag = false;
			request.setAttribute("lastNameErr", "Please Enter Valid Last Name");
		}

		if (DataValidator.isNull(request.getParameter("dob"))) {
			flag = false;
			request.setAttribute("dobErr", "Date of Birth is Required");
		} else if (!DataValidator.isDate(DataUtility.getString(request.getParameter("dob")))) {
			flag = false;
			request.setAttribute("dobErr", "Please Enter Valid Date of Birth");
		}

		if (DataValidator.isNull(request.getParameter("mobNo"))) {
			flag = false;
			request.setAttribute("mobNoErr", "Mobile No is Required");
		} else if (!DataValidator.isValidMobNo(DataUtility.getString(request.getParameter("mobNo")))) {
			flag = false;
			request.setAttribute("mobNoErr", "Please Enter Valid 10 Digit Mobile Number");
		}

		if (DataValidator.isNull(request.getParameter("email"))) {
			flag = false;
			request.setAttribute("emailErr", "Email is Required");
		} else if (!DataValidator.isEmail(DataUtility.getString(request.getParameter("email")))) {
			flag = false;
			request.setAttribute("emailErr", "Please Enter Valid Email (abc@pqr.xyz)");
		}

		return flag;
	}

	public StudentBean populateBean(HttpServletRequest request) {
		
		log.debug("StudentCtl PopulateBean");
		/*
		 * System.out.println("Inside studentCtl populateBean"); String studentId =
		 * request.getParameter("studentId"); String collegeId =
		 * request.getParameter("collegeId"); String collegeName =
		 * request.getParameter("collegeName"); String firstName =
		 * request.getParameter("firstName"); String lastName =
		 * request.getParameter("lastName"); String dob = request.getParameter("dob");
		 * String mobNo = request.getParameter("mobNo"); String email =
		 * request.getParameter("email");
		 * 
		 * System.out.println("studentId  " + studentId);
		 * System.out.println("collegeId  " + collegeId);
		 * System.out.println("collegeName  " + collegeName);
		 * System.out.println("firstName  " + firstName);
		 * System.out.println("lastName  " + lastName); System.out.println("dob  " +
		 * dob); System.out.println("mobNo  " + mobNo); System.out.println("email  " +
		 * email);
		 
		String operation = request.getParameter("operation");
		System.out.println(" Operation  " + operation);
		*/
		
		StudentBean bean = new StudentBean();
		bean.setId(DataUtility.getLong(request.getParameter("studentId")));
		bean.setCollegeId(DataUtility.getLong(request.getParameter("collegeId")));
		bean.setCollegeName(DataUtility.getString(request.getParameter("collegeName")));
		bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
		bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
		bean.setDob(DataUtility.getDate(request.getParameter("dob")));
		bean.setMobileNo(DataUtility.getString(request.getParameter("mobNo")));
		bean.setEmail(request.getParameter("email"));

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

	@Override
	protected String getView() {
		return ORSView.STUDENT_VIEW;
	}
	
	
	  public static String getDateFormat(Object dob) { Date date = (Date)dob ;
	  return DataUtility.getDateFormat(date); }
	 
}
