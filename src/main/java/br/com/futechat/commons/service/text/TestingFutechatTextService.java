package br.com.futechat.commons.service.text;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.futechat.commons.service.SourceApi;
import br.com.futechat.commons.service.TestingFutechatService;

@Service(SourceApi.TESTING_NAME)
public class TestingFutechatTextService implements FutechatTextService {

	@Autowired
	private TestingFutechatService futechatService;

	@Override
	public String getPlayerHeight(String playerName, String teamName, Optional<String> countryName) {
		return futechatService.getPlayerHeight(playerName, teamName, countryName);
	}

	@Override
	public String getPlayerTransferHistory(String playerName, String teamName) {
		return String.valueOf(futechatService.getPlayerTransferHistory(playerName, teamName));
	}

	@Override
	public String getLeagueTopScorersForTheSeason(Integer seasonYear, String leagueName) {
		// TODO Auto-generated method stub
		return null;
	}

}
