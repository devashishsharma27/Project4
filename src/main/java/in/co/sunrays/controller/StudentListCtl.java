package in.co.sunrays.controller;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import in.co.sunrays.beans.StudentBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.model.CollegeModel;
import in.co.sunrays.model.StudentModel;
import in.co.sunrays.util.DataUtility;
import in.co.sunrays.util.ServletUtility;

/**
 * Student List functionality Controller. Performs operation for list, search
 * and delete operations of Student
 *
 * @author  Devashish Sharma
 * @version 1.0
 * @Copyright (c) SunilOS
 */

@WebServlet("/ctl/StudentListCtl")
public class StudentListCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;
/*	public static final String OP_ADD = "Add";
	public static final String OP_DELETE = "Delete";
	public static final String OP_SEARCH = "Search";
	public static final String OP_PREVIOUS = "Previous";
	public static final String OP_NEXT = "Next";
*/
	private static Logger log = Logger.getLogger(StudentListCtl.class);

	public StudentListCtl() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		StudentModel model = new StudentModel();
		ArrayList<StudentBean> list = null;
		int pageNo = 1;
		try {
			if (request.getParameter("pageNo") != null) {
				pageNo = Integer.parseInt(request.getParameter("pageNo"));
			}
			list = model.studentList(pageNo, ServletUtility.getPageSize());
			request.setAttribute("list", list);
			request.setAttribute("pageNo", 1);
			request.setAttribute("pageSize", ServletUtility.getPageSize());
			request.setAttribute("recordCount", list.size());
			ServletUtility.forward(getView(), request, response);
			return;
		} catch (ApplicationException e) {
			e.printStackTrace();
			ServletUtility.handleException(e, request, response);
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ArrayList<StudentBean> list = null;
		StudentModel model = new StudentModel();
		StudentBean bean = new StudentBean();
		
		String operation = request.getParameter("operation");
		log.debug("Operation " + operation);
		request.setAttribute("operation", operation);
		try {
		//	int pageNo = getPageNo(request);
			int pageNo = ServletUtility.getPageNo(request);
			int pageSize = ServletUtility.getPageSize();
			
			if (OP_ADD.equalsIgnoreCase(operation)) {
				if (request.getAttribute("addRecordMsg") == null) {
				//	ServletUtility.forward(ORSView.STUDENT_VIEW, request, response);
					ServletUtility.redirect(ORSView.STUDENT_CTL, request, response);
					return;
				}
				list = model.studentList(pageNo, pageSize);
			}
			else if (OP_DELETE.equalsIgnoreCase(operation)) {
				String[] ids = request.getParameterValues("id");
				if (ids != null && ids.length > 0) {
					if (model.deleteStudents(ids)) {
						request.setAttribute("deleteRecordMsg", "Record(s) Successfully Deleted");
						// ServletUtility.setSuccessMessage("Record(s) Successfully Deleted", request);
					} else {
						ServletUtility.setErrorMessage("Record(s) Cannot be Deleted", request);
					}
				}else{
					ServletUtility.setErrorMessage("Select at least One Record", request);
				}
				list = model.studentList(pageNo, pageSize);
			}else if (OP_SEARCH.equalsIgnoreCase(operation) || OP_PREVIOUS.equalsIgnoreCase(operation)
					|| OP_NEXT.equalsIgnoreCase(operation)) {

				request.setAttribute("fNameSearch", request.getParameter("firstName"));
				request.setAttribute("lNameSearch", request.getParameter("lastName"));
				request.setAttribute("clgNameSearch", request.getParameter("collegeName"));

				bean = populateBean(request);
				list = model.search(bean, pageNo, pageSize);

				if (list == null || list.size() == 0) {
					ServletUtility.setErrorMessage("No Record Found", request);
				}
			}
			else {
				list = model.studentList(pageNo, pageSize);
			}
		} catch (ApplicationException e) {
			log.error("Exception in " + operation +" Operation", e);
			ServletUtility.handleException(e, request, response);
			return;
		}
		
		request.setAttribute("list", list);
		request.setAttribute("recordCount", list.size());
		request.setAttribute("pageSize", ServletUtility.getPageSize());
		ServletUtility.forward(getView(), request, response);
		return;
	}
