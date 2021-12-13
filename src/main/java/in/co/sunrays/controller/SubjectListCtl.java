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
import in.co.sunrays.beans.SubjectBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.model.CourseModel;
import in.co.sunrays.model.SubjectModel;
import in.co.sunrays.util.DataUtility;
import in.co.sunrays.util.ServletUtility;

/**
 * 
 * Subject List functionality Controller. Performs operation for list, search
 * and delete operations of Subject
 * 
 * @author Devashish Sharma
 * @version 1.0
 * @Copyright (c) SunilOS
 *
 */

@WebServlet(name = "SubjectListCtl", urlPatterns = { "/ctl/SubjectListCtl" })
public class SubjectListCtl extends BaseCtl {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(SubjectListCtl.class);

	@Override
	protected void preload(HttpServletRequest request) {
		CourseModel model = new CourseModel();
		try {
			List list = (List) model.list();
			request.setAttribute("courseList", list);
		} catch (ApplicationException e) {

			e.printStackTrace();
		}
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		SubjectBean bean = new SubjectBean();
		bean.setName(DataUtility.getString(request.getParameter("subjectName")));
		bean.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
		return bean;
	}

	/**
	 * 
	 * Contains display logic
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		int pageNo = 1;
		int pageSize = ServletUtility.getPageSize();
		SubjectBean bean = (SubjectBean) populateBean(request);
		SubjectModel model = new SubjectModel();
		try {
			List<SubjectBean> list = model.search(bean, pageNo, pageSize);

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
		log.debug("SubjectListCtl doGet Ended");
	}

	/**
	 * 
	 * Contains Submit Logic
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		List<SubjectBean> list = null;
		SubjectModel model = new SubjectModel();
		SubjectBean bean = (SubjectBean) populateBean(request);
		
		String operation = DataUtility.getString(request.getParameter("operation"));
		log.debug("Operation " + operation);
				
		int pageNo = ServletUtility.getPageNo(request);
		int pageSize = ServletUtility.getPageSize();
		try {
			
			if (OP_ADD.equalsIgnoreCase(operation)) {
				if (request.getAttribute("addRecordMsg") == null) {			
					ServletUtility.redirect(ORSView.SUBJECT_CTL, request, response);
					return;
				}				
				list = model.search(bean, pageNo, pageSize);
			}
			else if (OP_DELETE.equalsIgnoreCase(operation)) {
				String[] ids = request.getParameterValues("id");
				if (ids != null && ids.length > 0) {
					if (model.deleteSubjects(ids)) {
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
				request.setAttribute("subjectNameSearch", request.getParameter("subjectName"));
				request.setAttribute("courseIdSearch", request.getParameter("courseId"));
				
				System.out.println("subjectName " +  request.getParameter("subjectName"));
				System.out.println("courseId " +  request.getParameter("courseId"));		
				
			}else {
				System.out.println("OP_SEARCH subjectName " +  request.getParameter("subjectName"));
				System.out.println("OP_SEARCH courseId " +  request.getParameter("courseId"));	
				list = model.search(bean, pageNo, pageSize);
			}
			System.out.println("else subjectName " +  request.getParameter("subjectName"));
			System.out.println("else courseId " +  request.getParameter("courseId"));	
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

		/*
		 * if (OP_SEARCH.equalsIgnoreCase(operation) ||
		 * OP_PREVIOUS.equalsIgnoreCase(operation) ||
		 * OP_NEXT.equalsIgnoreCase(operation)) { if
		 * (OP_SEARCH.equalsIgnoreCase(operation)) { pageNo = 1; } else if
		 * (OP_PREVIOUS.equalsIgnoreCase(operation)) { pageNo--; } else if
		 * (OP_NEXT.equalsIgnoreCase(operation)) { pageNo++; }
		 * 
		 * } else if (OP_NEW.equalsIgnoreCase(operation)) {
		 * ServletUtility.redirect(ORSView.SUBJECT_CTL, request, response); return; }
		 * else if (OP_DELETE.equalsIgnoreCase(operation)) { pageNo = 1; SubjectBean
		 * deletebean = new SubjectBean(); if (ids != null && ids.length > 0) { for
		 * (String id : ids) {
		 * 
		 * deletebean.setId(DataUtility.getLong(id)); try { model.delete(deletebean);
		 * ServletUtility.setSuccessMessage("Record Deleted Successfully", request); }
		 * catch (ApplicationException e) { log.error(e);
		 * ServletUtility.handleException(e, request, response);
		 * 
		 * }
		 * 
		 * } } else { ServletUtility.setErrorMessage("Select at least one record",
		 * request); } } try { list = model.search(bean, pageNo, pageSize); if (list ==
		 * null || list.size() == 0) {
		 * 
		 * ServletUtility.setErrorMessage("No Record Found", request); }
		 * ServletUtility.setList(list, request); ServletUtility.setBean(bean, request);
		 * ServletUtility.setPageNo(pageNo, request);
		 * ServletUtility.setPageSize(pageSize, request);
		 * ServletUtility.forward(getView(), request, response); } catch
		 * (ApplicationException e) {
		 * 
		 * log.error(e); ServletUtility.handleException(e, request, response); return; }
		 * 
		 */

	}

	@Override
	protected String getView() {

		return ORSView.SUBJECT_LIST_VIEW;
	}

}
