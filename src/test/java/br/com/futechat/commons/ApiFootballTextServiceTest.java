package br.com.futechat.commons;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlConfig.TransactionMode;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import br.com.futechat.commons.api.client.config.FeignConfig;
import br.com.futechat.commons.aspect.ApiFootballAspect;
import br.com.futechat.commons.aspect.AspectJConfig;
import br.com.futechat.commons.mapper.FutechatMapperImpl;
import br.com.futechat.commons.service.ApiFootballService;
import br.com.futechat.commons.service.JPAPersistenceAdapter;
import br.com.futechat.commons.service.text.ApiFootballTextService;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = { ApiFootballService.class, ApiFootballTextService.class, FeignConfig.class,
		ApiFootballAspect.class, AspectJConfig.class, FutechatMapperImpl.class, JPAPersistenceAdapter.class,
		H2Config.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {
		"/player_sample_record.sql" }, config = @SqlConfig(encoding = "utf-8", dataSource = "h2DataSource", transactionManager = "transactionManager", transactionMode = TransactionMode.ISOLATED), executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class ApiFootballTextServiceTest {

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(
			options().port(2345).extensions(new ResponseTemplateTransformer(false)).notifier(new Slf4jNotifier(true)));

	@Autowired
	private ApiFootballTextService apiFootballTextService;

	@Test
	public void shouldGetNeymarTransferHistoryText() {
		String playerTransferHistoryText = apiFootballTextService.getPlayerTransferHistory("Neymar",
				"Paris Saint Germain");
		assertTrue(playerTransferHistoryText.contains("Transferências"));
	}

	@Test
	public void whenNonExistingTeamIsSearchedThenExceptionTextShouldBeReturned() {
		String playerTransferHistoryText = apiFootballTextService.getPlayerTransferHistory("Rogerio Vaughan", "ESPN");
		assertEquals("O time ESPN nao foi encontrado", playerTransferHistoryText);
	}
	
	@Test
	public void whenNonExistingPlayerIsSearchedThenExceptionTextShouldBeReturned() {
		String playerTransferHistoryText = apiFootballTextService.getPlayerTransferHistory("Rogerio Vaughan", "Paris Saint Germain");
		assertEquals("O jogador Rogerio Vaughan nao foi encontrado", playerTransferHistoryText);
	}
	
	@Test
	public void whenNonExistingLeagueIsSearchedThenExceptionTextShouldBeReturned() {
		String leagueTopScorers = apiFootballTextService.getLeagueTopScorersForTheSeason(2021, "Desimpedidos", "England");
		assertEquals("A liga Desimpedidos nao foi encontrada", leagueTopScorers);
	}

	@Test
	public void textWithTopScorersShouldBeAvailableToRead() {
		String leagueTopScorersForTheSeason = apiFootballTextService.getLeagueTopScorersForTheSeason(2021,
				"Premier League", "England");
		assertTrue(leagueTopScorersForTheSeason.contains("Artilheiros"));
	}
	
	@Test
	public void shouldGetArsenalMostImportantFailureMatch() {
		String matches = apiFootballTextService.getSoccerMatches(Optional.of("Premier League"), Optional.of("England"),
				Optional.of(LocalDate.of(2022, 5, 12)));
		assertTrue(matches.contains("Partidas encontradas"));
		assertTrue(matches.contains("Tottenham(3) X Arsenal(0)"));
	}
	
	@Test
	public void shouldFetchLiveEvents() {
		String matchEventListText = apiFootballTextService.getSoccerMatches(Optional.empty(), Optional.empty(),
				Optional.empty());
		assertNotNull(matchEventListText);
		assertTrue(matchEventListText.contains(
				"Palmeiras U20(1) X Flamengo U20(3) -> 19/06/2022 14:00 -> [33/Goal/W. de Almeida Rego | 75/Goal/Jose Welinton | 81/Goal/J. Pedro | 90/Goal]"));
	}
	
	@Test
	public void shouldFindFutureSoccerFixtures() {
		String soccerMatchesForTomorrow = apiFootballTextService.getSoccerMatches(Optional.empty(), Optional.empty(),
				Optional.of(LocalDate.of(2022, 6, 20)));
		assertNotNull(soccerMatchesForTomorrow);
		assertTrue(soccerMatchesForTomorrow.contains("Sao Paulo X Palmeiras -> 20/06/2022 23:00"));
	}
	
	@Test
	public void shouldGetArsenalDefeatStatistics() {
		String fixtureStatisticsText = apiFootballTextService.getFixtureStatistics(710773);
		assertNotNull(fixtureStatisticsText);
		assertTrue(fixtureStatisticsText.contains("Tottenham X Arsenal:"));
		assertTrue(fixtureStatisticsText.contains("Cartões vermelhos | 0 | 1"));
	}

}
