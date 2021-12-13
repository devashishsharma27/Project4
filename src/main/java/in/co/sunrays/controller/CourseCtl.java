package in.co.sunrays.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.sunrays.beans.BaseBean;
import in.co.sunrays.beans.CourseBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.exception.DuplicateRecordException;
import in.co.sunrays.model.CourseModel;
import in.co.sunrays.util.DataUtility;
import in.co.sunrays.util.DataValidator;
import in.co.sunrays.util.ServletUtility;

/**
 * Course functionality Controller. Performs operation for add, update and get
 * Course
 * 
 * @author Devashish Sharma
 * @version 1.0
 * 
 */
@WebServlet(name = "CourseCtl", urlPatterns = { "/ctl/CourseCtl" })
public class CourseCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(CourseCtl.class);

	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean pass = true;
		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("nameErr", "Course Name is Required");
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("duration"))) {
			request.setAttribute("durationErr", "Duration is Required");
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("description"))) {
			request.setAttribute("descriptionErr", "Description is Required");
			pass = false;
		}

		return pass;
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		CourseBean bean = new CourseBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setDuration(DataUtility.getString(request.getParameter("duration")));
		bean.setDescription(DataUtility.getString(request.getParameter("description")));
		populateDTO(bean, request);
		return bean;
	}

	/**
	 * contains display logics
	 * 
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String op = DataUtility.getString(request.getParameter("operation"));

		// get model
		CourseModel model = new CourseModel();

		long id = DataUtility.getLong(request.getParameter("id"));
		if (id > 0 || op != null) {
			CourseBean bean;
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
	 * contains submit logics
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String operation = DataUtility.getString(request.getParameter("operation"));
		request.setAttribute("operation", operation);		

		if (OP_CANCEL.equalsIgnoreCase(operation)) {
			log.debug("In Cancel Operation");
			ServletUtility.redirect(ORSView.COURSE_LIST_CTL, request, response);
			return;
		}

		CourseBean bean = (CourseBean) populateBean(request);
		CourseModel model = new CourseModel();
		try {

			if (OP_ADD.equalsIgnoreCase(request.getParameter("operation"))) {
				log.debug("In Add Operation");
				model.addCourse(bean);
				request.setAttribute("addRecordMsg", "Record Has Been Successfully Added");
				log.info("Record Has Been Successfully Added");
				ServletUtility.forward(ServletUtility.trimRequestPattern(ORSView.COURSE_LIST_CTL), request, response);
				return;
			} else if (OP_SAVE.equalsIgnoreCase(operation) || OP_UPDATE.equalsIgnoreCase(operation)) {
				model.updateCourse(bean);
				ServletUtility.setBean(bean, request);
				request.setAttribute("updateRecordMsg", "Record Has Been Successfully Updated");
				log.info("Record Has Been Successfully Updated");		
				ServletUtility.forward(ServletUtility.trimRequestPattern(ORSView.COURSE_LIST_CTL), request, response);
			}
			
			long courseId = Long.parseLong(DataUtility.getString(request.getParameter("id")));
			bean = model.findByPK(courseId);
			request.setAttribute("bean", bean);
			ServletUtility.forward(getView(), request, response);
			
		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		} catch (DuplicateRecordException e) {
			ServletUtility.setBean(bean, request);
			ServletUtility.setErrorMessage("Course already exists", request);
		}
	}

	protected void preload(HttpServletRequest request) {
		log.debug("CourseCtl Preload");
		CourseModel model = new CourseModel();
		request.setAttribute("courseDurationList", model.getCourseDurationList());
	}

	@Override
	protected String getView() {

		return ORSView.COURSE_VIEW;
	}

}
