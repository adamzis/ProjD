package adhoc;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet Filter implementation class October
 */
@WebFilter(urlPatterns = { "/Sis.do", "/Ride.do" })
public class October implements Filter {

	/**
	 * Default constructor.
	 */
	public October() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;

		if (httpRequest.getRequestURL().toString().matches(".*(Sis.do)")) {
			String sortBy = httpRequest.getParameter("sortBy");

			if (sortBy != null && !sortBy.matches("NONE")) {
				httpRequest.setAttribute("message", "Sorting is Unavailable");
				httpRequest.getServletContext().getRequestDispatcher("/Filter.jspx").forward(httpRequest, response);
			}

		} else if (httpRequest.getRequestURL().toString().matches(".*(Ride.do)")) {
			httpRequest.setAttribute("message", "Ride Functionality is Unavailable");
			httpRequest.getServletContext().getRequestDispatcher("/Filter.jspx").forward(httpRequest, response);
		}

		chain.doFilter(httpRequest, response);

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
