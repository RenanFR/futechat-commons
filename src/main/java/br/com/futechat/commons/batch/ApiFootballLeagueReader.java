package br.com.futechat.commons.batch;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.futechat.commons.model.League;
import br.com.futechat.commons.service.ApiFootballService;

public class ApiFootballLeagueReader implements ItemReader<List<League>> {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ApiFootballLeagueReader.class);
	
	@Autowired
	private ApiFootballService apiFootballService;

	@Override
	public List<League> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		List<League> leagues = apiFootballService.getLeagues();
		LOGGER.info("{} leagues were found within API-FOOTBALL", leagues.size());
		return leagues;
	}

}
