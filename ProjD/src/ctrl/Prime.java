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

		if (request.getParameter("calc") != null || request.getParameter("next") != null) {
			Engine model = Engine.getEngine();

			String firstNumber, lastNumber, result;

			if (request.getParameter("next") != null && request.getParameter("result") != null)
				firstNumber = request.getParameter("result");
			else
				firstNumber = request.getParameter("lessThan");

			lastNumber = request.getParameter("greaterThan");

			try {
				result = model.doPrime(firstNumber, lastNumber);
				request.setAttribute("result", result);	
			} catch (Exception e) {
				request.setAttribute("error", e.getMessage());
			}

			request.setAttribute("lessThan", firstNumber);
			request.setAttribute("greaterThan", lastNumber);

		}

		this.getServletContext().getRequestDispatcher("/Prime.jspx").forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}
}
