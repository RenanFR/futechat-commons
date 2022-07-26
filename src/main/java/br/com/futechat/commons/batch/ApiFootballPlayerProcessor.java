package br.com.futechat.commons.batch;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.futechat.commons.entity.PlayerEntity;
import br.com.futechat.commons.mapper.FutechatMapper;
import br.com.futechat.commons.model.League;
import br.com.futechat.commons.model.Player;
import br.com.futechat.commons.model.Team;
import br.com.futechat.commons.repository.PlayerRepository;

public class ApiFootballPlayerProcessor implements ItemProcessor<List<Player>, List<Player>> {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ApiFootballPlayerProcessor.class);
	
	@Autowired
	private PlayerRepository playerRepository;
	
	@Autowired
	private FutechatMapper mapper;
	
	@Override
	public List<Player> process(List<Player> playersCurrentChunk) throws Exception {
		if (!playersCurrentChunk.isEmpty()) {
			
			Integer currentLeagueId = playersCurrentChunk.stream().map(Player::getTeam).map(Team::getLeague)
					.map(League::getApiFootballId).findFirst().get();
			List<PlayerEntity> playersFromThatLeagueInTheDatabase = playerRepository
					.findByTeamLeagueApiFootballId(currentLeagueId);
			List<Player> playersAlreadySaved = mapper.fromPlayerEntityToPlayerList(playersFromThatLeagueInTheDatabase)
					.stream().filter(playersCurrentChunk::contains).collect(Collectors.toList());
			LOGGER.info("{} players from league {} already exists in our database", playersAlreadySaved.size(),
					currentLeagueId);
			playersCurrentChunk.removeAll(playersAlreadySaved);
			return playersCurrentChunk;
		}
		return Collections.emptyList();
	}



}
