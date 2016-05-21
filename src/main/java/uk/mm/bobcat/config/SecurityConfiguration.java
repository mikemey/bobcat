package uk.mm.bobcat.config;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Credential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configure the embedded Jetty server and the SpringMVC dispatcher servlet.
 */
@Configuration
@ComponentScan(basePackages = {"uk.mm.bobcat.security"})
public class SecurityConfiguration {

    @Value("${server.user:}")
    private String user;

    @Value("${server.password:}")
    private String password;

    @Bean
    public SecurityHandler securityHandler() {
        String realm = "Bobcat server";
        HashLoginService loginService = new HashLoginService();
        loginService.putUser(user, Credential.getCredential(password), new String[]{"parent"});
        loginService.setName(realm);

        Constraint constraint = new Constraint();
        constraint.setName(Constraint.__BASIC_AUTH);
        constraint.setRoles(new String[]{"parent"});
        constraint.setAuthenticate(true);

        ConstraintSecurityHandler csh = new ConstraintSecurityHandler();
        csh.setAuthenticator(new BasicAuthenticator());
        csh.setRealmName(realm);
        csh.addConstraintMapping(createMapping("/competition/*", constraint));
        csh.addConstraintMapping(createMapping("/ranking/add_name", constraint));
        csh.addConstraintMapping(createMapping("/ranking/remove_name", constraint));
        csh.addConstraintMapping(createMapping("/ranking/modify_name", constraint));
        csh.setLoginService(loginService);
        return csh;
    }

    private ConstraintMapping createMapping(String pathSpec, Constraint constraint) {
        ConstraintMapping constraintMapping = new ConstraintMapping();
        constraintMapping.setConstraint(constraint);
        constraintMapping.setPathSpec(pathSpec);
        return constraintMapping;
    }
}
