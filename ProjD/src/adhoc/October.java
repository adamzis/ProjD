package adhoc;

import java.io.IOException;
import java.io.Writer;

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

	public static final String html = "<html><body><p>Feature unavailable <a href='Dash.do'>Back To Dashboard</a></p></body></html>";

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		Writer out = response.getWriter();
		if (httpRequest.getRequestURL().toString().matches(".*Sis.do")) {
			String sortBy = httpRequest.getParameter("sortBy");

			if (sortBy != null && !sortBy.matches("NONE"))
				out.write(html);
			else
				chain.doFilter(httpRequest, response);

		} else if (httpRequest.getRequestURL().toString().matches(".*Ride.do")) {
			out.write(html);
		} else {
			chain.doFilter(httpRequest, response);
		}

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
