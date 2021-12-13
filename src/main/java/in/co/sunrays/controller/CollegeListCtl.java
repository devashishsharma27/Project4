package in.co.sunrays.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.sunrays.beans.CollegeBean;
import in.co.sunrays.beans.RoleBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.model.CollegeModel;
import in.co.sunrays.model.RoleModel;
import in.co.sunrays.util.DataUtility;
import in.co.sunrays.util.ServletUtility;

@WebServlet("/ctl/CollegeListCtl")
public class CollegeListCtl extends BaseCtl{
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(CollegeListCtl.class);
    
	public CollegeListCtl() {
        super();
      
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CollegeModel model = new CollegeModel();
		ArrayList<CollegeBean> list = null;
		int pageNo = 1;
		try {
			if (request.getParameter("pageNo") != null) {
				pageNo = Integer.parseInt(request.getParameter("pageNo"));
			}
			list = model.collegeList(pageNo, ServletUtility.getPageSize());
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
		
		ArrayList<CollegeBean> list = null;
		CollegeModel model = new CollegeModel();
		CollegeBean bean = new CollegeBean();
		
		String operation = request.getParameter("operation");
		log.debug("Operation " + operation);
		request.setAttribute("operation", operation);
		try {
			int pageNo = ServletUtility.getPageNo(request);
			int pageSize = ServletUtility.getPageSize();
			
			if (OP_ADD.equalsIgnoreCase(operation)) {
				if (request.getAttribute("addRecordMsg") == null) {
				//	ServletUtility.forward(ORSView.COLLEGE_VIEW, request, response);
					ServletUtility.redirect(ORSView.COLLEGE_CTL, request, response);
					return;
				}
				list = model.collegeList(pageNo, pageSize);
			}
			else if (OP_DELETE.equalsIgnoreCase(operation)) {
				String[] ids = request.getParameterValues("id");
				if (ids != null && ids.length > 0) {
					if (model.deleteColleges(ids)) {
						request.setAttribute("deleteRecordMsg", "Record(s) Successfully Deleted");
					} else {
						ServletUtility.setErrorMessage("Record(s) Cannot be Deleted", request);
					}
				}else{
					ServletUtility.setErrorMessage("Select at least One Record", request);
				}
				list = model.collegeList(pageNo, pageSize);
			}else if (OP_SEARCH.equalsIgnoreCase(operation) || OP_PREVIOUS.equalsIgnoreCase(operation)
					|| OP_NEXT.equalsIgnoreCase(operation)) {

				request.setAttribute("collegeNameSearch", request.getParameter("collegeName"));
				request.setAttribute("stateSearch", request.getParameter("state"));
				request.setAttribute("citySearch", request.getParameter("city"));
				
				bean = populateBean(request);				
				list = model.search(bean, pageNo, pageSize);

				if (list == null || list.size() == 0) {
					ServletUtility.setErrorMessage("No Record Found", request);
				}
			}
			else {
				list = model.collegeList(pageNo, pageSize);
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
		return ORSView.COLLEGE_LIST_VIEW;
	}

	protected boolean validate(HttpServletRequest request) {
		log.debug("CollegeListCtl Validate");
		return true;
	}

	protected void preload(HttpServletRequest request) {
		log.debug("CollegeListCtl Preload");		
	}

	public CollegeBean populateBean(HttpServletRequest request) {
		log.debug("CollegeListCtl populateBean");		
		CollegeBean bean = new CollegeBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setAddress(DataUtility.getString(request.getParameter("address")));
		bean.setState(DataUtility.getString(request.getParameter("state")));
		bean.setCity(DataUtility.getString(request.getParameter("city")));
		bean.setPhoneNo(DataUtility.getString(request.getParameter("phoneNo")));
		return bean;
	}
	
}