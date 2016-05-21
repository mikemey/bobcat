package uk.mm.bobcat.config;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.authentication.FormAuthenticator;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import uk.mm.bobcat.service.BobcatLoginService;

/**
 * Configure the embedded Jetty server and the SpringMVC dispatcher servlet.
 */
@Configuration
@ComponentScan(basePackages = { "uk.mm.bobcat.security" })
public class SecurityConfiguration {

	@Bean
	public SecurityHandler securityHandler() {
		String realm = "Bobcat server";

		Constraint constraint = new Constraint();
		constraint.setName(Constraint.__FORM_AUTH);
		constraint.setRoles(new String[] { "user", "admin" });
		constraint.setAuthenticate(true);

		ConstraintSecurityHandler csh = new ConstraintSecurityHandler();
		csh.setAuthenticator(new FormAuthenticator("/login", "/login", true));
		csh.setRealmName(realm);
		csh.addConstraintMapping(createMapping("/competition/*", constraint));
		BobcatLoginService loginService = loginService();
		csh.setLoginService(loginService);
		return csh;
	}

	private ConstraintMapping createMapping(String pathSpec, Constraint constraint) {
		ConstraintMapping constraintMapping = new ConstraintMapping();
		constraintMapping.setConstraint(constraint);
		constraintMapping.setPathSpec(pathSpec);
		return constraintMapping;
	}

	@Bean
	public BobcatLoginService loginService() {
		return new BobcatLoginService();
	}

	@Bean
	public SslContextFactory sslContextFactory() {
		SslContextFactory sslContextFactory = new SslContextFactory();
		String keyStorePath = "keystore/keystore.jks";
		String keyStorePassword = "caterpillar";
		sslContextFactory.setKeyStorePath(keyStorePath);
		sslContextFactory.setKeyStorePassword(keyStorePassword);
		sslContextFactory.setTrustStorePath(keyStorePath);
		sslContextFactory.setTrustStorePassword(keyStorePassword);
		sslContextFactory.setExcludeCipherSuites(
				"SSL_RSA_WITH_DES_CBC_SHA",
				"SSL_DHE_RSA_WITH_DES_CBC_SHA",
				"SSL_DHE_DSS_WITH_DES_CBC_SHA",
				"SSL_RSA_EXPORT_WITH_RC4_40_MD5",
				"SSL_RSA_EXPORT_WITH_DES40_CBC_SHA",
				"SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA",
				"SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA");
		return sslContextFactory;
	}
}
