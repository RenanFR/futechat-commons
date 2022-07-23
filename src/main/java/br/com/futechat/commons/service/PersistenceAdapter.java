package br.com.futechat.commons.service;

import java.util.List;
import java.util.Optional;

import br.com.futechat.commons.model.Player;

public abstract class PersistenceAdapter {
	
	public abstract void savePlayerAndTeam(Player player);
	
	public abstract List<Player> getPlayerFromTeam(String playerName, String teamName, Optional<String> countryName);

}
