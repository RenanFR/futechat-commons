package br.com.futechat.commons.service;

import java.util.List;
import java.util.Optional;

import org.javatuples.Pair;

import br.com.futechat.commons.api.model.ApiFootballFixtureRequest;
import br.com.futechat.commons.model.Match;
import br.com.futechat.commons.model.PlayerTransferHistory;

public interface FutechatService {

	String getPlayerHeight(String playerName, String teamName, Optional<String> countryName);
	
	PlayerTransferHistory getPlayerTransferHistory(String playerName, String teamName);
	
	List<Pair<String, Integer>> getLeagueTopScorersForTheSeason(Integer seasonYear, String leagueName);
	
	List<Match> getMatchesScheduleFor(ApiFootballFixtureRequest request);

}
