package br.com.futechat.commons.batch;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.futechat.commons.entity.PlayerEntity;
import br.com.futechat.commons.mapper.FutechatMapper;
import br.com.futechat.commons.model.Player;
import br.com.futechat.commons.repository.PlayerRepository;
import br.com.futechat.commons.repository.TeamRepository;

public class ApiFootballPlayerWriter implements ItemWriter<List<Player>> {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ApiFootballLeagueWriter.class);
	
	@Autowired
	private PlayerRepository playerRepository;
	
	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private FutechatMapper mapper;
	
	@Override
	public void write(List<? extends List<Player>> playersListChunk) throws Exception {
		List<PlayerEntity> playersEntities = mapper.fromPlayerToPlayerEntityList(
				playersListChunk.stream().flatMap(List::stream).collect(Collectors.toList()));
		playersEntities.forEach(player -> {
			teamRepository.findByNameAndApiFootballId(player.getTeam().getName(), player.getTeam().getApiFootballId())
					.stream().findFirst().ifPresent(team -> {
						player.setTeam(team);
					});
		});
		Integer playersCount = playerRepository.saveAllAndFlush(playersEntities).size();
		LOGGER.info("{} players saved in our database", playersCount);
	}



}
