package uk.mm.bobcat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "uk.mm.bobcat.controller", "uk.mm.bobcat.service", "uk.mm.bobcat.domain" })
public class BobcatTestConfig {
	@Bean
	public String webappResourcePath() {
		return "src/main/resources/webapp";
	}
}