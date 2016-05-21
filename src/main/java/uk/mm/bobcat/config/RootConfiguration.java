package uk.mm.bobcat.config;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import com.mongodb.Mongo;

/**
 * The root application context.
 * <p/>
 * Beans can also be configured by XML in root-context.xml which is imported by
 * this context class.
 * <p/>
 * Component scanning is also done to pickup any components other than
 *
 * @Controllers. @Controllers will be picked up by the SpringMVC context.
 */
@Configuration
@Import({ JettyConfiguration.class, SecurityConfiguration.class })
@PropertySource("bobcat.properties")
@EnableMongoRepositories(basePackages = "uk.mm.bobcat.service.repos")
public class RootConfiguration {
	@Value("${server.host:localhost}")
	private String serverHost;
	@Value("${db.host:localhost}")
	private String databaseHost;
	@Value("${db.port:27017}")
	private int databasePort;
	@Value("${db.name:}")
	private String databaseName;
	@Value("${db.user:}")
	private String databaseUser;
	@Value("${db.password:}")
	private String databasePassword;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public MappingJacksonHttpMessageConverter mappingJacksonHttpMessageConverter() {
		return new MappingJacksonHttpMessageConverter();
	}

	@Bean
	public MongoTemplate mongoTemplate() throws UnknownHostException {
		UserCredentials credentials = new UserCredentials(databaseUser, databasePassword);
		Mongo mongo = new Mongo(databaseHost, databasePort);
		return new MongoTemplate(mongo, databaseName, credentials);
	}
}