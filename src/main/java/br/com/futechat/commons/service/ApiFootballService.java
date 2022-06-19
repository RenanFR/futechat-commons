package br.com.futechat.commons.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.futechat.commons.api.client.ApiFootballClient;
import br.com.futechat.commons.api.model.ApiFootballFixtureRequest;
import br.com.futechat.commons.api.model.ApiFootballFixturesResponse;
import br.com.futechat.commons.api.model.ApiFootballFixturesStatus;
import br.com.futechat.commons.api.model.ApiFootballLeague;
import br.com.futechat.commons.api.model.ApiFootballLeagueResponse;
import br.com.futechat.commons.api.model.ApiFootballLive;
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
import br.com.futechat.commons.model.Match;
import br.com.futechat.commons.model.MatchEvent;
import br.com.futechat.commons.model.PlayerTransferHistory;
import br.com.futechat.commons.model.Transfer;

@Service
public class ApiFootballService implements FutechatService {

	private static final String PATTERN = "yyyy-MM-dd";

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

	@Override
	public List<Match> getSoccerMatches(Optional<String> leagueName, Optional<String> countryName, Optional<LocalDate> schedule) {
		Map<String, String> parameters = new HashMap<String, String>();
		
		Integer leagueId = leagueName.isPresent() ? getLeagueByNameAndCountry(leagueName.get(), countryName) : null;
		
		ApiFootballFixtureRequest request = schedule.isPresent()
				? new ApiFootballFixtureRequest(schedule.get(), leagueId)
				: new ApiFootballFixtureRequest(leagueId);
		
		Optional.ofNullable(request.id()).ifPresent(id -> parameters.put("id", id.toString()));
		Optional.ofNullable(request.ids())
				.ifPresent(ids -> parameters.put("ids", ids.stream().collect(Collectors.joining("-"))));
		Optional.ofNullable(request.live())
				.ifPresent(live -> parameters.put("live", live == ApiFootballLive.ALL ? ApiFootballLive.ALL.getValue()
						: request.ids().stream().collect(Collectors.joining("-"))));
		Optional.ofNullable(request.date())
				.ifPresent(date -> parameters.put("date", date.format(DateTimeFormatter.ofPattern(PATTERN))));
		Optional.ofNullable(request.league()).ifPresent(league -> parameters.put(LEAGUE_PARAM, league.toString()));
		Optional.ofNullable(request.season()).ifPresent(season -> parameters.put(SEASON_PARAM, season.toString()));
		Optional.ofNullable(request.season()).ifPresent(team -> parameters.put(PLAYER_TEAM_PARAM, team.toString()));
		Optional.ofNullable(request.last()).ifPresent(last -> parameters.put("last", last.toString()));
		Optional.ofNullable(request.next()).ifPresent(next -> parameters.put("next", next.toString()));
		Optional.ofNullable(request.from())
				.ifPresent(from -> parameters.put("from", from.format(DateTimeFormatter.ofPattern(PATTERN))));
		Optional.ofNullable(request.to())
				.ifPresent(to -> parameters.put("to", to.format(DateTimeFormatter.ofPattern(PATTERN))));
		Optional.ofNullable(request.round()).ifPresent(round -> parameters.put("round", round.toString()));
		Optional.ofNullable(request.status())
				.ifPresent(status -> parameters.put("status", status == ApiFootballFixturesStatus._1H ? "1H"
						: status == ApiFootballFixturesStatus._2H ? "2H" : status.name()));
		Optional.ofNullable(request.venue()).ifPresent(venue -> parameters.put("venue", venue.toString()));
		Optional.ofNullable(request.timezone()).ifPresent(timezone -> parameters.put("timezone", timezone.toString()));
		List<Match> matchList = apiFootballClient.fixtures(parameters).response().stream().map(mapSoccerFixtureToMatchObject()).collect(Collectors.toList());
		return matchList;
	}

	private Function<? super ApiFootballFixturesResponse, ? extends Match> mapSoccerFixtureToMatchObject() {
		return fixture -> {
			
			List<ApiFootballFixturesStatus> noGoalMatchStatusList = List.of(ApiFootballFixturesStatus.TBD,
					ApiFootballFixturesStatus.NS, ApiFootballFixturesStatus.SUSP, ApiFootballFixturesStatus.INT,
					ApiFootballFixturesStatus.PST, ApiFootballFixturesStatus.CANC, ApiFootballFixturesStatus.ABD,
					ApiFootballFixturesStatus.AWD, ApiFootballFixturesStatus.WO);
			
			int goalsHome = noGoalMatchStatusList.contains(fixture.fixture().status().shortStatus()) ? null : fixture.goals().home();
			
			int goalsAway = noGoalMatchStatusList.contains(fixture.fixture().status().shortStatus()) ? null : fixture.goals().away();
			
			List<MatchEvent> events = new ArrayList<>();
			
			if (fixture.events() != null) {
				events.addAll(fixture.events().stream()
						.map(event -> new MatchEvent(event.time().elapsed(), event.type(), event.player().name()))
						.collect(Collectors.toList()));
			}
			
			Match match = new Match(fixture.teams().home().name(), fixture.teams().away().name(), goalsHome,
					goalsAway,
					LocalDateTime.parse(fixture.fixture().date(), DateTimeFormatter.ISO_OFFSET_DATE_TIME), fixture.fixture().status().longStatus(), events);
			return match;
		};
	}

	private int getLeagueByNameAndCountry(String leagueName, Optional<String> countryName) {
		Map<String, String> leagueQueryParameters = new HashMap<String, String>();
		
		leagueQueryParameters.put("name", leagueName);
		
		countryName.ifPresent(country -> leagueQueryParameters.put("country", country));
		
		int leagueId = apiFootballClient.leagues(leagueQueryParameters).response().stream()
				.map(ApiFootballLeagueResponse::league).mapToInt(ApiFootballLeague::id).findFirst()
				.orElseThrow(() -> new LeagueNotFoundException(leagueName));
		return leagueId;
	}

}
