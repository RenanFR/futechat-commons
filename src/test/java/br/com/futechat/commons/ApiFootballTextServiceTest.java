package br.com.futechat.commons;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import br.com.futechat.commons.api.client.config.FeignConfig;
import br.com.futechat.commons.aspect.ApiFootballAspect;
import br.com.futechat.commons.aspect.AspectJConfig;
import br.com.futechat.commons.mapper.FutechatMapperImpl;
import br.com.futechat.commons.service.ApiFootballService;
import br.com.futechat.commons.service.text.ApiFootballTextService;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = { ApiFootballService.class, ApiFootballTextService.class, FeignConfig.class,
		ApiFootballAspect.class, AspectJConfig.class, FutechatMapperImpl.class })
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
		String leagueTopScorers = apiFootballTextService.getLeagueTopScorersForTheSeason(2021, "Desimpedidos");
		assertEquals("A liga Desimpedidos nao foi encontrada", leagueTopScorers);
	}

	@Test
	public void textWithTopScorersShouldBeAvailableToRead() {
		String leagueTopScorersForTheSeason = apiFootballTextService.getLeagueTopScorersForTheSeason(2021,
				"Premier League");
		assertTrue(leagueTopScorersForTheSeason.contains("Artilheiros"));
	}

}
