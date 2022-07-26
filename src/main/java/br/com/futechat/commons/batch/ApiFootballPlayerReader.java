package br.com.futechat.commons.batch;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.futechat.commons.model.Player;
import br.com.futechat.commons.service.ApiFootballService;

public class ApiFootballPlayerReader implements Tasklet, StepExecutionListener {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ApiFootballTeamReader.class);
	
	@Autowired
	private ApiFootballService apiFootballService;
	
	private List<Player> obtainedPlayers;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		LOGGER.info("Players database update job initialized");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		stepExecution.getJobExecution().getExecutionContext().put("obtainedPlayers", this.obtainedPlayers);
		return ExitStatus.COMPLETED;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		obtainedPlayers = apiFootballService.getPlayersFromObservedLeagues();
		LOGGER.info("{} players were found within API-FOOTBALL", obtainedPlayers.size());
		return RepeatStatus.FINISHED;
	}

}