/*
	protected void test(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ArrayList<StudentBean> list = null;
		StudentModel model = new StudentModel();
		StudentBean bean = new StudentBean();
		int pageNo = getPageNo(request);
		int pageSize = ServletUtility.getPageSize();

		String operation = request.getParameter("operation");
		request.setAttribute("operation", operation);
		log.debug("Operation " + operation);

		if (OP_ADD.equalsIgnoreCase(operation)) {
			try {
				if (request.getAttribute("addRecordMsg") == null) {
					ServletUtility.forward(ORSView.STUDENT_VIEW, request, response);
					return;
				}
				list = model.studentList(pageNo, pageSize);
			} catch (ApplicationException e) {
				ServletUtility.handleException(e, request, response);
				return;
			}
		}

		else if (OP_DELETE.equalsIgnoreCase(operation)) {
			System.out.println(" Start Operation  " + operation);
			try {

				String[] ids = request.getParameterValues("id");

				if (ids != null && ids.length > 0) {
					if (model.deleteStudents(ids)) {
						request.setAttribute("deleteRecordMsg", "Record(s) Successfully Deleted");
						// ServletUtility.setSuccessMessage("Record(s) Successfully Deleted", request);
					} else {
						ServletUtility.setErrorMessage("Record(s) Cannot be Deleted", request);
					}

				} else {
					ServletUtility.setErrorMessage("Select at least One Record", request);
				}
				list = model.studentList(pageNo, pageSize);
			} catch (ApplicationException e) {
				System.out.println("Exception in Delete Operation");
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_SEARCH.equalsIgnoreCase(operation) || OP_PREVIOUS.equalsIgnoreCase(operation)
				|| OP_NEXT.equalsIgnoreCase(operation)) {
			try {

				request.setAttribute("fNameSearch", request.getParameter("firstName"));
				request.setAttribute("lNameSearch", request.getParameter("lastName"));
				request.setAttribute("clgNameSearch", request.getParameter("collegeName"));

				bean = populateBean(request);
				list = model.search(bean, pageNo, pageSize);

				if (list == null || list.size() == 0) {
					ServletUtility.setErrorMessage("No Record Found", request);
				}
			} catch (ApplicationException e) {
				ServletUtility.handleException(e, request, response);
				return;
			}
		}

		else {
			try {
				list = model.studentList(pageNo, pageSize);
			} catch (ApplicationException e) {
				ServletUtility.handleException(e, request, response);
				return;
			}
		}
		request.setAttribute("list", list);
		request.setAttribute("recordCount", list.size());
		ServletUtility.forward(getView(), request, response);
		return;
	}
*/
	@Override
	protected String getView() {

		return ORSView.STUDENT_LIST_VIEW;
	}

	protected boolean validate(HttpServletRequest request) {
		log.debug("StudentListCtl Validate");
		return true;
	}

	protected void preload(HttpServletRequest request) {
		log.debug("StudentListCtl Preload");
		CollegeModel clgModel = new CollegeModel();
		request.setAttribute("collegeList", clgModel.getCollegeList());
	}

	public StudentBean populateBean(HttpServletRequest request) {
		log.debug("StudentListCtl populateBean");
		/*
		 * String studentId = request.getParameter("studentId"); String collegeId =
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
		 * 
		 * String operation = request.getParameter("operation");
		 * System.out.println(" Operation  " + operation);
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
		return bean;
	}

	public int getPageNo(HttpServletRequest request) {
		int pageNo = 1;
		String operation = request.getParameter("operation");

		if (request.getParameter("pageNo") == null || request.getParameter("operation") == null
				|| OP_SEARCH.equalsIgnoreCase(operation)) {
			pageNo = 1;
		} else {
			pageNo = Integer.parseInt(request.getParameter("pageNo"));
		}

		if (OP_PREVIOUS.equalsIgnoreCase(operation)) {
			pageNo = (pageNo > 1) ? pageNo - 1 : 1;
		}

		if (OP_NEXT.equalsIgnoreCase(operation)) {
			int recordCount = Integer.parseInt(request.getParameter("recordCount"));
			pageNo = (recordCount < 10) ? pageNo : pageNo + 1;
		}
		request.setAttribute("pageNo", pageNo);
		return pageNo;
	}
}
