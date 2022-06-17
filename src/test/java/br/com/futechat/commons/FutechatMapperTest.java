package br.com.futechat.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.futechat.commons.api.model.ApiFootballResponse;
import br.com.futechat.commons.api.model.ApiFootballTransfersResponse;
import br.com.futechat.commons.mapper.FutechatMapper;
import br.com.futechat.commons.mapper.FutechatMapperImpl;
import br.com.futechat.commons.model.PlayerTransferHistory;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = { FutechatMapperImpl.class, ObjectMapperConfig.class })
public class FutechatMapperTest {

	@Autowired
	private FutechatMapper mapper;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void shouldMapTransfersCorrectly() throws IOException {

		String transfersSampleResponse = new String(
				Files.readAllBytes(Paths.get("src/test/resources/__files/neymar-transfers-sample-response.json")));
		
		ApiFootballResponse<ApiFootballTransfersResponse> transfersResponse = objectMapper.readValue(transfersSampleResponse,
				new TypeReference<ApiFootballResponse<ApiFootballTransfersResponse>>() {
				});
		
		PlayerTransferHistory playerTransferHistory = mapper.fromApiFootballTransfersResponseToPlayerTransferHistory(transfersResponse);
		
		assertEquals("Paris Saint Germain", playerTransferHistory.transfers().get(0).teamIn());

	}

}
