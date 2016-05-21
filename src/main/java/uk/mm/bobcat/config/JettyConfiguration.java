package uk.mm.bobcat.config;

import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

import java.io.IOException;

import static org.apache.commons.lang.StringUtils.startsWith;

/**
 * Configure the embedded Jetty server and the SpringMVC dispatcher servlet.
 */
@Configuration
public class JettyConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SecurityHandler securityHandler;

    @Autowired(required = false)
    private String webappResourcePath;

    @Value("${server.port:5555}")
    private int serverPort;

    @Value("${server.path:/}")
    private String serverPath;

    /**
     * Jetty Server bean.
     * <p>
     * Instantiate the Jetty server.
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server jettyServer() throws IOException {
        /* Create the server. */
        Server server = new Server();
        /* Create a basic connector. */

        ServerConnector httpConnector = new ServerConnector(server);
        httpConnector.setPort(serverPort);
        server.addConnector(httpConnector);

        server.setHandler(webAppContext());
        return server;
    }

    @Bean
    public WebAppContext webAppContext() throws IOException {
        WebAppContext ctx = new WebAppContext();
        ctx.setContextPath(ensureLeadingSlash(serverPath));
        ctx.setSecurityHandler(securityHandler);
        ctx.setWar(getResourcePath());

		/* Disable directory listings if no index.html is found. */
        ctx.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");

		/* Create the root web application context and set it as a servlet
		 * attribute so the dispatcher servlet can find it. */
        GenericWebApplicationContext webApplicationContext = new GenericWebApplicationContext();
        webApplicationContext.setParent(applicationContext);
        webApplicationContext.refresh();
        ctx.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, webApplicationContext);

        ctx.addEventListener(new WebAppInitializer());
        return ctx;
    }

    private String getResourcePath() throws IOException {
        if (webappResourcePath == null) {
            return new ClassPathResource("webapp").getURI().toString();
        }
        return webappResourcePath;
    }

    private String ensureLeadingSlash(String input) {
        return startsWith(input, "/")
                ? input
                : "/" + input;
    }
}
