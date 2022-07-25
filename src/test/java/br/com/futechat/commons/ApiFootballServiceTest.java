package br.com.futechat.commons;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.javatuples.Pair;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import br.com.futechat.commons.api.client.config.FeignConfig;
import br.com.futechat.commons.entity.PlayerEntity;
import br.com.futechat.commons.entity.TeamEntity;
import br.com.futechat.commons.mapper.FutechatMapper;
import br.com.futechat.commons.mapper.FutechatMapperImpl;
import br.com.futechat.commons.model.Match;
import br.com.futechat.commons.repository.PlayerRepository;
import br.com.futechat.commons.repository.TeamRepository;
import br.com.futechat.commons.repository.TransferRepository;
import br.com.futechat.commons.service.ApiFootballService;
import br.com.futechat.commons.service.FutechatService;
import br.com.futechat.commons.service.JPAPersistenceAdapter;
import br.com.futechat.commons.service.PersistenceAdapter;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = { ApiFootballService.class, FeignConfig.class, FutechatMapperImpl.class,
		JPAPersistenceAdapter.class, H2Config.class, PlayerRepository.class, TeamRepository.class,
		TransferRepository.class })
public class ApiFootballServiceTest {

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(
			options().port(2345).extensions(new ResponseTemplateTransformer(false)).notifier(new Slf4jNotifier(true)));

	@Autowired
	private FutechatService futechatService;
	
	@Mock
	private PlayerRepository playerRepository;
	
	@Mock
	private TeamRepository teamRepository;
	
	@Mock
	private TransferRepository transferRepository;
	
	@Autowired
	private FutechatMapper mapper;
	
	private PersistenceAdapter persistenceAdapter;
	
	@Before
	public void setup() {
		persistenceAdapter = new JPAPersistenceAdapter(playerRepository, teamRepository, transferRepository, mapper);
		futechatService.setPersistenceAdapter(persistenceAdapter);
	}
	
	@Test
	public void shouldFetchNeyzinhoHeight() {
		assertEquals("175 cm", futechatService.getPlayerHeight("Neymar", "Paris Saint Germain", Optional.empty()));
		verify(playerRepository, times(1)).save(any(PlayerEntity.class));
		verify(teamRepository, times(1)).save(any(TeamEntity.class));
	}

	@Test
	public void shouldGetNeymarTransferHistory() {
		assertEquals("Santos", futechatService.getPlayerTransferHistory("Neymar", "Paris Saint Germain")
				.transfers().get(0).teamOut());
		verify(teamRepository, times(1)).save(any(TeamEntity.class));
		verify(playerRepository, times(1)).save(any(PlayerEntity.class));
		verify(transferRepository, times(1)).saveAll(ArgumentMatchers.anyList());
	}
	
	@Test
	public void shouldGetPremierLeagueTopScorers() {
		List<Pair<String, Integer>> leagueTopScorersForTheSeason = futechatService.getLeagueTopScorersForTheSeason(2021, "Premier League");
		assertEquals("Son Heung-Min", leagueTopScorersForTheSeason.get(0).getValue0());
		assertEquals("23", leagueTopScorersForTheSeason.get(0).getValue1().toString());
	}
	
	@Test
	public void shouldGetArsenalMostImportantFailureMatch() {
		
		List<Match> matchesPlayed = futechatService.getSoccerMatches(Optional.of("Premier League"), Optional.of("England"),
				Optional.of(LocalDate.of(2022, 5, 12)));
		assertEquals("Tottenham", matchesPlayed.get(1).homeTeam());
		assertEquals("Arsenal", matchesPlayed.get(1).awayTeam());
		assertEquals(3, matchesPlayed.get(1).homeScore());
		assertEquals(0, matchesPlayed.get(1).awayScore());
	}
	
	@Test
	public void shouldFetchLiveEvents() {
		List<Match> liveFixtureList = futechatService.getSoccerMatches(Optional.empty(), Optional.empty(), Optional.empty());
		assertNotNull(liveFixtureList);
		Match u20Fixture = liveFixtureList.stream().filter(soccerFixture -> soccerFixture.homeTeam().equals("Palmeiras U20")
				&& soccerFixture.awayTeam().equals("Flamengo U20")).findFirst().get();
		assertEquals(1, u20Fixture.homeScore());
		assertEquals(3, u20Fixture.awayScore());
		assertTrue(!u20Fixture.events().isEmpty());
	}
	
	@Test
	public void shouldFindFutureSoccerFixtures() {
		List<Match> tomorrowFixtures = futechatService.getSoccerMatches(Optional.empty(), Optional.empty(), Optional.of(LocalDate.of(2022, 6, 20)));
		Match choqueRei = tomorrowFixtures.stream().filter(soccerFixture -> soccerFixture.homeTeam().equals("Sao Paulo")
				&& soccerFixture.awayTeam().equals("Palmeiras")).findFirst().get();
		assertEquals("Anderson Daronco", choqueRei.referee());
	}
	
	@Test
	public void shouldGetArsenalDefeatStatistics() {
		Match fixtureStatistics = futechatService.getFixtureStatistics("Tottenham", "Arsenal", LocalDate.of(2022, 5, 12));
		assertNotNull(fixtureStatistics);
		assertEquals(1, fixtureStatistics.awayTeamStatistics().redCards());
	}
	
}
