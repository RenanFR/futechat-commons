package br.com.futechat.commons.service;

import java.util.List;
import java.util.Optional;

import br.com.futechat.commons.entity.PlayerEntity;
import br.com.futechat.commons.model.Player;
import br.com.futechat.commons.model.PlayerTransferHistory;

public abstract class PersistenceAdapter {
	
	public abstract PlayerEntity savePlayerAndTeam(Player player);
	
	public abstract List<Player> getPlayerFromTeam(String playerName, String teamName);
	
	public abstract Optional<PlayerTransferHistory> getPlayerWithTransferHistory(String playerName, String teamName);
	
	public abstract void savePlayerTransfers(PlayerTransferHistory playerTransferHistory);

}
