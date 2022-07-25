package br.com.futechat.commons.batch;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfig {

	@Value("classpath:/org/springframework/batch/core/schema-drop-postgresql.sql")
	private Resource schemaDropPostgresqlSql;

	@Value("classpath:/org/springframework/batch/core/schema-postgresql.sql")
	private Resource schemaPostgresqlSql;
	
	@Autowired
	@Qualifier("rdsDataSource")
	private DataSource dataSource;

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Bean
	public DataSourceInitializer dataSourceInitializer() {
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
		databasePopulator.addScript(schemaDropPostgresqlSql);
		databasePopulator.addScript(schemaPostgresqlSql);
		databasePopulator.setIgnoreFailedDrops(true);
		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
		dataSourceInitializer.setDataSource(dataSource);
		dataSourceInitializer.setDatabasePopulator(databasePopulator);
		return dataSourceInitializer;
	}

	@Bean
	public BatchConfigurer batchConfigurer() {
		return new FutechatBatchConfigurer(entityManagerFactory);
	}

}
