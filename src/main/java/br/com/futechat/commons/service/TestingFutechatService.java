package br.com.futechat.commons.service;

import java.util.List;
import java.util.Optional;

import org.javatuples.Pair;
import org.springframework.stereotype.Service;

import br.com.futechat.commons.model.PlayerTransferHistory;

@Service
public class TestingFutechatService implements FutechatService {

	@Override
	public String getPlayerHeight(String playerName, String teamName, Optional<String> countryName) {
		return "1,65m";
	}

	@Override
	public PlayerTransferHistory getPlayerTransferHistory(String playerName, String teamName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Pair<String, Integer>> getLeagueTopScorersForTheSeason(Integer seasonYear, String leagueName) {
		// TODO Auto-generated method stub
		return null;
	}

}
