package in.co.sunrays.controller;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.sunrays.beans.RoleBean;
import in.co.sunrays.beans.StudentBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.model.CollegeModel;
import in.co.sunrays.model.RoleModel;
import in.co.sunrays.model.StudentModel;
import in.co.sunrays.util.DataUtility;
import in.co.sunrays.util.ServletUtility;

@WebServlet("/ctl/RoleListCtl")
public class RoleListCtl extends BaseCtl {
	
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(RoleListCtl.class);
	
	public RoleListCtl() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RoleModel model = new RoleModel();
		ArrayList<RoleBean> list = null;
		int pageNo = 1;
		try {
			if (request.getParameter("pageNo") != null) {
				pageNo = Integer.parseInt(request.getParameter("pageNo"));
			}
			list = model.roleList(pageNo, ServletUtility.getPageSize());
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
		
		ArrayList<RoleBean> list = null;
		RoleModel model = new RoleModel();
		RoleBean bean = new RoleBean();
		
		String operation = request.getParameter("operation");
		log.debug("Operation " + operation);
		request.setAttribute("operation", operation);
		try {
			int pageNo = ServletUtility.getPageNo(request);
			int pageSize = ServletUtility.getPageSize();
			
			if (OP_ADD.equalsIgnoreCase(operation)) {
				if (request.getAttribute("addRecordMsg") == null) {
				//	ServletUtility.forward(ORSView.ROLE_VIEW, request, response);
					ServletUtility.redirect(ORSView.ROLE_CTL, request, response);
					return;
				}
				list = model.roleList(pageNo, pageSize);
			}
			else if (OP_DELETE.equalsIgnoreCase(operation)) {
				String[] ids = request.getParameterValues("id");
				if (ids != null && ids.length > 0) {
					if (model.deleteRoles(ids)) {
						request.setAttribute("deleteRecordMsg", "Record(s) Successfully Deleted");
					} else {
						ServletUtility.setErrorMessage("Record(s) Cannot be Deleted", request);
					}
				}else{
					ServletUtility.setErrorMessage("Select at least One Record", request);
				}
				list = model.roleList(pageNo, pageSize);
			}else if (OP_SEARCH.equalsIgnoreCase(operation) || OP_PREVIOUS.equalsIgnoreCase(operation)
					|| OP_NEXT.equalsIgnoreCase(operation)) {

				request.setAttribute("roleNameSearch", request.getParameter("roleName"));
				bean = populateBean(request);				
				list = model.search(bean, pageNo, pageSize);

				if (list == null || list.size() == 0) {
					ServletUtility.setErrorMessage("No Record Found", request);
				}
			}
			else {
				list = model.roleList(pageNo, pageSize);
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
		return ORSView.ROLE_LIST_VIEW;
	}

	protected boolean validate(HttpServletRequest request) {
		log.debug("RoleListCtl Validate");
		return true;
	}

	protected void preload(HttpServletRequest request) {
		log.debug("RoleListCtl Preload");		
	}

	public RoleBean populateBean(HttpServletRequest request) {
		log.debug("RoleListCtl populateBean");		
		RoleBean bean = new RoleBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));		
		bean.setName(DataUtility.getString(request.getParameter("roleName")));
		bean.setDescription(DataUtility.getString(request.getParameter("description")));
		return bean;
	}
	
}
