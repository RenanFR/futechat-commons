package br.com.futechat.commons.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.futechat.commons.model.Match;
import br.com.futechat.commons.model.Player;
import br.com.futechat.commons.model.PlayerTransferHistory;

public abstract class FutechatService {
	
	private PersistenceAdapter persistenceAdapter;
	
	public String getPlayerHeight(String playerName, String teamName, Optional<String> countryName) {
		Optional<Player> optionalPlayer = persistenceAdapter.getPlayerFromTeam(playerName, teamName)
				.stream().findFirst();

		Player player = optionalPlayer.orElse(getPlayer(playerName, teamName, countryName));
		if (!optionalPlayer.isPresent()) {
			persistenceAdapter.savePlayerAndTeam(player);
		}
		return player.height();
	}

	public PlayerTransferHistory getPlayerTransferHistory(String playerName, String teamName) {
		
		Optional<PlayerTransferHistory> optionalPlayerTransferHistory = persistenceAdapter.getPlayerWithTransferHistory(playerName, teamName);
		
		if (!optionalPlayerTransferHistory.isPresent()) {
			return fetchAndSavePlayerWithTransfers(playerName, teamName);
		} else {
			if (!optionalPlayerTransferHistory.get().transfers().isEmpty()) {
				return optionalPlayerTransferHistory.get();
			} else {
				return fetchAndSavePlayerWithTransfers(playerName, teamName);
			}
		}
		
	}

	private PlayerTransferHistory fetchAndSavePlayerWithTransfers(String playerName, String teamName) {
		PlayerTransferHistory playerTransferHistory = getPlayerTransfers(playerName, teamName);
		persistenceAdapter.savePlayerTransfers(playerTransferHistory);
		return playerTransferHistory;
	}
	
	public abstract Player getPlayer(String playerName, String teamName, Optional<String> countryName);

	public abstract PlayerTransferHistory getPlayerTransfers(String playerName, String teamName);

	public abstract List<Pair<String, Integer>> getLeagueTopScorersForTheSeason(Integer seasonYear, String leagueName);

	public abstract List<Match> getSoccerMatches(Optional<String> leagueName, Optional<String> countryName,
			Optional<LocalDate> schedule);

	public abstract Match getFixtureStatistics(String homeTeam, String awayTeam, LocalDate matchDate);

	@Autowired
	public void setPersistenceAdapter(PersistenceAdapter persistenceAdapter) {
		this.persistenceAdapter = persistenceAdapter;
	}

}
