package ctrl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Engine;
import model.StudentBean;

/**
 * Servlet implementation class Sis
 */
@WebServlet("/Sis.do")
public class Sis extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getParameter("calc") != null) {
			Engine model = Engine.getEngine();

			String namePre = request.getParameter("namePre");
			String minGpa = request.getParameter("minGpa");
			String sortBy = request.getParameter("sortBy");

			try {
				List<StudentBean> result = model.doSis(namePre, minGpa);
				request.setAttribute("result", result);
			} catch (Exception e) {
				request.setAttribute("error", e.getMessage());
			}

			request.setAttribute("namePre", namePre);
			request.setAttribute("minGpa", minGpa);
			request.setAttribute("sortBy", sortBy);
		}

		this.getServletContext().getRequestDispatcher("/Sis.jspx").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
