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

import br.com.futechat.commons.entity.TeamEntity;
import br.com.futechat.commons.mapper.FutechatMapper;
import br.com.futechat.commons.model.Team;
import br.com.futechat.commons.repository.LeagueRepository;
import br.com.futechat.commons.repository.TeamRepository;

public class ApiFootballTeamWriter implements Tasklet, StepExecutionListener {

	public static final Logger LOGGER = LoggerFactory.getLogger(ApiFootballTeamWriter.class);

	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private LeagueRepository leagueRepository;

	@Autowired
	private FutechatMapper mapper;

	private List<Team> teamsFiltered;

	private int databaseTeamsCount;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
		this.teamsFiltered = (List<Team>) executionContext.get("teamsFiltered");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		LOGGER.info("{} teams saved in our database", databaseTeamsCount);
		return ExitStatus.COMPLETED;
	}

	@Override
	@Transactional
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		List<TeamEntity> teamsToSave = mapper.fromTeamToTeamEntityList(teamsFiltered);
		teamsToSave.stream().forEach(team -> {
			leagueRepository.findByApiFootballId(team.getLeague().getApiFootballId())
					.ifPresent(league -> team.setLeague(league));
		});
		List<TeamEntity> savedTeams = teamRepository.saveAllAndFlush(teamsToSave);
		databaseTeamsCount = savedTeams.size();
		return RepeatStatus.FINISHED;
	}

}
