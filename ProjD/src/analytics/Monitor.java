package analytics;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Application Lifecycle Listener implementation class Monitor
 *
 */
@WebListener
public class Monitor implements ServletContextListener, ServletContextAttributeListener, ServletRequestListener,
		ServletRequestAttributeListener, HttpSessionAttributeListener, HttpSessionListener {

	/**
	 * Default constructor.
	 */
	public Monitor() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see ServletContextAttributeListener#attributeAdded(ServletContextAttributeEvent)
	 */
	public void attributeAdded(ServletContextAttributeEvent scae) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see ServletRequestListener#requestDestroyed(ServletRequestEvent)
	 */
	public void requestDestroyed(ServletRequestEvent sre) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see ServletContextAttributeListener#attributeRemoved(ServletContextAttributeEvent)
	 */
	public void attributeRemoved(ServletContextAttributeEvent scae) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see ServletRequestAttributeListener#attributeRemoved(ServletRequestAttributeEvent)
	 */
	public void attributeRemoved(ServletRequestAttributeEvent srae) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see ServletRequestListener#requestInitialized(ServletRequestEvent)
	 */
	public void requestInitialized(ServletRequestEvent sre) {
		HttpServletRequest httpReq = (HttpServletRequest) sre.getServletRequest();

		if (httpReq.getParameter("calc") != null) {
			ServletContext context = sre.getServletContext();
			String pageVisted = httpReq.getRequestURL().toString();

			int totalUsage = (int) context.getAttribute("totalUsage");
			int droneUsage = (int) context.getAttribute("droneUsage");
			double dronePercent = (double) context.getAttribute("dronePercent");

			sre.getServletContext().setAttribute("totalUsage", ++totalUsage);

			if (pageVisted.matches(".*Drone.do")) {
				context.setAttribute("droneUsage", ++droneUsage);
			}

			dronePercent = ((double) droneUsage / (double) totalUsage) * 100;
			context.setAttribute("dronePercent", dronePercent);

			int totalSessions = (int) context.getAttribute("totalSessions");
			int usedDroneRide = (int) context.getAttribute("sessionDroneRide");
			double droneRidePercent = (double) context.getAttribute("droneRidePercent");

			droneRidePercent = ((double) usedDroneRide / (double) totalSessions) * 100;
			context.setAttribute("droneRidePercent", droneRidePercent);
		}
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent sce) {

	}

	/**
	 * @see ServletRequestAttributeListener#attributeAdded(ServletRequestAttributeEvent)
	 */
	public void attributeAdded(ServletRequestAttributeEvent srae) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see ServletRequestAttributeListener#attributeReplaced(ServletRequestAttributeEvent)
	 */
	public void attributeReplaced(ServletRequestAttributeEvent srae) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see ServletContextAttributeListener#attributeReplaced(ServletContextAttributeEvent)
	 */
	public void attributeReplaced(ServletContextAttributeEvent scae) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();

		context.setAttribute("totalUsage", 0);
		context.setAttribute("droneUsage", 0);
		context.setAttribute("dronePercent", 0.0);

		context.setAttribute("totalSessions", 0);
		context.setAttribute("sessionDroneRide", 0);
		context.setAttribute("droneRidePercent", 0.0);

	}

	/**
	 * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
	 */
	public void sessionCreated(HttpSessionEvent se) {
		ServletContext context = se.getSession().getServletContext();
		int totalSessions = (int) context.getAttribute("totalSessions");

		context.setAttribute("totalSessions", ++totalSessions);
	}

	/**
	 * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
	 */
	public void sessionDestroyed(HttpSessionEvent se) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpSessionAttributeListener#attributeAdded(HttpSessionBindingEvent)
	 */
	public void attributeAdded(HttpSessionBindingEvent se) {
		HttpSession session = se.getSession();

		if (session.getAttribute("usedDrone") != null && session.getAttribute("usedRide") != null
				&& session.getAttribute("addedDroneRideStat") == null) {

			boolean usedDrone = (boolean) session.getAttribute("usedDrone");
			boolean usedRide = (boolean) session.getAttribute("usedRide");

			if (usedDrone && usedRide) {
				ServletContext context = session.getServletContext();
				int usedDroneRide = (int) context.getAttribute("sessionDroneRide");
				int totalSessions = (int) context.getAttribute("totalSessions");
				double droneRidePercent = (double) context.getAttribute("droneRidePercent");

				context.setAttribute("sessionDroneRide", ++usedDroneRide);
				session.setAttribute("addedDroneRideStat", true);

				droneRidePercent = ((double) usedDroneRide / (double) totalSessions) * 100;
				context.setAttribute("droneRidePercent", droneRidePercent);
			}
		}
	}

	/**
	 * @see HttpSessionAttributeListener#attributeRemoved(HttpSessionBindingEvent)
	 */
	public void attributeRemoved(HttpSessionBindingEvent se) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpSessionAttributeListener#attributeReplaced(HttpSessionBindingEvent)
	 */
	public void attributeReplaced(HttpSessionBindingEvent se) {
		// TODO Auto-generated method stub
	}
}
