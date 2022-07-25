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

import br.com.futechat.commons.entity.LeagueEntity;
import br.com.futechat.commons.mapper.FutechatMapper;
import br.com.futechat.commons.model.League;
import br.com.futechat.commons.repository.LeagueRepository;

public class ApiFootballLeagueProcessor implements Tasklet, StepExecutionListener {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ApiFootballLeagueProcessor.class);
	
	@Autowired
	private LeagueRepository leagueRepository;
	
	@Autowired
	private FutechatMapper mapper;
	
	private List<League> leaguesRead;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
		this.leaguesRead = (List<League>) executionContext.get("leaguesRead");		
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		LOGGER.info("{} new leagues remaining to persist", leaguesRead.size());
		stepExecution.getJobExecution().getExecutionContext().put("leaguesFiltered", this.leaguesRead);
		return ExitStatus.COMPLETED;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		List<LeagueEntity> existingLeaguesInOurDatabase = leagueRepository.findAll();
		List<League> leaguesAlreadySaved = mapper.fromLeagueEntityToLeagueList(existingLeaguesInOurDatabase).stream()
				.filter(leaguesRead::contains).collect(Collectors.toList());
		LOGGER.info("{} leagues already exists in our database", leaguesAlreadySaved.size());
		leaguesRead.removeAll(leaguesAlreadySaved);
		return RepeatStatus.FINISHED;
	}



}
