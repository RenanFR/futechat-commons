package br.com.futechat.commons.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class LeaguesDatabaseUpdateJob {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private ApiFootballLeagueReader reader;

	@Autowired
	private ApiFootballLeagueProcessor processor;

	@Autowired
	private ApiFootballLeagueWriter writer;
	
	@Autowired
	private PlatformTransactionManager transactionManager;

	@Bean
	protected Step fetchLeaguesFromApi() {
		return stepBuilderFactory.get("fetchLeaguesFromApi").tasklet(reader).build();
	}

	@Bean
	protected Step filterExistingDatabaseLeagues() {
		return stepBuilderFactory.get("filterExistingDatabaseLeagues").tasklet(processor).build();
	}

	@Bean
	protected Step saveRemainingLeaguesToDatabase() {
		return stepBuilderFactory.get("saveRemainingLeaguesToDatabase").tasklet(writer)
				.transactionManager(transactionManager).build();
	}

	@Bean(name = "leagueDatabaseUpdateJob")
	public Job leagueDatabaseUpdateJob() {
		return jobBuilderFactory.get("leagueDatabaseUpdateJob").start(fetchLeaguesFromApi())
				.next(filterExistingDatabaseLeagues()).next(saveRemainingLeaguesToDatabase()).build();
	}

}
