package ctrl;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Engine;

/**
 * Servlet implementation class Prime
 */
@WebServlet("/Prime.do")
public class Prime extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getParameter("getPrime") != null || request.getParameter("next") != null) {
			Engine model = Engine.getEngine();

			if (request.getParameter("next") == null) {
				String firstNumber = request.getParameter("lessThan");
				String lastNumber = request.getParameter("greaterThan");

				request.setAttribute("lessThan", firstNumber);
				request.setAttribute("greaterThan", lastNumber);

				String result = model.doPrime(firstNumber, lastNumber);

				if (result == null) {
					request.setAttribute("error", "No Values in Range");
				} else {
					request.setAttribute("result", result);
				}
			} else {
				
			}

		}

		this.getServletContext().getRequestDispatcher("/Prime.jspx").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}
}
