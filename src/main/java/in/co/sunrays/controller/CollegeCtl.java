package in.co.sunrays.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import in.co.sunrays.beans.CollegeBean;
import in.co.sunrays.beans.RoleBean;
import in.co.sunrays.beans.UserBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.exception.DuplicateRecordException;
import in.co.sunrays.model.CollegeModel;
import in.co.sunrays.model.RoleModel;
import in.co.sunrays.util.DataUtility;
import in.co.sunrays.util.DataValidator;
import in.co.sunrays.util.ServletUtility;

@WebServlet("/ctl/CollegeCtl")
public class CollegeCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(CollegeCtl.class);

	public CollegeCtl() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("Operation  " + request.getParameter("operation"));

		if (OP_EDIT.equalsIgnoreCase(request.getParameter("operation"))) {
			long collegeId = Long.parseLong(request.getParameter("id"));
			CollegeBean bean = null;
			CollegeModel model = new CollegeModel();
			try {
				bean = model.findByPK(collegeId);
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
		CollegeModel model = new CollegeModel();
		CollegeBean bean;
		bean = populateBean(request);
		try {
			if (OP_CANCEL.equalsIgnoreCase(request.getParameter("operation"))) {
				log.debug("In Cancel Operation");
				ServletUtility.forward(ServletUtility.trimRequestPattern(ORSView.COLLEGE_LIST_CTL), request, response);
				return;
			}
			if (OP_ADD.equalsIgnoreCase(request.getParameter("operation"))) {
				log.debug("In Add Operation");
				model.addStudent(bean);
				request.setAttribute("addRecordMsg", "Record Has Been Successfully Added");
				log.info("Record Has Been Successfully Added");
				ServletUtility.forward(ServletUtility.trimRequestPattern(ORSView.COLLEGE_LIST_CTL), request, response);
				return;
			}
			if (OP_SAVE.equalsIgnoreCase(request.getParameter("operation"))) {
				log.debug("In Update Operation");
				model.updateRole(bean);
				request.setAttribute("updateRecordMsg", "Record Has Been Successfully Updated");
				log.info("Record Has Been Successfully Updated");
				ServletUtility.forward(ServletUtility.trimRequestPattern(ORSView.COLLEGE_LIST_CTL), request, response);
				return;
			}

			long collegeId = Long.parseLong(DataUtility.getString(request.getParameter("id")));
			bean = model.findByPK(collegeId);
			request.setAttribute("bean", bean);
			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			log.error("Application Exception", e);
			ServletUtility.handleException(e, request, response);
			return;
		} catch (DuplicateRecordException e) {
			ServletUtility.setBean(bean, request);
			log.error("College Name Already Exists");
			ServletUtility.setErrorMessage("College Name Already Exists", request);
			ServletUtility.forward(getView(), request, response);
			return;
		}
	}

	protected void preload(HttpServletRequest request) {
		log.debug("CollegeCtl Preload");
	}

	public boolean validate(HttpServletRequest request) {
		boolean flag = true;

		log.debug("CollegeCtl Validate");

		if (DataValidator.isNull(request.getParameter("name"))) {
			flag = false;
			request.setAttribute("collegeNameErr", "College Name is Required");
		}
		if (DataValidator.isNull(request.getParameter("address"))) {
			flag = false;
			request.setAttribute("addressErr", "Address is Required");
		}
		if (DataValidator.isNull(request.getParameter("state"))) {
			flag = false;
			request.setAttribute("stateErr", "State is Required");
		}
		if (DataValidator.isNull(request.getParameter("city"))) {
			flag = false;
			request.setAttribute("cityErr", "City is Required");
		}
		if (DataValidator.isNull(request.getParameter("phoneNo"))) {
			flag = false;
			request.setAttribute("phoneNoErr", "Phone Number is Required");
		}else if (!DataValidator.isValidMobNo(DataUtility.getString(request.getParameter("phoneNo")))) {
			flag = false;
			request.setAttribute("phoneNoErr", "Please Enter Valid 10 Digit Mobile Number");
		}
		return flag;
	}

	public CollegeBean populateBean(HttpServletRequest request) {

		log.debug("CollegeCtl populateBean");
		CollegeBean bean = new CollegeBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setAddress(DataUtility.getString(request.getParameter("address")));
		bean.setState(DataUtility.getString(request.getParameter("state")));
		bean.setCity(DataUtility.getString(request.getParameter("city")));
		bean.setPhoneNo(DataUtility.getString(request.getParameter("phoneNo")));

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
		return ORSView.COLLEGE_VIEW;
	}

}
