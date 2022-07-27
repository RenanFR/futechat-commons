package br.com.futechat.commons;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.javatuples.Pair;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlConfig.ErrorMode;
import org.springframework.test.context.jdbc.SqlConfig.TransactionMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import br.com.futechat.commons.api.client.config.FeignConfig;
import br.com.futechat.commons.entity.LeagueEntity;
import br.com.futechat.commons.entity.PlayerEntity;
import br.com.futechat.commons.mapper.FutechatMapper;
import br.com.futechat.commons.mapper.FutechatMapperImpl;
import br.com.futechat.commons.model.Match;
import br.com.futechat.commons.repository.LeagueRepository;
import br.com.futechat.commons.repository.PlayerRepository;
import br.com.futechat.commons.repository.TeamRepository;
import br.com.futechat.commons.service.ApiFootballService;
import br.com.futechat.commons.service.FutechatService;
import br.com.futechat.commons.service.JPAPersistenceAdapter;
import br.com.futechat.commons.service.PersistenceAdapter;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = { ApiFootballService.class, FeignConfig.class, FutechatMapperImpl.class,
		JPAPersistenceAdapter.class, H2Config.class })
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ExtendWith(SpringExtension.class)
//@Sql(scripts = {
//		"/player_sample_record.sql" }, config = @SqlConfig(encoding = "utf-8", dataSource = "h2DataSource", transactionManager = "transactionManager", transactionMode = TransactionMode.ISOLATED), executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class ApiFootballServiceTest {

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(
			options().port(2345).extensions(new ResponseTemplateTransformer(false)).notifier(new Slf4jNotifier(true)));

	@Autowired
	private FutechatService futechatService;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private LeagueRepository leagueRepository;

	@Autowired
	private FutechatMapper mapper;

	private PersistenceAdapter persistenceAdapter;

	@Before
	public void setup() {
		persistenceAdapter = new JPAPersistenceAdapter(teamRepository, leagueRepository, playerRepository, mapper);
		futechatService.setPersistenceAdapter(persistenceAdapter);
	}

	@Test
	@Sql(scripts = {
			"/player_sample_record.sql" }, 
		config = @SqlConfig(encoding = "utf-8", dataSource = "h2DataSource", transactionManager = "transactionManager", transactionMode = TransactionMode.INFERRED, errorMode = ErrorMode.FAIL_ON_ERROR), 
		executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	public void shouldFetchNeyzinhoHeight() {
		assertEquals("175 cm", futechatService.getPlayerHeight("Neymar", "Paris Saint Germain", Optional.empty()));
	}

	@Test
	@Ignore
	public void shouldGetNeymarTransferHistory() {
//		mockNeymar();
		assertEquals("Santos",
				futechatService.getPlayerTransferHistory("Neymar", "Paris Saint Germain").transfers().get(0).teamOut());
	}

	private void mockNeymar() {
		PlayerEntity playerEntity = new PlayerEntity();
		playerEntity.setName("Neymar");
		playerEntity.setHeight("175 cm");
		playerEntity.setApiFootballId(276);
		Mockito.when(playerRepository.findByNameAndTeamName("Neymar", "Paris Saint Germain"))
				.thenReturn(Optional.of(playerEntity));
	}

	@Test
	@Ignore
	public void shouldGetPremierLeagueTopScorers() {
		mockPremierLeague();
		List<Pair<String, Integer>> leagueTopScorersForTheSeason = futechatService.getLeagueTopScorersForTheSeason(2021,
				"Premier League", "England");
		assertEquals("Son Heung-Min", leagueTopScorersForTheSeason.get(0).getValue0());
		assertEquals("23", leagueTopScorersForTheSeason.get(0).getValue1().toString());
	}

	@Test
	@Ignore
	public void shouldGetArsenalMostImportantFailureMatch() {
//		mockPremierLeague();
		List<Match> matchesPlayed = futechatService.getSoccerMatches(Optional.of("Premier League"),
				Optional.of("England"), Optional.of(LocalDate.of(2022, 5, 12)));
		assertEquals("Tottenham", matchesPlayed.get(1).homeTeam());
		assertEquals("Arsenal", matchesPlayed.get(1).awayTeam());
		assertEquals(3, matchesPlayed.get(1).homeScore());
		assertEquals(0, matchesPlayed.get(1).awayScore());
	}

	private void mockPremierLeague() {
		LeagueEntity leagueEntity = new LeagueEntity();
		leagueEntity.setName("Premier League");
		leagueEntity.setCountry("England");
		leagueEntity.setApiFootballId(39);
		Mockito.when(leagueRepository.findByNameAndCountry("Premier League", "England"))
				.thenReturn(Optional.of(leagueEntity));
	}

	@Test
	@Ignore
	public void shouldFetchLiveEvents() {
		List<Match> liveFixtureList = futechatService.getSoccerMatches(Optional.empty(), Optional.empty(),
				Optional.empty());
		assertNotNull(liveFixtureList);
		Match u20Fixture = liveFixtureList.stream()
				.filter(soccerFixture -> soccerFixture.homeTeam().equals("Palmeiras U20")
						&& soccerFixture.awayTeam().equals("Flamengo U20"))
				.findFirst().get();
		assertEquals(1, u20Fixture.homeScore());
		assertEquals(3, u20Fixture.awayScore());
		assertTrue(!u20Fixture.events().isEmpty());
	}

	@Test
	@Ignore
	public void shouldFindFutureSoccerFixtures() {
		List<Match> tomorrowFixtures = futechatService.getSoccerMatches(Optional.empty(), Optional.empty(),
				Optional.of(LocalDate.of(2022, 6, 20)));
		Match choqueRei = tomorrowFixtures.stream().filter(soccerFixture -> soccerFixture.homeTeam().equals("Sao Paulo")
				&& soccerFixture.awayTeam().equals("Palmeiras")).findFirst().get();
		assertEquals("Anderson Daronco", choqueRei.referee());
	}

	@Test
	@Ignore
	public void shouldGetArsenalDefeatStatistics() {
		Match fixtureStatistics = futechatService.getFixtureStatistics(710773);
		assertNotNull(fixtureStatistics);
		assertEquals(1, fixtureStatistics.awayTeamStatistics().redCards());
	}

}
