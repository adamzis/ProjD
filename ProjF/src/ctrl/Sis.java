package ctrl;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import model.Engine;
import model.StudentBean;

/**
 * Servlet implementation class Sis
 */
@WebServlet("/Sis.do")
public class Sis extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String prefix, minGpa, sortBy, jsonResult;
		List<StudentBean> result;
		Engine model = Engine.getInstance();

		prefix = request.getParameter("prefix");
		minGpa = request.getParameter("minGpa");
		sortBy = request.getParameter("sortBy");

		response.setContentType("text/json");
		response.setHeader("Access-Control-Allow-Origin", "*");

		Writer restOut = response.getWriter();
		Gson gson = new Gson();

		try {
			result = model.doSis(prefix, minGpa, sortBy);
			jsonResult = gson.toJson(result);
			restOut.write("{\"status\":true, \"data\":" + jsonResult + "}");
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
		doGet(request, response);
	}

}
