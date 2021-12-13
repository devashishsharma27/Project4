/**
 * 
 */
package in.co.sunrays.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.sunrays.beans.BaseBean;
import in.co.sunrays.beans.CourseBean;
import in.co.sunrays.beans.MarksheetBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.model.CourseModel;
import in.co.sunrays.util.DataUtility;
import in.co.sunrays.util.ServletUtility;

/**
 * Course List functionality Controller. Performs operation for list, search
 * operations of Course
 * 
 * @author Devashish Sharma
 * @version 1.0
 * @Copyright (c) SunilOS
 */
@WebServlet(name = "CourseListCtl", urlPatterns = { "/ctl/CourseListCtl" })
public class CourseListCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(RoleListCtl.class);

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		CourseBean bean = new CourseBean();
		bean.setName(request.getParameter("courseName"));
		bean.setDuration(request.getParameter("courseDuration"));
		return bean;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		List<CourseBean> list = null;
		String operation = DataUtility.getString(request.getParameter("operation"));
		System.out.println("Operation " + operation);

		try {
			int pageNo = 1;
			int pageSize = ServletUtility.getPageSize();
			CourseBean bean = (CourseBean) populateBean(request);
			CourseModel model = new CourseModel();

			list = model.search(bean, pageNo, pageSize);			
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
		
		List<CourseBean> list = null;
		CourseModel model = new CourseModel();
		CourseBean bean = (CourseBean) populateBean(request);
		String operation = DataUtility.getString(request.getParameter("operation"));
		log.debug("Operation " + operation);
		
		int pageNo = ServletUtility.getPageNo(request);
		int pageSize = ServletUtility.getPageSize();
		try {
			
			if (OP_ADD.equalsIgnoreCase(operation)) {
				if (request.getAttribute("addRecordMsg") == null) {			
					ServletUtility.redirect(ORSView.COURSE_CTL, request, response);
					return;
				}				
				list = model.search(bean, pageNo, pageSize);
			}
			else if (OP_DELETE.equalsIgnoreCase(operation)) {
				String[] ids = request.getParameterValues("id");
				if (ids != null && ids.length > 0) {
					if (model.deleteCourses(ids)) {
						request.setAttribute("deleteRecordMsg", "Record(s) Successfully Deleted");
					} else {
						ServletUtility.setErrorMessage("Record(s) Cannot be Deleted", request);
					}
				}else{
					ServletUtility.setErrorMessage("Select at least One Record", request);
				}
				list = model.search(bean, pageNo, pageSize);
			}
			else if (OP_SEARCH.equalsIgnoreCase(operation) || OP_NEXT.equalsIgnoreCase(operation) || OP_PREVIOUS.equalsIgnoreCase(operation)) {
				request.setAttribute("courseNameSearch", request.getParameter("courseName"));
				request.setAttribute("courseDurationSearch", request.getParameter("courseDuration"));	
			}else {
				list = model.search(bean, pageNo, pageSize);
			} 
			list = model.search(bean, pageNo, pageSize);
			// ServletUtility.setList(list, request);

			//System.out.println(list);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
		//	ServletUtility.setList(list, request);
	//		ServletUtility.setPageNo(pageNo, request);
	//		ServletUtility.setPageSize(pageSize, request);		
			
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
	
	
	protected void preload(HttpServletRequest request) {
		log.debug("CourseListCtl Preload");	
		CourseModel model = new CourseModel();
		request.setAttribute("courseDurationList", model.getCourseDurationList());
	}

	@Override
	protected String getView() {

		return ORSView.COURSE_LIST_VIEW;
	}

}
