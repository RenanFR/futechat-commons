package br.com.futechat.commons.service.text;

import java.time.LocalDate;
import java.util.Optional;

public interface FutechatTextService {

	String getPlayerHeight(String playerName, String teamName, Optional<String> countryName);

	String getPlayerTransferHistory(String playerName, String teamName);
	
	String getLeagueTopScorersForTheSeason(Integer seasonYear, String leagueName);
	
	String getSoccerMatches(Optional<String> leagueName, Optional<String> countryName, Optional<LocalDate> schedule);
	
	String getFixtureStatistics(String homeTeam, String awayTeam, LocalDate matchDate);

}
