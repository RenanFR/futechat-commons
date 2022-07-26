package br.com.futechat.commons.batch;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.futechat.commons.entity.PlayerEntity;
import br.com.futechat.commons.mapper.FutechatMapper;
import br.com.futechat.commons.model.Player;
import br.com.futechat.commons.repository.PlayerRepository;

public class ApiFootballPlayerProcessor implements Tasklet, StepExecutionListener {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ApiFootballPlayerProcessor.class);
	
	@Autowired
	private PlayerRepository playerRepository;
	
	@Autowired
	private FutechatMapper mapper;
	
	private List<Player> playersFromReadStep;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
		this.playersFromReadStep = (List<Player>) executionContext.get("obtainedPlayers");		
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		LOGGER.info("{} players to be persisted", playersFromReadStep.size());
		stepExecution.getJobExecution().getExecutionContext().put("playersToPersist", this.playersFromReadStep);
		return ExitStatus.COMPLETED;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		List<PlayerEntity> databaseExistingPlayers = playerRepository.findAll();
		List<Player> playersAlreadySaved = mapper.fromPlayerEntityToPlayerList(databaseExistingPlayers).stream()
				.filter(playersFromReadStep::contains).collect(Collectors.toList());
		LOGGER.info("{} players already  exists in our database", playersAlreadySaved.size());
		playersFromReadStep.removeAll(playersAlreadySaved);
		return RepeatStatus.FINISHED;
	}

}
