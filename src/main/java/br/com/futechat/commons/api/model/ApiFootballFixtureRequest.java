package br.com.futechat.commons.api.model;

import java.time.LocalDate;
import java.util.List;

public record ApiFootballFixtureRequest(Integer id, List<String> ids, ApiFootballLive live, LocalDate date, Integer league,
		Integer season, Integer team, Integer last, Integer next, LocalDate from, LocalDate to, String round,
		ApiFootballFixturesStatus status, Integer venue, String timezone) {
	
	public ApiFootballFixtureRequest(LocalDate date, Integer league) {
		this(null, null, null, date, league, null, null, null, null, null, null, null, null, null, null);
	}
	
	public ApiFootballFixtureRequest(Integer league) {
		this(null, null, ApiFootballLive.ALL, null, league, null, null, null, null, null, null, null, null, null, null);
	}
}
