package br.com.futechat.commons.batch;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.resource.StepExecutionSimpleCompletionPolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.futechat.commons.model.League;

@Configuration
public class LeaguesDatabaseUpdateJob {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean(name = "singleLeagueStep")
	public Step singleLeagueStep(ItemReader<List<League>> reader, ItemProcessor<List<League>, List<League>> processor,
			ItemWriter<List<League>> writer) {
		return stepBuilderFactory.get("singleLeagueStep").<List<League>, List<League>>chunk(new StepExecutionSimpleCompletionPolicy()).reader(reader)
				.processor(processor).writer(writer).build();
	}
	
	@Bean(name = "leagueDatabaseUpdateJob")
	public Job leagueDatabaseUpdateJob(@Qualifier("singleLeagueStep") Step step) {
		return jobBuilderFactory.get("leagueDatabaseUpdateJob").start(step).build();
	}
	
}
