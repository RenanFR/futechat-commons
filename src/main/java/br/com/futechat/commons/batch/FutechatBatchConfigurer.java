package br.com.futechat.commons.batch;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

public class FutechatBatchConfigurer implements BatchConfigurer {

	private final EntityManagerFactory entityManagerFactory;

	private JobRepository jobRepository;

	private JobLauncher jobLauncher;

	private PlatformTransactionManager platformTransactionManager;

	private JobExplorer jobExplorer;

	public FutechatBatchConfigurer(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	@Override
	public JobRepository getJobRepository() throws Exception {
		return jobRepository;
	}

	@Override
	public PlatformTransactionManager getTransactionManager() throws Exception {
		return platformTransactionManager;
	}

	@Override
	public JobLauncher getJobLauncher() throws Exception {
		return jobLauncher;
	}

	@Override
	public JobExplorer getJobExplorer() throws Exception {
		return jobExplorer;
	}

	@PostConstruct
	public void init() {
		try {
			this.platformTransactionManager = new JpaTransactionManager(this.entityManagerFactory);

			MapJobRepositoryFactoryBean mapJobRepositoryFactoryBean = new MapJobRepositoryFactoryBean(
					this.platformTransactionManager);
			mapJobRepositoryFactoryBean.afterPropertiesSet();
			this.jobRepository = mapJobRepositoryFactoryBean.getObject();

			SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
			simpleJobLauncher.setJobRepository(getJobRepository());
			simpleJobLauncher.afterPropertiesSet();
			this.jobLauncher = simpleJobLauncher;

			MapJobExplorerFactoryBean mapJobExplorerFactoryBean = new MapJobExplorerFactoryBean(mapJobRepositoryFactoryBean);
			mapJobExplorerFactoryBean.afterPropertiesSet();
			this.jobExplorer = mapJobExplorerFactoryBean.getObject();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

}
