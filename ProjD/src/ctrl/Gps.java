package ctrl;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Engine;

/**
 * Servlet implementation class Gps
 */
@WebServlet("/Gps.do")
public class Gps extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getParameter("getGps") != null) {
			Engine model = Engine.getEngine();

			String fromLat = request.getParameter("fromLat");
			String fromLong = request.getParameter("fromLong");
			String toLat = request.getParameter("toLat");
			String toLong = request.getParameter("toLong");

			request.setAttribute("fromLat", fromLat);
			request.setAttribute("fromLong", fromLong);
			request.setAttribute("toLat", toLat);
			request.setAttribute("toLong", toLong);

			try {
				double gps = model.doGps(fromLat, fromLong, toLat, toLong);
				request.setAttribute("result", gps);
			} catch (NumberFormatException e) {
				request.setAttribute("error", e.getMessage());
			}
		}
		this.getServletContext().getRequestDispatcher("/Gps.jspx").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}
}
