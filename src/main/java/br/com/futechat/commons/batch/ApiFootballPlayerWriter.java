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
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.futechat.commons.entity.PlayerEntity;
import br.com.futechat.commons.mapper.FutechatMapper;
import br.com.futechat.commons.model.Player;
import br.com.futechat.commons.repository.PlayerRepository;
import br.com.futechat.commons.repository.TeamRepository;

public class ApiFootballPlayerWriter implements Tasklet, StepExecutionListener {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ApiFootballLeagueWriter.class);
	
	@Autowired
	private PlayerRepository playerRepository;
	
	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private FutechatMapper mapper;
	
	private List<Player> playersToPersist;
	
	private Integer playersCount;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
		this.playersToPersist = (List<Player>) executionContext.get("playersToPersist");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		LOGGER.info("{} players saved in our database", playersCount);
		return ExitStatus.COMPLETED;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		List<PlayerEntity> playersEntities = mapper.fromPlayerToPlayerEntityList(this.playersToPersist);
		playersEntities.forEach(player -> {
			teamRepository.findByNameAndApiFootballId(player.getTeam().getName(), player.getTeam().getApiFootballId())
					.ifPresent(team -> {
						player.setTeam(team);
					});
		});
		playersCount = playerRepository.saveAllAndFlush(playersEntities).size();
		return RepeatStatus.FINISHED;
	}

}
