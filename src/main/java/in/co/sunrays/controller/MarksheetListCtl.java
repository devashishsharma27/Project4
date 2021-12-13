package in.co.sunrays.controller;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import in.co.sunrays.beans.MarksheetBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.model.MarksheetModel;
import in.co.sunrays.util.DataUtility;
import in.co.sunrays.util.ServletUtility;

/**
 * Marksheet List functionality Controller. Performs operation for list, search
 * and delete operations of Marksheet
 *
 * @author Devashish Sharma
 * @version 1.0
 * @Copyright (c) SunilOS
 */

/**
 * Servlet implementation class MarksheetlistCtl
 */
@WebServlet("/ctl/MarksheetListCtl")
public class MarksheetListCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(MarksheetListCtl.class);  
    
    public MarksheetListCtl() {
        super();
      
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
    	MarksheetModel model = new MarksheetModel();
		ArrayList<MarksheetBean> list = null;
		int pageNo = 1;
		try {
			if (request.getParameter("pageNo") != null) {
				pageNo = Integer.parseInt(request.getParameter("pageNo"));
			}
			list = model.marksheetList(pageNo, ServletUtility.getPageSize());
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
		
		ArrayList<MarksheetBean> list = null;
		MarksheetModel model = new MarksheetModel();
		MarksheetBean bean = new MarksheetBean();
		
		String operation = request.getParameter("operation");
		log.debug("Operation " + operation);
		request.setAttribute("operation", operation);
		try {
			int pageNo = ServletUtility.getPageNo(request);
			int pageSize = ServletUtility.getPageSize();
			
			if (OP_ADD.equalsIgnoreCase(operation)) {
				if (request.getAttribute("addRecordMsg") == null) {
				//	ServletUtility.forward(ServletUtility.trimRequestPattern(ORSView.MARKSHEET_CTL), request, response);
					ServletUtility.redirect(ORSView.MARKSHEET_CTL, request, response);
					return;
				}
				list = model.marksheetList(pageNo, pageSize);
			}
			else if (OP_DELETE.equalsIgnoreCase(operation)) {
				String[] ids = request.getParameterValues("id");
				if (ids != null && ids.length > 0) {
					if (model.deleteMarksheets(ids)) {
						request.setAttribute("deleteRecordMsg", "Record(s) Successfully Deleted");
					} else {
						ServletUtility.setErrorMessage("Record(s) Cannot be Deleted", request);
					}
				}else{
					ServletUtility.setErrorMessage("Select at least One Record", request);
				}
				list = model.marksheetList(pageNo, pageSize);
			}else if (OP_SEARCH.equalsIgnoreCase(operation) || OP_PREVIOUS.equalsIgnoreCase(operation)
					|| OP_NEXT.equalsIgnoreCase(operation)) {

				request.setAttribute("studentNameSearch", request.getParameter("name"));
				request.setAttribute("rollNoSearch", request.getParameter("rollNo"));
				
				bean = populateBean(request);				
				list = model.search(bean, pageNo, pageSize);

				if (list == null || list.size() == 0) {
					ServletUtility.setErrorMessage("No Record Found", request);
				}
			}
			else {
				list = model.marksheetList(pageNo, pageSize);
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


	@Override
	protected String getView() {
		return ORSView.MARKSHEET_LIST_VIEW;
	}

	protected boolean validate(HttpServletRequest request) {
		log.debug("MarksheetListCtl Validate");
		return true;
	}

	protected void preload(HttpServletRequest request) {
		log.debug("MarksheetListCtl Preload");		
	}

	public MarksheetBean populateBean(HttpServletRequest request) {
		log.debug("MarksheetListCtl populateBean");
		MarksheetBean bean = new MarksheetBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setStudentId(DataUtility.getLong(request.getParameter("studentId")));
		bean.setRollNo(DataUtility.getString(request.getParameter("rollNo")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setPhyMarks(DataUtility.getInt(request.getParameter("phyMarks")));
		bean.setChemMarks(DataUtility.getInt(request.getParameter("chemMarks")));
		bean.setMathMarks(DataUtility.getInt(request.getParameter("mathMarks")));		
		return bean;
	}
	
}