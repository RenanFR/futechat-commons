package br.com.futechat.commons;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.javatuples.Pair;
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
import br.com.futechat.commons.mapper.FutechatMapperImpl;
import br.com.futechat.commons.service.ApiFootballService;
import br.com.futechat.commons.service.FutechatService;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = { ApiFootballService.class, FeignConfig.class,
		FutechatMapperImpl.class })
public class ApiFootballServiceTest {

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(
			options().port(2345).extensions(new ResponseTemplateTransformer(false)).notifier(new Slf4jNotifier(true)));

	@Autowired
	private FutechatService futechatService;

	@Test
	public void shouldFetchNeyzinhoHeight() {
		assertEquals("175 cm", futechatService.getPlayerHeight("Neymar", "Paris Saint Germain", Optional.empty()));
	}

	@Test
	public void shouldGetNeymarTransferHistory() {
		assertEquals("Santos", futechatService.getPlayerTransferHistory("Neymar", "Paris Saint Germain")
				.transfers().get(0).teamOut());
	}
	
	@Test
	public void shouldGetPremierLeagueTopScorers() {
		List<Pair<String, Integer>> leagueTopScorersForTheSeason = futechatService.getLeagueTopScorersForTheSeason(2021, "Premier League");
		assertEquals("Son Heung-Min", leagueTopScorersForTheSeason.get(0).getValue0());
		assertEquals("23", leagueTopScorersForTheSeason.get(0).getValue1().toString());
	}
	
}
