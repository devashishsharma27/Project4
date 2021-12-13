package in.co.sunrays.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.sunrays.util.ServletUtility;

@WebServlet("/ctl/ORSErrorHandlerCtl")
public class ORSErrorHandlerCtl extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ORSErrorHandlerCtl() {
        super();

    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ServletUtility.forward(getView(), request, response);
		
	}

	private String getView() {
		
		return ORSView.ERROR_VIEW;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
