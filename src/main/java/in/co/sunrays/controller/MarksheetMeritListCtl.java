package in.co.sunrays.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.sunrays.beans.BaseBean;
import in.co.sunrays.beans.MarksheetBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.model.MarksheetModel;

import in.co.sunrays.util.ServletUtility;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Marksheet Merit List functionality Controller. Performance operation of
 * Marksheet Merit List
 *
 * @author Devashish Sharma
 * @version 1.0
 * @Copyright (c) SunilOS
 */

@WebServlet("/ctl/MarksheetMeritListCtl")
public class MarksheetMeritListCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(MarksheetMeritListCtl.class);

	public MarksheetMeritListCtl() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String operation = request.getParameter("operation");
		log.debug("Operation " + operation);
		try {
			int pageNo = 1;
			MarksheetBean bean = (MarksheetBean) populateBean(request);
			MarksheetModel model = new MarksheetModel();
			ArrayList<MarksheetBean> list = null;

			list = model.getMeritList(pageNo, ServletUtility.getPageSize());

			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			request.setAttribute("list", list);
			request.setAttribute("pageNo", 1);
			request.setAttribute("recordCount", list.size());
			ServletUtility.forward(ORSView.MARKSHEET_MERIT_LIST_VIEW, request, response);
		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ArrayList<MarksheetBean> list = null;
		MarksheetModel model = new MarksheetModel();
		MarksheetBean bean = new MarksheetBean();		
		
		String operation = request.getParameter("operation");
		log.debug("Operation " + operation);
		request.setAttribute("operation", operation);
		
		bean = (MarksheetBean) populateBean(request);		
		
		try {
			
			int pageNo = ServletUtility.getPageNo(request);
			int pageSize = ServletUtility.getPageSize();
			
			if (OP_BACK.equalsIgnoreCase(operation)) {
				ServletUtility.redirect(ORSView.WELCOME_CTL, request, response);
				return;
			}
			list = model.getMeritList(pageNo, pageSize);
			request.setAttribute("list", list);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			request.setAttribute("list", list);
			request.setAttribute("pageNo", 1);
			request.setAttribute("recordCount", list.size());

			ServletUtility.forward(ORSView.MARKSHEET_MERIT_LIST_VIEW, request, response);
		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
		
	}

	protected BaseBean populateBean(HttpServletRequest request) {
		MarksheetBean bean = new MarksheetBean();
		return bean;
	}

	@Override
	protected String getView() {
		return ORSView.MARKSHEET_MERIT_LIST_VIEW;
	}

}
