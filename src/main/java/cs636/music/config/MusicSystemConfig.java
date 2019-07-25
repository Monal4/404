package cs636.music.config;

import java.io.PrintWriter;
import java.io.StringWriter;

import cs636.music.service.*;
/**
 * @author Betty O'Neil
 *
 *         No need to configure the service objects: Spring does that for us
 *         Just a few utilities left.
 * 
 */

public class MusicSystemConfig {
	public static final String SOUND_BASE_URL = "http://www.cs.umb.edu/cs636/music1-setup/sound/";

	private static CatalogService catalogservice;
	private static SalesService salesservice;
	
//	public static final int Max_Quantity = 10;
	
	// Compose an exception report
	// and return the string for callers to use
	public static String exceptionReport(Exception e) {
		String message = e.toString(); // exception name + message
		if (e.getCause() != null) {
			message += "\n  cause: " + e.getCause().toString();
			if (e.getCause().getCause() != null)
				message += "\n    cause's cause: "
						+ e.getCause().getCause().toString();
		}
		return message + exceptionStackTraceString(e);
	}
	
	private static String exceptionStackTraceString(Throwable e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
	
	public static CatalogService getCatalogService() {
		return catalogservice;
	}
	
	public static SalesService getSalesService() {
		return salesservice;
	}
	
//	public static void configureCatalogService(String catalogdb)
//			throws Exception {
//		// Configure service layer and DAO objects, all singletons--
//		// DAOs first, then service objects, for DI (dependency injection),
//		// a form of IoC (inversion of control)
//		// The service objects get what they need at creation-time
//		// This is known as "constructor injection"
//		try {
//			dbDAO = new DbDAO(dbUrl, usr, psswd);
//			adminDAO = new AdminDAO(dbDAO);
//			menuDAO = new MenuDAO(dbDAO);
//			pizzaOrderDAO = new PizzaOrderDAO(dbDAO, menuDAO);
//			adminService = new AdminService(dbDAO, adminDAO, pizzaOrderDAO, menuDAO);
//			studentService = new StudentService(pizzaOrderDAO, menuDAO, adminDAO);
//		} catch (Exception e) {
//			System.out.println(exceptionReport(e));
//			// e.printStackTrace(); // causes lots of output
//			System.out.println("Problem with contacting DB: " + e);
//			throw (e); // rethrow to notify caller
//		}
//	}

}
