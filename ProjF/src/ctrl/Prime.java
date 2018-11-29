package ctrl;

import java.io.IOException;
import java.io.Writer;

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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String min, max, result;
		Engine model = Engine.getInstance();
		min = request.getParameter("min");
		max = request.getParameter("max");

		response.setContentType("text/json");
		response.setHeader("Access-Control-Allow-Origin", "*");
		Writer restOut = response.getWriter();

		try {
			result = model.doPrime(min, max);
			restOut.write("{\"status\":true, \"data\":" + result + "}");
		} catch (Exception e) {
			restOut.write("{\"status\":false, \"data\":\"" + e.getMessage() + "\"}");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
