package analytics;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
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
	 * @see ServletRequestListener#requestInitialized(ServletRequestEvent)
	 */
	public void requestInitialized(ServletRequestEvent sre) {
		HttpServletRequest httpReq = (HttpServletRequest) sre.getServletRequest();

		if (httpReq.getParameter("calc") != null) {
			ServletContext context = sre.getServletContext();
			String pageVisted = httpReq.getRequestURL().toString();

			incrementUsage(context, pageVisted);
			calculateDroneRide(context);
		}
	}

	// Increments the total usage, checks if the used service was drone and
	// increments that too. Finally recalculates the percentage of drone usage and
	// stores it back in the context.
	private void incrementUsage(ServletContext context, String pageVisted) {
		int totalUsage = (int) context.getAttribute("totalUsage");
		int droneUsage = (int) context.getAttribute("droneUsage");
		double dronePercent = (double) context.getAttribute("dronePercent");

		if (pageVisted.matches(".*Drone.do"))
			context.setAttribute("droneUsage", ++droneUsage);

		context.setAttribute("totalUsage", ++totalUsage);

		dronePercent = ((double) droneUsage / (double) totalUsage) * 100;

		context.setAttribute("dronePercent", dronePercent);
	}

	/**
	 * @see HttpSessionAttributeListener#attributeAdded(HttpSessionBindingEvent)
	 */
	public void attributeAdded(HttpSessionBindingEvent se) {
		HttpSession session = se.getSession();

		if (session.getAttribute("usedDrone") != null && session.getAttribute("usedRide") != null
				&& session.getAttribute("addedDroneRideStat") == null)
			usedDroneRide(session);
	}

	// The user used both drone and ride as the variables are set. Increments people
	// who used both, sets that the session user has had their stat incremented, and
	// then calculates people who have used drone and ride.
	private void usedDroneRide(HttpSession session) {

		ServletContext context = session.getServletContext();
		int usedDroneRide = (int) context.getAttribute("sessionDroneRide");

		context.setAttribute("sessionDroneRide", ++usedDroneRide);
		session.setAttribute("addedDroneRideStat", true);

		calculateDroneRide(context);

	}

	// Calculates the ratio of sessions that both drone and ride with total
	// sessions. Called when the 'usedDrone/usedRide' attributes are added, and with
	// every service request.
	private void calculateDroneRide(ServletContext context) {
		int totalSessions = (int) context.getAttribute("totalSessions");
		int usedDroneRide = (int) context.getAttribute("sessionDroneRide");
		double droneRidePercent = (double) context.getAttribute("droneRidePercent");

		droneRidePercent = ((double) usedDroneRide / (double) totalSessions) * 100;
		context.setAttribute("droneRidePercent", droneRidePercent);
	}

}
