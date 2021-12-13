/**
 * 
 */
package in.co.sunrays.controller;

import java.io.IOException;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.sunrays.beans.BaseBean;
import in.co.sunrays.beans.CourseBean;
import in.co.sunrays.beans.SubjectBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.exception.DatabaseException;
import in.co.sunrays.exception.DuplicateRecordException;
import in.co.sunrays.model.CourseModel;
import in.co.sunrays.model.SubjectModel;
import in.co.sunrays.util.DataUtility;
import in.co.sunrays.util.DataValidator;
import in.co.sunrays.util.PropertyReader;
import in.co.sunrays.util.ServletUtility;

/**
 * Subject functionality Controller. Performs operation for add, update and get
 * Subject
 * 
 * @author Devashish Sharma
 * @version 1.0
 * @Copyright (c) SunilOS
 */
@WebServlet(name = "SubjectCtl", urlPatterns = { "/ctl/SubjectCtl" })
public class SubjectCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(SubjectCtl.class);
	private static final long serialVersionUID = 1L;

	@Override
	protected void preload(HttpServletRequest request) {
		CourseModel model = new CourseModel();
		try {
			List list = model.list();
			request.setAttribute("courseList", list);
		} catch (ApplicationException e) {

			e.printStackTrace();
		}

	}

	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean pass = true;
		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("nameErr", "Subject Name is Required");
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("courseNameId"))) {
			request.setAttribute("courseNameIdErr", "Course Name is Required");
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
		SubjectBean bean = new SubjectBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setCourseId(DataUtility.getLong(request.getParameter("courseNameId")));
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

		String operation = DataUtility.getString(request.getParameter("operation"));
		SubjectModel model = new SubjectModel();

		long id = DataUtility.getLong(request.getParameter("id"));
		if (id > 0 || operation != null) {
			SubjectBean bean;
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

		if (OP_CANCEL.equalsIgnoreCase(operation)) {
			ServletUtility.redirect(ORSView.SUBJECT_LIST_CTL, request, response);
			return;

		}
//		long id = DataUtility.getLong(request.getParameter("id"));
		SubjectBean bean = (SubjectBean) populateBean(request);
		SubjectModel model = new SubjectModel();
		try {

			if (OP_ADD.equalsIgnoreCase(request.getParameter("operation"))) {
				log.debug("In Add Operation");				
				model.addSubject(bean);			
				request.setAttribute("addRecordMsg", "Record Has Been Successfully Added");
				log.info("Record Has Been Successfully Added");
				ServletUtility.forward(ServletUtility.trimRequestPattern(ORSView.SUBJECT_LIST_CTL), request, response);
				return;
			} else if (OP_SAVE.equalsIgnoreCase(operation) || OP_UPDATE.equalsIgnoreCase(operation)) {
				model.updateSubject(bean);
				ServletUtility.setBean(bean, request);
				request.setAttribute("updateRecordMsg", "Record Has Been Successfully Updated");
				log.info("Record Has Been Successfully Updated");
				ServletUtility.forward(ServletUtility.trimRequestPattern(ORSView.SUBJECT_LIST_CTL), request, response);
			}

			long subjectId = Long.parseLong(DataUtility.getString(request.getParameter("id")));
			bean = model.findByPK(subjectId);
			request.setAttribute("bean", bean);
			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		} catch (DuplicateRecordException e) {
			ServletUtility.setBean(bean, request);
			ServletUtility.setErrorMessage("Subject Already exists", request);
		}catch (DatabaseException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
		}
		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		return ORSView.SUBJECT_VIEW;
	}

}
