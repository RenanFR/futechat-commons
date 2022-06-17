package br.com.futechat.commons.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.futechat.commons.api.client.ApiFootballClient;
import br.com.futechat.commons.api.model.ApiFootballLeague;
import br.com.futechat.commons.api.model.ApiFootballLeagueResponse;
import br.com.futechat.commons.api.model.ApiFootballPlayer;
import br.com.futechat.commons.api.model.ApiFootballPlayerGoals;
import br.com.futechat.commons.api.model.ApiFootballPlayerStatistics;
import br.com.futechat.commons.api.model.ApiFootballPlayersResponse;
import br.com.futechat.commons.api.model.ApiFootballResponse;
import br.com.futechat.commons.api.model.ApiFootballTeam;
import br.com.futechat.commons.api.model.ApiFootballTeamsResponse;
import br.com.futechat.commons.api.model.ApiFootballTransfersResponse;
import br.com.futechat.commons.exception.LeagueNotFoundException;
import br.com.futechat.commons.exception.PlayerNotFoundException;
import br.com.futechat.commons.exception.TeamNotFoundException;
import br.com.futechat.commons.mapper.FutechatMapper;
import br.com.futechat.commons.model.PlayerTransferHistory;
import br.com.futechat.commons.model.Transfer;

@Service
public class ApiFootballService implements FutechatService {

	private static final String PLAYER_TEAM_PARAM = "team";

	private static final String PLAYER_PARAM = "player";

	private static final String SEARCH_PARAM = "search";

	private static final String LEAGUE_PARAM = "league";

	private static final String SEASON_PARAM = "season";

	private static final String NAME_PARAM = "name";

	private ApiFootballClient apiFootballClient;

	private FutechatMapper mapper;

	@Autowired
	public ApiFootballService(ApiFootballClient apiFootballClient, FutechatMapper mapper) {
		this.apiFootballClient = apiFootballClient;
		this.mapper = mapper;
	}

	@Override
	public String getPlayerHeight(String playerName, String teamName, Optional<String> countryName) {
		ApiFootballResponse<ApiFootballTeamsResponse> teams = apiFootballClient.teams(Map.of(NAME_PARAM, teamName));
		ApiFootballTeam team = teams.response().stream().map(ApiFootballTeamsResponse::team).findFirst()
				.orElseThrow(() -> new TeamNotFoundException(teamName));
		Integer teamId = team.id();
		ApiFootballPlayer player = getPlayerByNameAndTeamId(playerName, teamId);
		return player.height();
	}

	private ApiFootballPlayer getPlayerByNameAndTeamId(String playerName, Integer teamId) {
		return apiFootballClient.players(Map.of(SEARCH_PARAM, playerName, PLAYER_TEAM_PARAM, teamId.toString()))
				.response().stream().map(ApiFootballPlayersResponse::player).findFirst()
				.orElseThrow(() -> new PlayerNotFoundException(playerName));
	}

	@Override
	public PlayerTransferHistory getPlayerTransferHistory(String playerName, String teamName) {
		int teamId = apiFootballClient.teams(Map.of(NAME_PARAM, teamName)).response().stream()
				.map(ApiFootballTeamsResponse::team).findAny().orElseThrow(() -> new TeamNotFoundException(teamName))
				.id();
		ApiFootballPlayer player = getPlayerByNameAndTeamId(playerName, teamId);
		ApiFootballResponse<ApiFootballTransfersResponse> transfers = apiFootballClient
				.transfers(Map.of(PLAYER_PARAM, String.valueOf(player.id())));
		PlayerTransferHistory playerTransferHistory = mapper
				.fromApiFootballTransfersResponseToPlayerTransferHistory(transfers);
		playerTransferHistory.transfers().sort(Comparator.comparing(Transfer::date));
		return playerTransferHistory;

	}

	@Override
	public List<Pair<String, Integer>> getLeagueTopScorersForTheSeason(Integer seasonYear, String leagueName) {
		Integer leagueId = apiFootballClient
				.leagues(Map.of(NAME_PARAM, leagueName, SEASON_PARAM, seasonYear.toString())).response().stream()
				.filter(leagueResponse -> leagueResponse.seasons().get(0).coverage().topScorers()).limit(1)
				.map(ApiFootballLeagueResponse::league).mapToInt(ApiFootballLeague::id).findFirst()
				.orElseThrow(() -> new LeagueNotFoundException(leagueName));
		List<Pair<String, Integer>> topScorers = apiFootballClient
				.topScorers(Map.of(LEAGUE_PARAM, leagueId.toString(), SEASON_PARAM, seasonYear.toString())).response()
				.stream()
				.map((ApiFootballPlayersResponse apiFootballPlayersResponse) -> new Pair<String, Integer>(
						apiFootballPlayersResponse.player().name(), apiFootballPlayersResponse.statistics().stream()
								.map(ApiFootballPlayerStatistics::goals).mapToInt(ApiFootballPlayerGoals::total).sum()))
				.collect(Collectors.toList());
		return topScorers;

	}

}
