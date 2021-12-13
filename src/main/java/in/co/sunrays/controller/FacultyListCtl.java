/**
 * 
 */
package in.co.sunrays.controller;

import java.io.IOException;

import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.sunrays.beans.BaseBean;
import in.co.sunrays.beans.CourseBean;
import in.co.sunrays.beans.FacultyBean;
import in.co.sunrays.beans.SubjectBean;
import in.co.sunrays.beans.UserBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.model.CollegeModel;
import in.co.sunrays.model.CourseModel;
import in.co.sunrays.model.FacultyModel;
import in.co.sunrays.model.SubjectModel;
import in.co.sunrays.util.DataUtility;
import in.co.sunrays.util.PropertyReader;
import in.co.sunrays.util.ServletUtility;

/**
 * 
 * Faculty List functionality Controller. Performs operation for list, search
 * and delete operations of Faculty
 * 
 * @author Devashish Sharma
 * @version 1.0
 * @Copyright (c) SunilOS
 *
 */
@WebServlet(name = "FacultyListCtl", urlPatterns = { "/ctl/FacultyListCtl" })
public class FacultyListCtl extends BaseCtl {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(FacultyListCtl.class);

	@Override
	protected void preload(HttpServletRequest request) {
		log.debug("FacultyCtl preload started");

		CollegeModel clgmodel = new CollegeModel();
		SubjectModel subjectmodel = new SubjectModel();
		CourseModel courseModel = new CourseModel();
		try {
			request.setAttribute("collegeList", clgmodel.collegeList(0,0));
			request.setAttribute("subjectList", subjectmodel.list());
			request.setAttribute("courseList", courseModel.list());
		} catch (ApplicationException e) {
			log.error(e);
			e.printStackTrace();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}

		log.debug("FacultyCtl preload ended");

	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("FacultyCtl populatebean started");
		FacultyBean bean = new FacultyBean();
		bean.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
		bean.setCollegeId(DataUtility.getLong(request.getParameter("collegeId")));
		bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
		bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
		bean.setEmailId(DataUtility.getString(request.getParameter("login")));
		log.debug("FacultyCtl populatebean ended");
		return bean;

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int pageNo = 1;
		int pageSize = ServletUtility.getPageSize();
		FacultyBean bean = (FacultyBean) populateBean(request);
		FacultyModel model = new FacultyModel();
		try {
			List<FacultyBean> list = model.search(bean, pageNo, pageSize);

			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No Record Found", request);
			}

			request.setAttribute("list", list);
			request.setAttribute("pageSize", pageSize);
			request.setAttribute("pageNo", 1);
			request.setAttribute("recordCount", list.size());
			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<FacultyBean> list = null;
		FacultyModel model = new FacultyModel();
		FacultyBean bean = (FacultyBean) populateBean(request);

		String operation = DataUtility.getString(request.getParameter("operation"));
		log.debug("Operation " + operation);

		int pageNo = ServletUtility.getPageNo(request);
		int pageSize = ServletUtility.getPageSize();
		try {

			if (OP_ADD.equalsIgnoreCase(operation)) {
				if (request.getAttribute("addRecordMsg") == null) {
					ServletUtility.redirect(ORSView.FACULTY_CTL, request, response);
					return;
				}
				list = model.search(bean, pageNo, pageSize);
			} else if (OP_DELETE.equalsIgnoreCase(operation)) {
				String[] ids = request.getParameterValues("id");
				if (ids != null && ids.length > 0) {
					if (model.deleteFaculty(ids)) {
						request.setAttribute("deleteRecordMsg", "Record(s) Successfully Deleted");
					} else {
						ServletUtility.setErrorMessage("Record(s) Cannot be Deleted", request);
					}
				} else {
					ServletUtility.setErrorMessage("Select at least One Record", request);
				}
				list = model.search(bean, pageNo, pageSize);
			} else if (OP_SEARCH.equalsIgnoreCase(operation) || OP_NEXT.equalsIgnoreCase(operation)
					|| OP_PREVIOUS.equalsIgnoreCase(operation)) {
				request.setAttribute("subjectNameSearch", request.getParameter("subjectName"));
				request.setAttribute("courseIdSearch", request.getParameter("courseId"));
			} else {				
				list = model.search(bean, pageNo, pageSize);
			}			
			list = model.search(bean, pageNo, pageSize);

			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			request.setAttribute("list", list);
			request.setAttribute("pageSize", pageSize);
			request.setAttribute("recordCount", list.size());
			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
	}

	@Override
	protected String getView() {

		return ORSView.FACULTY_LIST_VIEW;
	}

}
