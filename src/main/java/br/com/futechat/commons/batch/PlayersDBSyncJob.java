package br.com.futechat.commons.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlayersDBSyncJob {

	public static final String PLAYERS_DB_SYNC_JOB = "playersDbSyncJob";

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private ApiFootballPlayerReader reader;

	@Autowired
	private ApiFootballPlayerProcessor processor;

	@Autowired
	private ApiFootballPlayerWriter writer;
	
	private Step fetchPlayersFromApi() {
		return stepBuilderFactory.get("fetchPlayersFromApi").tasklet(reader).build();
	}

	private Step filterExistingDatabasePlayers() {
		return stepBuilderFactory.get("filterExistingDatabasePlayers").tasklet(processor).build();
	}

	private Step saveRemainingPlayersToDatabase() {
		return stepBuilderFactory.get("saveRemainingPlayersToDatabase").tasklet(writer).build();
	}

	@Bean(name = PLAYERS_DB_SYNC_JOB)
	public Job leaguesDBSyncJob() {
		return jobBuilderFactory.get(PLAYERS_DB_SYNC_JOB).start(fetchPlayersFromApi())
				.next(filterExistingDatabasePlayers()).next(saveRemainingPlayersToDatabase()).build();
	}

}
