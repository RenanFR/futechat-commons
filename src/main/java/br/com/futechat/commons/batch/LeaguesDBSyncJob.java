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
public class LeaguesDBSyncJob {

	public static final String LEAGUES_DB_SYNC_JOB = "leaguesDbSyncJob";

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
	
	private Step fetchLeaguesFromApi() {
		return stepBuilderFactory.get("fetchLeaguesFromApi").tasklet(reader).build();
	}

	private Step filterExistingDatabaseLeagues() {
		return stepBuilderFactory.get("filterExistingDatabaseLeagues").tasklet(processor).build();
	}

	private Step saveRemainingLeaguesToDatabase() {
		return stepBuilderFactory.get("saveRemainingLeaguesToDatabase").tasklet(writer).build();
	}

	@Bean(name = LEAGUES_DB_SYNC_JOB)
	public Job leaguesDBSyncJob() {
		return jobBuilderFactory.get(LEAGUES_DB_SYNC_JOB).start(fetchLeaguesFromApi())
				.next(filterExistingDatabaseLeagues()).next(saveRemainingLeaguesToDatabase()).build();
	}

}
