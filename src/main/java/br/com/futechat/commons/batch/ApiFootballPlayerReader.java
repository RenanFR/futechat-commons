package br.com.futechat.commons.batch;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.futechat.commons.model.League;
import br.com.futechat.commons.model.Player;
import br.com.futechat.commons.service.ApiFootballService;

public class ApiFootballPlayerReader implements ItemReader<List<Player>> {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ApiFootballTeamReader.class);
	
	private ApiFootballService apiFootballService;
	
	private Queue<League> observedLeagues;
	
	@Autowired
	public ApiFootballPlayerReader(ApiFootballService apiFootballService) {
		this.apiFootballService = apiFootballService;
		this.observedLeagues = new LinkedList<>(apiFootballService.getLeaguesOfInterest());
	}

	@Override
	public List<Player> read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		Optional<League> currentLeague = Optional.ofNullable(this.observedLeagues.poll());
		if (currentLeague.isPresent()) {
			
			List<Player> playersFromLeague = apiFootballService.getPlayersFromLeague(currentLeague.get().getApiFootballId());
			LOGGER.info("{} players were found from the league {}", playersFromLeague.size(), currentLeague.get().getApiFootballId());
			return playersFromLeague;
		}
		return null;
	}



}
