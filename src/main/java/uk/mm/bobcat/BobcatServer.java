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
		logger.info("Starting up server........");
		try (AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext()){
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