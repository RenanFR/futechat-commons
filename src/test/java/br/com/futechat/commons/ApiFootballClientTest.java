package br.com.futechat.commons;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.futechat.commons.api.client.ApiFootballClient;
import br.com.futechat.commons.api.client.config.FeignConfig;
import br.com.futechat.commons.api.model.ApiFootballFixturesResponse;
import br.com.futechat.commons.api.model.ApiFootballLeagueResponse;
import br.com.futechat.commons.api.model.ApiFootballPlayersResponse;
import br.com.futechat.commons.api.model.ApiFootballResponse;
import br.com.futechat.commons.api.model.ApiFootballStatisticsResponse;
import br.com.futechat.commons.api.model.ApiFootballTeamsResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { FeignConfig.class, FutechatCommonsConfig.class })
public class ApiFootballClientTest {
	
	@Autowired
	private ApiFootballClient apiFootballClient;

	@Test
	public void shouldFetchSeasonsFromApi() {

		ApiFootballResponse<Integer> seasons = apiFootballClient.seasons();
		assertNotNull(seasons);
		assertEquals("leagues/seasons", seasons.get());
		assertTrue(seasons.response().contains(LocalDate.now().getYear()));
	}

	@Test
	public void shouldFetchLeagueInformationFromApi() {
		ApiFootballResponse<ApiFootballLeagueResponse> leagues = apiFootballClient
				.leagues(Map.of("name", "Premier League"));
		assertEquals("leagues", leagues.get());
		ApiFootballLeagueResponse premierLeague = leagues.response().get(0);
		assertNotNull(premierLeague);
		assertEquals("England", premierLeague.country().name());
	}

	@Test
	public void shouldFetchTeamsInformationFromApi() {
		ApiFootballResponse<ApiFootballTeamsResponse> teams = apiFootballClient.teams(Map.of("name", "Arsenal"));
		assertEquals("teams", teams.get());
		assertEquals("England", teams.response().get(0).team().country());
	}
	
	@Test
	public void shouldFetchAdultoNey() {
		ApiFootballResponse<ApiFootballPlayersResponse> players = apiFootballClient.players(Map.of("search", "Neymar", "team", "85"));
		assertEquals("players", players.get());
		assertEquals("da Silva Santos Júnior", players.response().get(0).player().lastname());
	}
	
	@Test
	public void shouldGetTopScorers() {
		ApiFootballResponse<ApiFootballPlayersResponse> topScorers = apiFootballClient
				.topScorers(Map.of("league", "39", "season", "2021"));
		assertEquals("players/topscorers", topScorers.get());
		assertEquals("Heung-Min Son", topScorers.response().get(0).player().name());
		assertEquals(23, topScorers.response().get(0).statistics().get(0).goals().total());
	}
	
	@Test
	public void shouldGetArsenalMostImportantFailureMatch() {
		ApiFootballResponse<ApiFootballFixturesResponse> fixtures = apiFootballClient
				.fixtures(Map.of("league", "39", "season", "2021", "date", "2022-05-12"));
		assertEquals("fixtures", fixtures.get());
		ApiFootballFixturesResponse apiFootballFixturesResponse = fixtures.response().stream()
				.filter(match -> match.fixture().id() == 710773).findFirst().get();
		assertEquals(3, apiFootballFixturesResponse.goals().home());
		assertEquals(0, apiFootballFixturesResponse.goals().away());
	}
	
	@Test
	public void shouldGetArsenalDefeatStatistics() {
		ApiFootballResponse<ApiFootballStatisticsResponse> fixturesStatistics = apiFootballClient
				.fixturesStatistics(Map.of("fixture", "710773"));
		assertEquals("fixtures/statistics", fixturesStatistics.get());
		String redCards = fixturesStatistics.response().stream()
				.filter(statistics -> statistics.team().name().equals("Arsenal"))
				.flatMap(statistics -> statistics.statistics().stream())
				.filter(statistics -> statistics.type().equals("Red Cards")).findFirst().get().value();
		assertEquals("1", redCards);
	}

}
