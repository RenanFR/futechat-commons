package br.com.futechat.commons.api.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;

@Configuration
@EnableFeignClients({ "br.com.futechat.commons.api.client" })
@ImportAutoConfiguration({ FeignAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class })
public class FeignConfig {

	@Value("${apiFootball.key}")
	private String apiFootballKey;
	
	@Value("${apiFootball.host}")
	private String apiFootballHost;

	@Bean
	public RequestInterceptor requestInterceptor() {
		return requestTemplate -> {
			requestTemplate.header("X-RapidAPI-Host", apiFootballHost);
			requestTemplate.header("X-RapidAPI-Key", apiFootballKey);
		};
	}

}
