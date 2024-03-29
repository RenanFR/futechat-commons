package br.com.futechat.commons.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.futechat.commons.exception.PlayerNotFoundException;
import br.com.futechat.commons.model.League;
import br.com.futechat.commons.model.Match;
import br.com.futechat.commons.model.Player;
import br.com.futechat.commons.model.PlayerTransferHistory;
import br.com.futechat.commons.model.Team;

public abstract class FutechatService {
	
	private PersistenceAdapter persistenceAdapter;
	
	public String getPlayerHeight(String playerName, String teamName, Optional<String> countryName) {
		Optional<Player> possibleDatabasePlayer = persistenceAdapter.getPlayerByNameAndTeamName(playerName, teamName);
		if (possibleDatabasePlayer.isPresent()) {
			return possibleDatabasePlayer.get().getHeight();
		}
		Team team = countryName.isPresent() ? persistenceAdapter.getTeamByNameAndCountry(teamName, countryName.get())
				: persistenceAdapter.getTeamByName(teamName);
		Player player = getPlayer(playerName, team);
		return player.getHeight();
	}

	public PlayerTransferHistory getPlayerTransferHistory(String playerName, String teamName) {
		Player player = persistenceAdapter.getPlayerByNameAndTeamName(playerName, teamName)
				.orElseThrow(() -> new PlayerNotFoundException(playerName));
		PlayerTransferHistory playerTransferHistory = getPlayerTransfers(player);
		return playerTransferHistory;
	}

	public List<Pair<String, Integer>> getLeagueTopScorersForTheSeason(Integer seasonYear, String leagueName, String countryName) {
		return getLeagueTopScorersForTheSeason(seasonYear, persistenceAdapter.getLeagueByNameAndCountry(leagueName, countryName));
	}
	
	public List<Match> getSoccerMatches(Optional<String> leagueName, Optional<String> countryName,
			Optional<LocalDate> schedule) {
		if (leagueName.isPresent() && countryName.isPresent()) {

			return getSoccerMatches(
					Optional.ofNullable(
							persistenceAdapter.getLeagueByNameAndCountry(leagueName.get(), countryName.get())),
					schedule);
		}
		return getSoccerMatches(Optional.empty(), schedule);

	}
	
	public abstract Player getPlayer(String playerName, Team team);

	public abstract PlayerTransferHistory getPlayerTransfers(Player player);

	public abstract List<Pair<String, Integer>> getLeagueTopScorersForTheSeason(Integer seasonYear, League league);

	public abstract List<Match> getSoccerMatches(Optional<League> league,
			Optional<LocalDate> schedule);

	public abstract Match getFixtureStatistics(Integer idOfTheFixture);

	@Autowired
	public void setPersistenceAdapter(PersistenceAdapter persistenceAdapter) {
		this.persistenceAdapter = persistenceAdapter;
	}

}
