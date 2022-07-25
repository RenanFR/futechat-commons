package br.com.futechat.commons.batch;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.futechat.commons.entity.LeagueEntity;
import br.com.futechat.commons.mapper.FutechatMapper;
import br.com.futechat.commons.model.League;
import br.com.futechat.commons.repository.LeagueRepository;

public class ApiFootballLeagueWriter implements ItemWriter<List<League>> {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ApiFootballLeagueWriter.class);
	
	@Autowired
	private LeagueRepository leagueRepository;
	
	@Autowired
	private FutechatMapper mapper;

	@Override
	public void write(List<? extends List<League>> leagueList) throws Exception {
		List<LeagueEntity> leaguesToSave = mapper.fromLeagueToLeagueEntityList(leagueList.get(0));
		List<LeagueEntity> savedLeagues = leagueRepository.saveAll(leaguesToSave);
		LOGGER.info("{} leagues saved in our database", savedLeagues.size());
	}

}
