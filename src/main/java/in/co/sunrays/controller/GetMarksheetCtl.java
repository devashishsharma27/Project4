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

import in.co.sunrays.beans.MarksheetBean;
import in.co.sunrays.beans.UserBean;
import in.co.sunrays.exception.ApplicationException;
import in.co.sunrays.exception.DuplicateRecordException;
import in.co.sunrays.model.MarksheetModel;
import in.co.sunrays.util.DataUtility;
import in.co.sunrays.util.DataValidator;
import in.co.sunrays.util.ServletUtility;

@WebServlet("/ctl/GetMarksheetCtl")
public class GetMarksheetCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(GetMarksheetCtl.class);
	
    
    public GetMarksheetCtl() {
        super();
       
    }

	
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		ServletUtility.forward(getView(), request, response);
	}

	
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String operation = request.getParameter("operation");
		log.debug("Operation  " + operation);
		request.setAttribute("operation", operation);		
		
		if (OP_CANCEL.equalsIgnoreCase(request.getParameter("operation"))) {
			log.debug("In Cancel Operation");		
			ServletUtility.redirect(ORSView.WELCOME_CTL, request, response);
			return;
		}
		
		MarksheetModel model = new MarksheetModel();
		MarksheetBean bean;
		bean = populateBean(request);
		try {
			
			if(validate(request)) {
				if (OP_GO.equalsIgnoreCase(request.getParameter("operation"))) {
					log.debug("In Get Marksheet Operation");
					MarksheetBean marksheet= model.getMarksheet(bean);
					if(marksheet==null) {
						ServletUtility.setErrorMessage("No Marksheet Found", request);
						log.info("No Roll Number found");
					}else {					
						request.setAttribute("collegeName", model.getCollegeName(marksheet));
					}
					request.setAttribute("bean", marksheet);				
					ServletUtility.forward(getView(), request,
							response);
					return;
				}
			}
			
			ServletUtility.forward(getView(), request,
					response);
			
			

		} catch (ApplicationException e) {
			log.error("Application Exception", e);
			ServletUtility.handleException(e, request, response);
			return;
		} 
	}

	
	public boolean validate(HttpServletRequest request) {
		boolean flag = true;

		log.debug("GetMarksheetCtl Validate");

		if (DataValidator.isNull(request.getParameter("rollNo"))) {
			flag = false;
			request.setAttribute("rollNoErr", "Roll No is Required");
		}
		
		return flag;
	}

	public MarksheetBean populateBean(HttpServletRequest request) {

		log.debug("GetMarksheetCtl populateBean");
		MarksheetBean bean = new MarksheetBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setRollNo(DataUtility.getString(request.getParameter("rollNo")));
		return bean;
	}

	
	@Override
	protected String getView() {
		return ORSView.GET_MARKSHEET_VIEW;
	}

}
