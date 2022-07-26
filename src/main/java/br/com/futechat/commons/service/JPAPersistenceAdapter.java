package br.com.futechat.commons.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.futechat.commons.entity.TeamEntity;
import br.com.futechat.commons.exception.LeagueNotFoundException;
import br.com.futechat.commons.exception.TeamNotFoundException;
import br.com.futechat.commons.mapper.FutechatMapper;
import br.com.futechat.commons.model.League;
import br.com.futechat.commons.model.Team;
import br.com.futechat.commons.repository.LeagueRepository;
import br.com.futechat.commons.repository.TeamRepository;

@Service
public class JPAPersistenceAdapter extends PersistenceAdapter {
	
	private TeamRepository teamRepository;
	private LeagueRepository leagueRepository;
	private FutechatMapper mapper;

	public JPAPersistenceAdapter(TeamRepository teamRepository, LeagueRepository leagueRepository,
			FutechatMapper mapper) {
		this.teamRepository = teamRepository;
		this.leagueRepository = leagueRepository;
		this.mapper = mapper;
	}

	@Override
	public Team getTeamByNameAndCountry(String teamName, String countryName) {
		Optional<TeamEntity> possibleTeam = teamRepository.findByNameAndCountry(teamName, countryName);
		return mapper.fromTeamEntityToTeam(possibleTeam.orElseThrow(() -> new TeamNotFoundException(teamName)));
	}

	@Override
	public Team getTeamByName(String teamName) {
		Optional<TeamEntity> possibleTeam = teamRepository.findByName(teamName);
		return mapper.fromTeamEntityToTeam(possibleTeam.orElseThrow(() -> new TeamNotFoundException(teamName)));
	}

	@Override
	public League getLeagueByName(String leagueName) {
		return mapper.fromLeagueEntityToLeague(
				leagueRepository.findByName(leagueName).orElseThrow(() -> new LeagueNotFoundException(leagueName)));
	}

	@Override
	public League getLeagueByNameAndCountry(String leagueName, String countryName) {
		return mapper.fromLeagueEntityToLeague(leagueRepository.findByNameAndCountry(leagueName, countryName)
				.orElseThrow(() -> new LeagueNotFoundException(leagueName)));
	}

}
