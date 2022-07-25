package br.com.futechat.commons;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import br.com.futechat.commons.batch.ApiFootballLeagueProcessor;
import br.com.futechat.commons.batch.ApiFootballLeagueReader;
import br.com.futechat.commons.batch.ApiFootballLeagueWriter;
import br.com.futechat.commons.batch.BatchConfig;
import br.com.futechat.commons.batch.LeaguesDatabaseUpdateJob;
import br.com.futechat.commons.batch.LeaguesDatabaseUpdateJobScheduler;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({ FutechatCommonsConfig.class, DatabaseConfig.class, H2Config.class, BatchConfig.class,
		LeaguesDatabaseUpdateJobScheduler.class, LeaguesDatabaseUpdateJob.class, ApiFootballLeagueReader.class,
		ApiFootballLeagueWriter.class, ApiFootballLeagueProcessor.class })
public @interface EnableFutechatCommons {

}
