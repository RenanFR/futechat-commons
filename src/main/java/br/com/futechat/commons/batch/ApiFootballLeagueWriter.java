package br.com.futechat.commons.batch;

import java.util.List;

import javax.transaction.Transactional;

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

import br.com.futechat.commons.entity.LeagueEntity;
import br.com.futechat.commons.mapper.FutechatMapper;
import br.com.futechat.commons.model.League;
import br.com.futechat.commons.repository.LeagueRepository;

public class ApiFootballLeagueWriter implements Tasklet, StepExecutionListener {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ApiFootballLeagueWriter.class);
	
	@Autowired
	private LeagueRepository leagueRepository;
	
	@Autowired
	private FutechatMapper mapper;
	
	private List<League> leaguesFiltered;
	
	private int leagueRecordCount;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
		this.leaguesFiltered = (List<League>) executionContext.get("leaguesFiltered");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		LOGGER.info("{} leagues saved in our database", leagueRecordCount);
		return ExitStatus.COMPLETED;
	}

	@Override
	@Transactional
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		List<LeagueEntity> leaguesToSave = mapper.fromLeagueToLeagueEntityList(leaguesFiltered);
		List<LeagueEntity> savedLeagues = leagueRepository.saveAllAndFlush(leaguesToSave);
		leagueRecordCount = savedLeagues.size();
		return RepeatStatus.FINISHED;
	}



}
