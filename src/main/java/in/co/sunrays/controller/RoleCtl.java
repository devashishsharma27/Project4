package in.co.sunrays.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import in.co.sunrays.beans.RoleBean;
import in.co.sunrays.beans.UserBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.exception.DuplicateRecordException;
import in.co.sunrays.model.RoleModel;
import in.co.sunrays.util.DataUtility;
import in.co.sunrays.util.DataValidator;
import in.co.sunrays.util.ServletUtility;

@WebServlet("/ctl/RoleCtl")
public class RoleCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(RoleCtl.class);

	public RoleCtl() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("Operation  " + request.getParameter("operation"));

		if (OP_EDIT.equalsIgnoreCase(request.getParameter("operation"))) {
			long roleId = Long.parseLong(request.getParameter("id"));
			RoleBean bean = null;
			RoleModel model = new RoleModel();
			try {
				bean = model.findByPK(roleId);
			} catch (ApplicationException e) {
				ServletUtility.handleException(e, request, response);
				return;
			}
			request.setAttribute("bean", bean);
		}
		ServletUtility.forward(getView(), request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String operation = request.getParameter("operation");
		log.debug("Operation  " + operation);
		request.setAttribute("operation", operation);
		RoleModel model = new RoleModel();
		RoleBean bean;
		bean = populateBean(request);
		try {
			if (OP_CANCEL.equalsIgnoreCase(request.getParameter("operation"))) {
				log.debug("In Cancel Operation");
				ServletUtility.forward(ServletUtility.trimRequestPattern(ORSView.ROLE_LIST_CTL), request, response);
				return;
			}
			if (OP_ADD.equalsIgnoreCase(request.getParameter("operation"))) {
				log.debug("In Add Operation");
				model.addStudent(bean);
				request.setAttribute("addRecordMsg", "Record Has Been Successfully Added");
				log.info("Record Has Been Successfully Added");
				ServletUtility.forward(ServletUtility.trimRequestPattern(ORSView.ROLE_LIST_CTL), request, response);
				return;
				}
			if (OP_SAVE.equalsIgnoreCase(request.getParameter("operation"))) {
				log.debug("In Update Operation");
				model.updateRole(bean);
				request.setAttribute("updateRecordMsg", "Record Has Been Successfully Updated");
				log.info("Record Has Been Successfully Updated");
				ServletUtility.forward(ServletUtility.trimRequestPattern(ORSView.ROLE_LIST_CTL), request, response);
				return;
				}

			long roleId = Long.parseLong(DataUtility.getString(request.getParameter("id")));
			bean = model.findByPK(roleId);
			request.setAttribute("bean", bean);
			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			log.error("Application Exception", e);
			ServletUtility.handleException(e, request, response);
			return;
		} catch (DuplicateRecordException e) {
			ServletUtility.setBean(bean, request);
			log.error("Role Name Already Exists");
			ServletUtility.setErrorMessage("Role Name Already Exists", request);
			ServletUtility.forward(getView(), request, response);
			return;
		}
	}

	protected void preload(HttpServletRequest request) {
		log.debug("RoleListCtl Preload");
	}

	public boolean validate(HttpServletRequest request) {
		boolean flag = true;

		log.debug("RoleListCtl Validate");
		
		if (DataValidator.isNull(request.getParameter("roleName"))) {
			flag = false;
			request.setAttribute("roleNameErr", "Role Name is Required");
		} else if (!DataValidator.isValidName(DataUtility.getString(request.getParameter("roleName")))) {
			flag = false;
			request.setAttribute("roleNameErr", "Please Enter Valid Role Name");
		}
		if (DataValidator.isNull(request.getParameter("description"))) {
			flag = false;
			request.setAttribute("descriptionErr", "Description is Required");
		} 
		return flag;
	}

	public RoleBean populateBean(HttpServletRequest request) {

		log.debug("RoleListCtl populateBean");

		RoleBean bean = new RoleBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setName(DataUtility.getString(request.getParameter("roleName")));
		bean.setDescription(DataUtility.getString(request.getParameter("description")));

		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");

		if (OP_ADD.equalsIgnoreCase(request.getParameter("operation"))) {
			bean.setCreatedBy(String.valueOf(user.getId()));
			bean.setModifiedBy(String.valueOf(user.getId()));
			bean.setCreatedDateTime(new Timestamp(new Date().getTime()));
			bean.setModifiedDateTime(new Timestamp(new Date().getTime()));
		}
		if (OP_SAVE.equalsIgnoreCase(request.getParameter("operation"))) {
			bean.setModifiedBy(String.valueOf(user.getId()));
			bean.setModifiedDateTime(new Timestamp(new Date().getTime()));
		}
		return bean;
	}

	@Override
	protected String getView() {
		return ORSView.ROLE_VIEW;
	}
}
