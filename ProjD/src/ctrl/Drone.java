package ctrl;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Engine;

/**
 * Servlet implementation class Drone
 */
@WebServlet("/Drone.do")
public class Drone extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getParameter("calc") != null) {
			Engine model = Engine.getEngine();

			String startAddr = request.getParameter("startAddr");
			String destAddr = request.getParameter("destAddr");
			HttpSession sn = request.getSession();
			sn.setAttribute("usedDrone", true);

			try {
				double result = model.doDrone(startAddr, destAddr);
				request.setAttribute("result", result);
			} catch (Exception e) {
				request.setAttribute("error", e.getMessage());
			}

			request.setAttribute("startAddr", startAddr);
			request.setAttribute("destAddr", destAddr);
		}

		this.getServletContext().getRequestDispatcher("/Drone.jspx").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}
}
