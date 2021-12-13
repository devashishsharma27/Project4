/**
 * 
 */
package in.co.sunrays.controller;

import java.io.IOException;
import java.util.Date;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.sunrays.beans.BaseBean;
import in.co.sunrays.beans.FacultyBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.exception.DatabaseException;
import in.co.sunrays.exception.DuplicateRecordException;
import in.co.sunrays.model.CollegeModel;
import in.co.sunrays.model.CourseModel;
import in.co.sunrays.model.FacultyModel;
import in.co.sunrays.model.SubjectModel;
import in.co.sunrays.util.DataUtility;
import in.co.sunrays.util.DataValidator;
import in.co.sunrays.util.ServletUtility;

/**
 * Faculty functionality Controller. Performs operation for add, update and get
 * Faculty
 * 
 * @author Devashish Sharma
 * @version 1.0
 * @Copyright (c) SunilOS
 */
@WebServlet(name = "FacultyCtl", urlPatterns = { "/ctl/FacultyCtl" })
public class FacultyCtl extends BaseCtl {


	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(FacultyCtl.class);

	@Override
	protected void preload(HttpServletRequest request) {		

		CollegeModel clgmodel = new CollegeModel();
		CourseModel courseModel = new CourseModel();
		SubjectModel subjectmodel = new SubjectModel();
		
		try { 	
		request.setAttribute("collegeList", clgmodel.collegeList(0,0));
		request.setAttribute("courseList", courseModel.list());
		request.setAttribute("subjectList", subjectmodel.list());
		
		} catch (ApplicationException e) {
			log.error(e);
			e.printStackTrace();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		
		
	}

	@Override
	protected boolean validate(HttpServletRequest request) {
		
		boolean flag = true;
		String operation = DataUtility.getString(request.getParameter("operation"));
		
		if (OP_CANCEL.equalsIgnoreCase(operation)) {
			return flag;
		}
	
		if (DataValidator.isNull(request.getParameter("fName"))) {
			request.setAttribute("firstNameErr", "First Name is Required");
			flag = false;
		} else if (!DataValidator.isName(request.getParameter("fName"))) {
			request.setAttribute("firstNameErr", "Please Enter Valid First Name");
			flag = false;
		}

		if (DataValidator.isNull(request.getParameter("lName"))) {
			request.setAttribute("lastNameErr", "First Name is Required");			
			flag = false;
		} else if (!DataValidator.isName(request.getParameter("lName"))) {
			request.setAttribute("lastNameErr", "Please Enter Valid First Name");
			flag = false;
		}
		
		if (DataValidator.isNull(request.getParameter("qualification"))) {
			request.setAttribute("qualificationErr", "Qualification is Required");
			flag = false;
		}
		
		if (DataValidator.isNull(request.getParameter("loginId"))) {
			request.setAttribute("loginIdErr", "Login Id is Required");
			flag = false;
		} else if (!DataValidator.isEmail(request.getParameter("loginId"))) {			
			request.setAttribute("loginIdErr", "Please Enter Valid Login Id");
			flag = false;
		}
		
		if (DataValidator.isNull(request.getParameter("mobileNo"))) {
			request.setAttribute("mobileNoErr", "Phone is Required");
			flag = false;
		} else if (!DataValidator.isMobileNumber(request.getParameter("mobileNo"))) {
			request.setAttribute("mobileNoErr", "Please Enter Valid Mobile No");
			flag = false;
		}
		
		if (DataValidator.isNull(request.getParameter("gender"))) {
			request.setAttribute("genderErr", "Gender is Required");
			flag = false;
		}
		
		if (DataValidator.isNull(request.getParameter("dob"))) {
			flag = false;
			request.setAttribute("dobErr", "Date of Birth is Required");
		} else if (!DataValidator.isDate(DataUtility.getString(request.getParameter("dob")))) {
			flag = false;
			request.setAttribute("dobErr", "Please Enter Valid Date of Birth");
		}
		
		System.out.println("college Id :: " + request.getParameter("collegeNameId"));
		if (DataValidator.isNull(request.getParameter("collegeNameId"))) {
			request.setAttribute("collegeNameErr", "College Name is Required");
			flag = false;
		}

		if (DataValidator.isNull(request.getParameter("courseNameId"))) {
			request.setAttribute("courseNameErr", "Course Name is Required");
			flag = false;
		}
		if (DataValidator.isNull(request.getParameter("subjectNameId"))) {
			request.setAttribute("subjectNameErr", "Subject Name is Required");
			flag = false;
		}		
		return flag;
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		FacultyBean bean = new FacultyBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setFirstName(DataUtility.getString(request.getParameter("fName")));
		bean.setLastName(DataUtility.getString(request.getParameter("lName")));
		bean.setQualification(DataUtility.getString(request.getParameter("qualification")));
		bean.setEmailId(DataUtility.getString(request.getParameter("loginId")));
		bean.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
		bean.setGender(DataUtility.getString(request.getParameter("gender")));
		bean.setDOB(DataUtility.getDate(request.getParameter("dob")));
		bean.setCollegeId(DataUtility.getLong(request.getParameter("collegeNameId")));
		bean.setCourseId(DataUtility.getLong(request.getParameter("courseNameId")));
		bean.setSubjectId(DataUtility.getLong(request.getParameter("subjectNameId")));		
		populateDTO(bean, request);
		return bean;
	}

	/**
	 * Contains Display Logics
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		

		String operation = DataUtility.getString(request.getParameter("operation"));
		FacultyModel model = new FacultyModel();

		long id = DataUtility.getLong(request.getParameter("id"));
		if (id > 0 || operation != null) {
			FacultyBean bean;
			try {

				bean = model.findByPK(id);
				ServletUtility.setBean(bean, request);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}
		ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Contains Submit Logics
	 */

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {		
		
		String operation = DataUtility.getString(request.getParameter("operation"));
		System.out.println("Operation " + operation);
		
		if (OP_CANCEL.equalsIgnoreCase(operation)) {
			log.debug("In Cancel Operation");
			ServletUtility.redirect(ORSView.FACULTY_LIST_CTL, request, response);
			return;
		}
		
		FacultyBean bean = (FacultyBean) populateBean(request);
		FacultyModel model = new FacultyModel();
		try {
		if (OP_ADD.equalsIgnoreCase(request.getParameter("operation"))) {
			log.debug("In Add Operation");
			model.add(bean);
			request.setAttribute("addRecordMsg", "Record Has Been Successfully Added");
			log.info("Record Has Been Successfully Added");
			ServletUtility.forward(ServletUtility.trimRequestPattern(ORSView.FACULTY_LIST_CTL), request, response);
			return;
		} else if (OP_SAVE.equalsIgnoreCase(operation) || OP_UPDATE.equalsIgnoreCase(operation)) {
			model.update(bean);
			ServletUtility.setBean(bean, request);
			request.setAttribute("updateRecordMsg", "Record Has Been Successfully Updated");
			log.info("Record Has Been Successfully Updated");		
			ServletUtility.forward(ServletUtility.trimRequestPattern(ORSView.FACULTY_LIST_CTL), request, response);
		}
		
		long facultyId = Long.parseLong(DataUtility.getString(request.getParameter("id")));
		bean = model.findByPK(facultyId);
		request.setAttribute("bean", bean);
		ServletUtility.forward(getView(), request, response);
		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		} catch (DuplicateRecordException e) {
			ServletUtility.setBean(bean, request);
			ServletUtility.setErrorMessage("Faculty already exists", request);
		}catch (DatabaseException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
		}		
	
		ServletUtility.forward(getView(), request, response);
		log.debug("FacultyCtl Method doPost Started");
		System.out.println("FacultyCtl Method doPost Ended");
	}

	@Override
	protected String getView() {

		return ORSView.FACULTY_VIEW;
	}
	
	/*
	 * public static String getDateFormat(Object dob) { Date date = (Date)dob ;
	 * return DataUtility.getDateFormat(date); }
	 */
}
