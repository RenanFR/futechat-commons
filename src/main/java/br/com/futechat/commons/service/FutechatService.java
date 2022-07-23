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
		Optional<Player> optionalPlayer = persistenceAdapter.getPlayerFromTeam(playerName, teamName, countryName)
				.stream().findFirst();

		Player player = optionalPlayer.orElse(getPlayer(playerName, teamName, countryName));
		if (!optionalPlayer.isPresent()) {
			persistenceAdapter.savePlayerAndTeam(player);
		}
		return player.height();
	}

	public abstract Player getPlayer(String playerName, String teamName, Optional<String> countryName);

	public abstract PlayerTransferHistory getPlayerTransferHistory(String playerName, String teamName);

	public abstract List<Pair<String, Integer>> getLeagueTopScorersForTheSeason(Integer seasonYear, String leagueName);

	public abstract List<Match> getSoccerMatches(Optional<String> leagueName, Optional<String> countryName,
			Optional<LocalDate> schedule);

	public abstract Match getFixtureStatistics(String homeTeam, String awayTeam, LocalDate matchDate);

	@Autowired
	public void setPersistenceAdapter(PersistenceAdapter persistenceAdapter) {
		this.persistenceAdapter = persistenceAdapter;
	}

}
