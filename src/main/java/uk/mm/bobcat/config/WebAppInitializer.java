package uk.mm.bobcat.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer
		implements ServletContextListener {

	/**
	 * See {@link org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer}.
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return null;
	}

	/**
	/**
	 * Set the application context for the Spring MVC web tier.
	 *
	 * @See {@link org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer}
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { MvcConfiguration.class };
	}

	/**
	 * Map the Spring MVC servlet as the root.
	 *
	 * @See {@link org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer}
	 */
	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext);
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		try {
			onStartup(servletContextEvent.getServletContext());
		} catch (ServletException e) {
			logger.error("Failed to initialize web application", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		// do nothing
	}

	/**
	 * Override to squelch a meaningless log message when embedded.
	 */
	@Override
	protected void registerContextLoaderListener(ServletContext servletContext) {
		// do nothing
	}
}