package in.co.sunrays.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import in.co.sunrays.util.ServletUtility;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;

@WebFilter(filterName = "FrontCtl", urlPatterns = "/ctl/*")

public class FrontController implements Filter {
	private static final long serialVersionUID = 1L;

	public FrontController() {
		super();
	}

	/*
	 * This method is called only once in the lifecycle of filter and we should
	 * initialize any resources in this method. FilterConfig is used by container to
	 * provide init parameters and servlet context object to the Filter.
	 */

	public void init(FilterConfig filterConfig) throws ServletException {

	}

	/*
	 * This is the method invoked every time by container when it has to apply
	 * filter to a resource. Container provides request and response object
	 * references to filter as argument. FilterChain is used to invoke the next
	 * filter in the chain
	 */
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpSession session = request.getSession(true);
		System.out.println("----Front Controller-----------");
		if (session.getAttribute("user") == null) {
			request.setAttribute("requestedURI", request.getRequestURI());
			request.setAttribute("sessionExpiredMessage", "Your Session Has Been Expired. Please Login Again.");
			ServletUtility.forward(ServletUtility.trimRequestPattern(ORSView.LOGIN_CTL), request, response);
		} else {
			chain.doFilter(req, resp);
		}
	}

	/*
	 * When container offloads the Filter instance, it invokes the destroy() method.
	 * This is the method where we can close any resources opened by filter. This
	 * method is called only once in the lifetime of filter.
	 */
	public void destroy() {
	}
}