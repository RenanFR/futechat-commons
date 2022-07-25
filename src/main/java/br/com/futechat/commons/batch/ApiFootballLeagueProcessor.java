package br.com.futechat.commons.batch;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.futechat.commons.entity.LeagueEntity;
import br.com.futechat.commons.mapper.FutechatMapper;
import br.com.futechat.commons.model.League;
import br.com.futechat.commons.repository.LeagueRepository;

public class ApiFootballLeagueProcessor implements ItemProcessor<List<League>, List<League>> {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ApiFootballLeagueProcessor.class);
	
	@Autowired
	private LeagueRepository leagueRepository;
	
	@Autowired
	private FutechatMapper mapper;

	@Override
	public List<League> process(List<League> leagues) throws Exception {
		List<LeagueEntity> existingLeaguesInOurDatabase = leagueRepository.findAll();
		List<League> leaguesAlreadySaved = mapper.fromLeagueEntityToLeagueList(existingLeaguesInOurDatabase).stream()
				.filter(leagues::contains).collect(Collectors.toList());
		LOGGER.info("{} leagues already exists in our database", leaguesAlreadySaved.size());
		leagues.removeAll(leaguesAlreadySaved);
		LOGGER.info("{} new leagues remaining to persist", leagues.size());
		return leagues;
	}

}
