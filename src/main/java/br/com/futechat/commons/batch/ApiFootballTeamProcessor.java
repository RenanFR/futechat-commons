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

import br.com.futechat.commons.entity.TeamEntity;
import br.com.futechat.commons.mapper.FutechatMapper;
import br.com.futechat.commons.model.Team;
import br.com.futechat.commons.repository.TeamRepository;

public class ApiFootballTeamProcessor implements Tasklet, StepExecutionListener {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ApiFootballTeamProcessor.class);
	
	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private FutechatMapper mapper;
	
	private List<Team> teamsRead;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
		this.teamsRead = (List<Team>) executionContext.get("teamsRead");		
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		LOGGER.info("{} new teams remaining to persist", teamsRead.size());
		stepExecution.getJobExecution().getExecutionContext().put("teamsFiltered", this.teamsRead);
		return ExitStatus.COMPLETED;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		List<TeamEntity> databaseExistingTeams = teamRepository.findAll();
		List<Team> teamsAlreadySaved = mapper.fromTeamEntityToTeamList(databaseExistingTeams).stream()
				.filter(teamsRead::contains).collect(Collectors.toList());
		LOGGER.info("{} teams already exists in our database", teamsAlreadySaved.size());
		teamsRead.removeAll(teamsAlreadySaved);
		return RepeatStatus.FINISHED;
	}



}
