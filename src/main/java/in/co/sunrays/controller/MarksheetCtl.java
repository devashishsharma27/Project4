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


import in.co.sunrays.beans.MarksheetBean;

import in.co.sunrays.beans.UserBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.exception.DuplicateRecordException;

import in.co.sunrays.model.MarksheetModel;

import in.co.sunrays.util.DataUtility;
import in.co.sunrays.util.DataValidator;
import in.co.sunrays.util.ServletUtility;
/**
 * Marksheet functionality Controller. Performs operation for add, update,
 * delete and get Marksheet
 *
 * @author Devashish Sharma
 * @version 1.0
 * @Copyright (c) SunilOS
 */

@WebServlet("/ctl/MarksheetCtl")
public class MarksheetCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(MarksheetCtl.class);
	
	public MarksheetCtl() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("Operation  " + request.getParameter("operation"));
		if (OP_EDIT.equalsIgnoreCase(request.getParameter("operation"))) {
			long id = Long.parseLong(request.getParameter("id"));
			MarksheetBean bean = null;
			MarksheetModel model = new MarksheetModel();
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
		
		String operation = request.getParameter("operation");
		log.debug("Operation  " + operation);
		request.setAttribute("operation", operation);		
		
		if (OP_CANCEL.equalsIgnoreCase(request.getParameter("operation"))) {
			log.debug("In Cancel Operation");		
			ServletUtility.redirect(ORSView.MARKSHEET_LIST_CTL, request,response);
			return;
		}
		
		MarksheetModel model = new MarksheetModel();
		MarksheetBean bean;
		bean = populateBean(request);
		try {
			
			if (OP_ADD.equalsIgnoreCase(request.getParameter("operation"))) {
				log.debug("In Add Operation");
				model.addMarksheet(bean);
				request.setAttribute("addRecordMsg", "Record Has Been Successfully Added");
				log.info("Record Has Been Successfully Added");
				ServletUtility.forward(ServletUtility.trimRequestPattern(ORSView.MARKSHEET_LIST_CTL), request,
						response);
				return;
			}
			if (OP_SAVE.equalsIgnoreCase(request.getParameter("operation"))) {
				log.debug("In Update Operation");
				model.updateMarksheet(bean);
				request.setAttribute("updateRecordMsg", "Record Has Been Successfully Updated");
				log.info("Record Has Been Successfully Updated");
				ServletUtility.forward(ServletUtility.trimRequestPattern(ORSView.MARKSHEET_LIST_CTL), request,
						response);
				return;
			}
			
			long collegeId = Long.parseLong(DataUtility.getString(request.getParameter("id")));
			bean = model.findByPK(collegeId);
			request.setAttribute("bean", bean);
			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			log.error("Application Exception", e);
			ServletUtility.handleException(e, request, response);
			return;
		} catch (DuplicateRecordException e) {
			ServletUtility.setBean(bean, request);
			log.error("Roll No Already Exists");
			ServletUtility.setErrorMessage("Roll No Already Exists", request);
			ServletUtility.forward(getView(), request, response);
			return;
		}
	}

	protected void preload(HttpServletRequest request) {
		log.debug("MarksheetCtl Preload");
		MarksheetModel model = new MarksheetModel();
		request.setAttribute("studentList", model.studentNameList());
	}

	public boolean validate(HttpServletRequest request) {
		boolean flag = true;

		log.debug("MarksheetCtl Validate");

		if (DataValidator.isNull(request.getParameter("rollNo"))) {
			flag = false;
			request.setAttribute("rollNoErr", "Roll No is Required");
		}
		if (DataValidator.isNull(request.getParameter("studentId"))) {
			flag = false;
			request.setAttribute("nameErr", "Name is Required");
		}

		if (DataValidator.isNull(request.getParameter("phyMarks"))) {
			flag = false;
			request.setAttribute("phyMarksErr", "Physics Marks is Required");
		} else {
			if (!DataValidator.isInteger(request.getParameter("phyMarks"))) {
				flag = false;
				request.setAttribute("phyMarksErr", "Please Enter Integer Value Only");
			} else {
				if (DataUtility.getInt(request.getParameter("phyMarks")) > 100) {
					request.setAttribute("phyMarksErr", "Marks can not be greater than 100");
					flag = false;
				}if (DataUtility.getInt(request.getParameter("phyMarks")) < 0) {
					request.setAttribute("phyMarksErr", "Marks must be grater than zero");
					flag = false;
				}
			}
		}

		if (DataValidator.isNull(request.getParameter("chemMarks"))) {
			flag = false;
			request.setAttribute("chemMarksErr", "Chemistry Marks is Required");
		} else {
			if (!DataValidator.isInteger(request.getParameter("chemMarks"))) {
				flag = false;
				request.setAttribute("chemMarksErr", "Please Enter Integer Value Only");
			} else {
				if (DataUtility.getInt(request.getParameter("chemMarks")) > 100) {
					request.setAttribute("chemMarksErr", "Marks can not be greater than 100");
					flag = false;
				}if (DataUtility.getInt(request.getParameter("chemMarks")) < 0) {
					request.setAttribute("chemMarksErr", "Marks must be grater than zero");
					flag = false;
				}
			}
		}

		if (DataValidator.isNull(request.getParameter("mathMarks"))) {
			flag = false;
			request.setAttribute("mathMarksErr", "Maths Marks is Required");
		} else {
			if (!DataValidator.isInteger(request.getParameter("mathMarks"))) {
				flag = false;
				request.setAttribute("mathMarksErr", "Please Enter Integer Value Only");
			} else {
				if (DataUtility.getInt(request.getParameter("mathMarks")) > 100) {
					request.setAttribute("mathMarksErr", "Marks can not be greater than 100");
					flag = false;
				}if (DataUtility.getInt(request.getParameter("mathMarks")) < 0) {
					request.setAttribute("mathMarksErr", "Marks must be grater than zero");
					flag = false;
				}
			}
		}
		return flag;
	}

	public MarksheetBean populateBean(HttpServletRequest request) {

		log.debug("MarksheetCtl populateBean");
		MarksheetBean bean = new MarksheetBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setStudentId(DataUtility.getLong(request.getParameter("studentId")));
		bean.setRollNo(DataUtility.getString(request.getParameter("rollNo")));
		bean.setName(DataUtility.getString(request.getParameter("name")));	
		bean.setPhyMarks(DataUtility.getInt(request.getParameter("phyMarks")));
		bean.setChemMarks(DataUtility.getInt(request.getParameter("chemMarks")));
		bean.setMathMarks(DataUtility.getInt(request.getParameter("mathMarks")));

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
		return ORSView.MARKSHEET_VIEW;
	}

}