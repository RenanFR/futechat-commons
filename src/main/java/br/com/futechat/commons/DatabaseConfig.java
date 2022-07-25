package br.com.futechat.commons;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EntityScan("br.com.futechat.commons.entity")
@Profile("!test")
public class DatabaseConfig {

}
