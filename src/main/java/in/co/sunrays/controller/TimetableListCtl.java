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
import in.co.sunrays.beans.FacultyBean;
import in.co.sunrays.beans.TimetableBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.model.CourseModel;
import in.co.sunrays.model.FacultyModel;
import in.co.sunrays.model.SubjectModel;
import in.co.sunrays.model.TimetableModel;
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
@WebServlet(name = "TimetableListCtl", urlPatterns = { "/ctl/TimetableListCtl" })
public class TimetableListCtl extends BaseCtl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(TimetableListCtl.class);

	@Override
	protected void preload(HttpServletRequest request) {
		log.debug("TimetableCtl preload started");
		CourseModel courseModel = new CourseModel();
		SubjectModel subjectModel = new SubjectModel();
		try {			
			request.setAttribute("courseList", courseModel.list());			
			request.setAttribute("semesterMap", courseModel.getSemesterList());
			request.setAttribute("subjectList", subjectModel.list());
		} catch (ApplicationException e) {
			log.error(e);
		}
		log.debug("TimetableCtl preload ended");
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("TimetableCtl populatebean started");
		TimetableBean bean = new TimetableBean();
		bean.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
		bean.setSubjectId(DataUtility.getLong(request.getParameter("subjectId")));
		bean.setExamDate(DataUtility.getDate(request.getParameter("examDate")));		
		bean.setSemester(DataUtility.getInt(request.getParameter("semester")));
		bean.setExamTime(DataUtility.getString(request.getParameter("examTime")));
		log.debug("TimetableCtl populatebean started");
		return bean;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("TimetableCtl doGet Started");
		
		int pageNo = 1;
		int pageSize = ServletUtility.getPageSize();
		TimetableBean bean = (TimetableBean) populateBean(request);
		TimetableModel model = new TimetableModel();
		try {
			List<TimetableBean> list = model.search(bean, pageNo, pageSize);

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
		
		log.debug("TimetableCtl doGet Ended");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		List<TimetableBean> list = null;
		TimetableModel model = new TimetableModel();
		TimetableBean bean = (TimetableBean) populateBean(request);

		String operation = DataUtility.getString(request.getParameter("operation"));
		log.debug("Operation " + operation);

		int pageNo = ServletUtility.getPageNo(request);
		int pageSize = ServletUtility.getPageSize();
		try {

			if (OP_ADD.equalsIgnoreCase(operation)) {
				if (request.getAttribute("addRecordMsg") == null) {
					ServletUtility.redirect(ORSView.TIMETABLE_CTL, request, response);
					return;
				}
				list = model.search(bean, pageNo, pageSize);
			} else if (OP_DELETE.equalsIgnoreCase(operation)) {
				String[] ids = request.getParameterValues("id");
				if (ids != null && ids.length > 0) {
					if (model.deleteTimetable(ids)) {
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
				request.setAttribute("courseIdSearch", request.getParameter("courseId"));
				request.setAttribute("subjectNameSearch", request.getParameter("subjectId"));
				request.setAttribute("semesterSearch", request.getParameter("semester"));
				request.setAttribute("examDateSearch", request.getParameter("examDate"));
				
				System.out.println("courseId :: " + request.getParameter("courseId"));
				System.out.println("subjectId :: " + request.getParameter("subjectId"));				
				System.out.println("semester :: " + request.getParameter("semester"));
				System.out.println("examDate :: " + request.getParameter("examDate"));
				System.out.println("examDateSearch :: " + request.getAttribute("examDateSearch"));
				
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

		return ORSView.TIMETABLE_LIST_VIEW;
	}

}
