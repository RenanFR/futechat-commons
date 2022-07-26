package br.com.futechat.commons.batch;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.futechat.commons.model.Player;

@Configuration
@ConditionalOnProperty(prefix = "apiFootball", name = "playersSynchronizationEnabled", havingValue = "true")
public class PlayersDBSyncJob {

	public static final String PLAYERS_DB_SYNC_JOB = "playersDbSyncJob";

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	
	@Bean(name = "leaguePlayersUpdateStep")
	public Step leaguePlayersUpdateStep(ItemReader<List<Player>> reader,
			ItemProcessor<List<Player>, List<Player>> processor, ItemWriter<List<Player>> writer) {
		return stepBuilderFactory.get("leaguePlayersUpdateStep").<List<Player>, List<Player>>chunk(1).reader(reader)
				.processor(processor).writer(writer).build();
	}

	@Bean(name = PLAYERS_DB_SYNC_JOB)
	public Job leaguesDBSyncJob(@Qualifier("leaguePlayersUpdateStep") Step leaguePlayersUpdateStep) {
		return jobBuilderFactory.get(PLAYERS_DB_SYNC_JOB).flow(leaguePlayersUpdateStep).end().build();
	}

}
