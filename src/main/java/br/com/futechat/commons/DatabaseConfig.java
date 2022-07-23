package br.com.futechat.commons;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("br.com.futechat.commons.entity")
@EnableJpaRepositories("br.com.futechat.commons.repository")
public class DatabaseConfig {

}
