package br.com.futechat.commons;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import br.com.futechat.commons.batch.ApiFootballLeagueProcessor;
import br.com.futechat.commons.batch.ApiFootballLeagueReader;
import br.com.futechat.commons.batch.ApiFootballLeagueWriter;
import br.com.futechat.commons.batch.ApiFootballPlayerProcessor;
import br.com.futechat.commons.batch.ApiFootballPlayerReader;
import br.com.futechat.commons.batch.ApiFootballPlayerWriter;
import br.com.futechat.commons.batch.ApiFootballTeamProcessor;
import br.com.futechat.commons.batch.ApiFootballTeamReader;
import br.com.futechat.commons.batch.ApiFootballTeamWriter;
import br.com.futechat.commons.batch.BatchConfig;
import br.com.futechat.commons.batch.LeaguesDBSyncJob;
import br.com.futechat.commons.batch.PlayersDBSyncJob;
import br.com.futechat.commons.batch.TeamsDBSyncJob;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({ FutechatCommonsConfig.class, DatabaseConfig.class, H2Config.class, BatchConfig.class, TeamsDBSyncJob.class,
		LeaguesDBSyncJob.class, PlayersDBSyncJob.class, ApiFootballLeagueReader.class, ApiFootballLeagueWriter.class,
		ApiFootballLeagueProcessor.class, ApiFootballTeamReader.class, ApiFootballTeamProcessor.class,
		ApiFootballTeamWriter.class, ApiFootballPlayerReader.class, ApiFootballPlayerProcessor.class,
		ApiFootballPlayerWriter.class })
public @interface EnableFutechatCommons {

}
