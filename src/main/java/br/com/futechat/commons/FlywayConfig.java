package br.com.futechat.commons;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {
	
	@Autowired
	private DataSource dataSource;
	
	@Bean(initMethod = "migrate")
	public Flyway flyway() {
		ClassicConfiguration flywayConfiguration = new ClassicConfiguration();
		flywayConfiguration.setBaselineOnMigrate(true);
		flywayConfiguration.setDataSource(dataSource);
		Flyway flyway = new Flyway(flywayConfiguration);
		return flyway;
	}

}
