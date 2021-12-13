/**
 * 
 */
package in.co.sunrays.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.sunrays.beans.BaseBean;
import in.co.sunrays.beans.TimetableBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.exception.DatabaseException;
import in.co.sunrays.exception.DuplicateRecordException;
import in.co.sunrays.model.CourseModel;
import in.co.sunrays.model.SubjectModel;
import in.co.sunrays.model.TimetableModel;
import in.co.sunrays.util.DataUtility;
import in.co.sunrays.util.DataValidator;
import in.co.sunrays.util.ServletUtility;

/**
 * Timetable functionality Controller. Performs operation for add, update and
 * get Timetable
 * 
 * @author Devashish Sharma
 * @version 1.0
 * @Copyright (c) SunilOS
 */
@WebServlet(name="TimetableCtl",urlPatterns={"/ctl/TimetableCtl"})
public class TimetableCtl extends BaseCtl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(TimetableCtl.class);

	@Override
	protected void preload(HttpServletRequest request) {
		log.debug("TimetableCtl preload start");
		CourseModel courseModel = new CourseModel();
		SubjectModel subjectModel = new SubjectModel();
		TimetableModel timeTableModel = new TimetableModel();
		try {						
			request.setAttribute("courseList", courseModel.list());			
			request.setAttribute("subjectList", subjectModel.list());
			request.setAttribute("examTimeMap", timeTableModel.getExamTimeMap());
			request.setAttribute("semesterMap", courseModel.getSemesterList());

		} catch (ApplicationException e) {
			log.error(e);
			e.printStackTrace();
		}

		log.debug("TimetableCtl preload ended");
	}

	@Override
	protected boolean validate(HttpServletRequest request) {
		
		boolean flag = true;
		
		if (DataValidator.isNull(request.getParameter("courseNameId"))) {
			request.setAttribute("courseNameErr", "Course Name is Required");
			flag = false;
		}
		
		if (DataValidator.isNull(request.getParameter("subjectNameId"))) {
			request.setAttribute("subjectNameErr", "Subject Name is Required");
			flag = false;
		}
		if (DataValidator.isNull(request.getParameter("orsExamTime"))) {
			request.setAttribute("examTimeErr", "Exam Time is Required");
			flag = false;
		}
		if (DataValidator.isNull(request.getParameter("orsSemester"))) {
			request.setAttribute("semesterErr","Semester is Required");
		}
		if (DataValidator.isNull(request.getParameter("orsExamDate"))) {
			request.setAttribute("examDateErr","Exam Date is Required");
			flag = false;
		} else if (!DataValidator.isDate(request.getParameter("orsExamDate"))) {
			request.setAttribute("examDateErr","Please Enter Valid Exam Date");
			flag = false;
		}		
		
		return flag;

	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("TimetableCtl Method populatebean Started");
		TimetableBean bean = new TimetableBean();
		bean.setCourseId(DataUtility.getLong(request.getParameter("courseNameId")));
		bean.setSubjectId(DataUtility.getLong(request.getParameter("subjectNameId")));
		bean.setExamTime(DataUtility.getString(request.getParameter("orsExamTime")));
		bean.setSemester(DataUtility.getInt(request.getParameter("orsSemester")));
		bean.setExamDate(DataUtility.getDate(request.getParameter("orsExamDate")));
		populateDTO(bean, request);
		log.debug("TimetableCtl Method populatebean Ended");
		return bean;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String op = DataUtility.getString(request.getParameter("operation"));
		Long id = DataUtility.getLong(request.getParameter("id"));
		if (id > 0 || op != null) {
			TimetableModel model = new TimetableModel();
			try {
				TimetableBean bean = model.findByPK(id);
				ServletUtility.setBean(bean, request);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);

			}
		}
		ServletUtility.forward(getView(), request, response);
		

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String operation = DataUtility.getString(request.getParameter("operation"));
		System.out.println("Operation " + operation);
		
		if (OP_CANCEL.equalsIgnoreCase(operation)) {
			log.debug("In Cancel Operation");
			ServletUtility.redirect(ORSView.TIMETABLE_LIST_CTL, request, response);
			return;
		}
		
		TimetableBean bean = (TimetableBean) populateBean(request);
		TimetableModel model = new TimetableModel();
		try {
		if (OP_ADD.equalsIgnoreCase(request.getParameter("operation"))) {
			log.debug("In Add Operation");
			model.add(bean);
			request.setAttribute("addRecordMsg", "Record Has Been Successfully Added");
			log.info("Record Has Been Successfully Added");
			ServletUtility.forward(ServletUtility.trimRequestPattern(ORSView.TIMETABLE_LIST_CTL), request, response);
			return;
		} else if (OP_SAVE.equalsIgnoreCase(operation) || OP_UPDATE.equalsIgnoreCase(operation)) {
			model.update(bean);
			ServletUtility.setBean(bean, request);
			request.setAttribute("updateRecordMsg", "Record Has Been Successfully Updated");
			log.info("Record Has Been Successfully Updated");		
			ServletUtility.forward(ServletUtility.trimRequestPattern(ORSView.TIMETABLE_LIST_CTL), request, response);
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
			ServletUtility.setErrorMessage("Time Table Already Scheduled", request);
		}catch (DatabaseException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
		}		
	
		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {

		return ORSView.TIMETABLE_VIEW;
	}

}
