package br.com.futechat.commons.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.javatuples.Pair;

import br.com.futechat.commons.model.Match;
import br.com.futechat.commons.model.PlayerTransferHistory;

public interface FutechatService {

	String getPlayerHeight(String playerName, String teamName, Optional<String> countryName);

	PlayerTransferHistory getPlayerTransferHistory(String playerName, String teamName);

	List<Pair<String, Integer>> getLeagueTopScorersForTheSeason(Integer seasonYear, String leagueName);

	List<Match> getSoccerMatches(Optional<String> leagueName, Optional<String> countryName,
			Optional<LocalDate> schedule);

	Match getFixtureStatistics(String homeTeam, String awayTeam, LocalDate matchDate);

}
