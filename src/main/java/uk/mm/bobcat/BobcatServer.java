package uk.mm.bobcat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import uk.mm.bobcat.config.RootConfiguration;

public class BobcatServer {
	private final Logger logger = LoggerFactory.getLogger(BobcatServer.class);

	public static void main(String[] args) {
		new BobcatServer().startup();
	}

	public void startup() {
		try {
			logger.info("Starting up server........");

			// do not close app-context automatically - this is our container!!
			@SuppressWarnings("resource")
			AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
			
			applicationContext.registerShutdownHook();
			applicationContext.register(RootConfiguration.class);
			applicationContext.refresh();
			logger.info("Server started.");
		} catch (Exception e) {
			logger.error("Error starting application", e);
			System.exit(1);
		}
	}
}