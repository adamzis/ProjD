package ctrl;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Admin
 */
@WebServlet("/Admin")
public class Admin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext context = this.getServletContext();

		request.setAttribute("totalUsage", context.getAttribute("totalUsage"));
		request.setAttribute("droneUsage", context.getAttribute("droneUsage"));
		request.setAttribute("dronePercent", context.getAttribute("dronePercent"));

		request.setAttribute("totalSessions", context.getAttribute("totalSessions"));
		request.setAttribute("sessionDroneRide", context.getAttribute("sessionDroneRide"));
		request.setAttribute("droneRidePercent", context.getAttribute("droneRidePercent"));

		context.getRequestDispatcher("/Admin.jspx").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
