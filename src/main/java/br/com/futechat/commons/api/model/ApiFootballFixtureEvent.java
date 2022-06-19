package br.com.futechat.commons.api.model;

public record ApiFootballFixtureEvent(ApiFootballFixtureEventTime time, ApiFootballTeam team, ApiFootballPlayer player,
		String type, String detail, String comments) {

}
