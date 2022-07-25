package br.com.futechat.commons.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.futechat.commons.entity.PlayerEntity;
import br.com.futechat.commons.entity.TeamEntity;
import br.com.futechat.commons.entity.TransferEntity;
import br.com.futechat.commons.mapper.FutechatMapper;
import br.com.futechat.commons.model.Player;
import br.com.futechat.commons.model.PlayerTransferHistory;
import br.com.futechat.commons.repository.PlayerRepository;
import br.com.futechat.commons.repository.TeamRepository;
import br.com.futechat.commons.repository.TransferRepository;

@Service
public class JPAPersistenceAdapter extends PersistenceAdapter {
	
	private PlayerRepository playerRepository;
	private TeamRepository teamRepository;
	private TransferRepository transferRepository;
	private FutechatMapper mapper;
	
	public JPAPersistenceAdapter(PlayerRepository playerRepository, TeamRepository teamRepository,
			TransferRepository transferRepository, FutechatMapper mapper) {
		this.playerRepository = playerRepository;
		this.teamRepository = teamRepository;
		this.transferRepository = transferRepository;
		this.mapper = mapper;
	}

	@Override
	public PlayerEntity savePlayerAndTeam(Player player) {
		PlayerEntity playerEntity = mapper.fromPlayerToPlayerEntity(player);
		playerEntity.setTeam(teamRepository.findByNameAndCountry(player.team().getName(), player.team().getCountry())
				.orElse(teamRepository.saveAndFlush(playerEntity.getTeam())));
		return playerRepository.save(playerEntity);
	}

	@Override
	public List<Player> getPlayerFromTeam(String playerName, String teamName) {
		return mapper.fromPlayerEntityToPlayerList(playerRepository.findByNameAndTeamName(playerName, teamName));
		
	}

	@Override
	public void savePlayerTransfers(PlayerTransferHistory playerTransferHistory) {
		List<PlayerEntity> existingPlayer = playerRepository.findByNameAndTeamName(
				playerTransferHistory.player().name(), playerTransferHistory.player().team().getName());
		existingPlayer.stream().findFirst().ifPresentOrElse(player -> {
			convertAndSaveTransfers(playerTransferHistory, player);
		}, () -> {
			convertAndSaveTransfers(playerTransferHistory, savePlayerAndTeam(playerTransferHistory.player()));
		});
	}

	private void convertAndSaveTransfers(PlayerTransferHistory playerTransferHistory, PlayerEntity playerEntity) {
		List<TransferEntity> transfersToPersist = mapper
				.fromTransferListToTransferEntity(playerTransferHistory.transfers());
		transfersToPersist.stream().forEach(transfer -> {
			transfer.setPlayer(playerEntity);
			transfer.setTeamIn(saveTeamIfNotExists(transfer.getTeamIn()));
			transfer.setTeamOut(saveTeamIfNotExists(transfer.getTeamOut()));
		});
		transferRepository.saveAll(transfersToPersist);
	}

	private TeamEntity saveTeamIfNotExists(TeamEntity team) {
		List<TeamEntity> existingTeamWithThisName = teamRepository.findByName(team.getName());
		if (existingTeamWithThisName.isEmpty()) {
			return teamRepository.save(team);
		}
		return existingTeamWithThisName.stream().findFirst().get();
	}

	@Override
	public Optional<PlayerTransferHistory> getPlayerWithTransferHistory(String playerName, String teamName) {
		List<PlayerEntity> playersFound = playerRepository.findByNameAndTeamName(playerName, teamName);
		Optional<PlayerEntity> possiblePlayer = playersFound.stream().findFirst();
		if (possiblePlayer.isPresent()) {
			return Optional.ofNullable(mapper.fromPlayerEntityToPlayerTransferHistory(possiblePlayer.get()));
			
		}
		return Optional.empty();
	}

}
