package uk.mm.bobcat.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring3.SpringTemplateEngine;
import org.thymeleaf.spring3.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import uk.mm.bobcat.interceptors.IPLogInterceptor;

/**
 * The SpringMVC application context.
 *
 * This is the annotation variation of configuring the SpringMVC application
 * context. An XML configuration is imported so XML based configuration can
 * still be used.
 *
 * Any @Controller classes will be picked up by component scanning. All other
 * components are ignored as they will be picked up by the root application
 * context.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "uk.mm.bobcat.controller", "uk.mm.bobcat.service", "uk.mm.bobcat.domain" })
@PropertySource("classpath:bobcat.properties")
public class MvcConfiguration extends WebMvcConfigurerAdapter {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${cache.enabled:true}")
	private String cacheEnabled;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		if (!isCacheEnabled()) {
			registry.addResourceHandler("/styles/**").addResourceLocations("/styles/").setCachePeriod(0);
			registry.addResourceHandler("/images/**").addResourceLocations("/images/").setCachePeriod(0);
			registry.addResourceHandler("/scripts/**").addResourceLocations("/scripts/").setCachePeriod(0);
			registry.addResourceHandler("/static/**").addResourceLocations("/static/").setCachePeriod(0);
			registry.addResourceHandler("**favicon.ico").addResourceLocations("/images/favicon.ico").setCachePeriod(0);
		}
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		if (isCacheEnabled()) {
			configurer.enable();
		}
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new IPLogInterceptor());
	}

	@Bean
	public ServletContextTemplateResolver thymeleafTemplateResolver() {
		logCacheStatus();
		ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode("HTML5");
		if (isCacheEnabled()) {
			resolver.setCacheable(true);
		} else {
			resolver.setCacheable(false);
			resolver.setCacheTTLMs(1L);
		}
		return resolver;
	}

	@Bean
	public SpringTemplateEngine thymeleafTemplateEngine() {
		logger.warn("engine bean");
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(thymeleafTemplateResolver());
		return engine;
	}

	@Bean
	public ThymeleafViewResolver thymeleafViewResolver() {
		logger.warn("view-resolver bean");
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(thymeleafTemplateEngine());
		return resolver;
	}

	private void logCacheStatus() {
		if (isCacheEnabled()) {
			logger.info("Production mode: page caching enabled!");
		} else {
			logger.warn("+--------------------------------------------------------------------------------+");
			logger.warn("| DEVELOPMENT MODE: page caching disabled, also never sending '304 Not Modified' |");
			logger.warn("+--------------------------------------------------------------------------------+");
		}
	}

	private boolean isCacheEnabled() {
		return "true".equals(cacheEnabled);
	}
}