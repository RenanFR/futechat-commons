package br.com.futechat.commons.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.futechat.commons.entity.PlayerEntity;
import br.com.futechat.commons.mapper.FutechatMapper;
import br.com.futechat.commons.model.Player;
import br.com.futechat.commons.repository.PlayerRepository;
import br.com.futechat.commons.repository.TeamRepository;

@Service
public class JPAPersistenceAdapter extends PersistenceAdapter {
	
	private PlayerRepository playerRepository;
	private TeamRepository teamRepository;
	private FutechatMapper mapper;
	
	public JPAPersistenceAdapter(PlayerRepository playerRepository, TeamRepository teamRepository, FutechatMapper mapper) {
		this.playerRepository = playerRepository;
		this.teamRepository = teamRepository;
		this.mapper = mapper;
	}

	@Override
	public void savePlayerAndTeam(Player player) {
		PlayerEntity playerEntity = mapper.fromPlayerToPlayerEntity(player);
		playerEntity.setTeam(teamRepository.save(playerEntity.getTeam()));
		playerRepository.save(playerEntity);
	}

	@Override
	public List<Player> getPlayerFromTeam(String playerName, String teamName, Optional<String> countryName) {
		return mapper.fromPlayerEntityToPlayerList(playerRepository.findByNameAndTeamName(playerName, teamName));
		
	}

}
