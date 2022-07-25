package br.com.futechat.commons;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = {
"br.com.futechat.commons.repository" }, entityManagerFactoryRef = "entityManagerFactoryBean")
@EnableTransactionManagement
@Profile("test")
public class H2Config {

	@Value("${spring.datasource.url}")
	private String jdbcUrl;

	@Bean(name = "h2DataSource")
	@ConfigurationProperties(prefix = "spring.datasource.h2")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource());
		entityManagerFactoryBean.setPersistenceUnitName("futechatTest");
		entityManagerFactoryBean.setPackagesToScan("br.com.futechat.commons.entity");
		entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		entityManagerFactoryBean.setJpaPropertyMap(Map.of("hibernate.dialect", "org.hibernate.dialect.H2Dialect",
				"hibernate.hbm2ddl.auto", "create-drop"));

		return entityManagerFactoryBean;
	}

}
