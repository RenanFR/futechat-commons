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
public class TeamsDBSyncJob {

	public static final String TEAMS_DB_SYNC_JOB = "teamsDBSyncJob";

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private ApiFootballTeamReader reader;

	@Autowired
	private ApiFootballTeamProcessor processor;

	@Autowired
	private ApiFootballTeamWriter writer;

	private Step fetchTeamsFromApi() {
		return stepBuilderFactory.get("fetchTeamsFromApi").tasklet(reader).build();
	}

	private Step filterExistingDatabaseTeams() {
		return stepBuilderFactory.get("filterExistingDatabaseTeams").tasklet(processor).build();
	}

	private Step saveRemainingTeamsToDatabase() {
		return stepBuilderFactory.get("saveRemainingTeamsToDatabase").tasklet(writer).build();
	}

	@Bean(name = TEAMS_DB_SYNC_JOB)
	public Job teamsDBSyncJob() {
		return jobBuilderFactory.get(TEAMS_DB_SYNC_JOB).start(fetchTeamsFromApi())
				.next(filterExistingDatabaseTeams()).next(saveRemainingTeamsToDatabase()).build();
	}

}
