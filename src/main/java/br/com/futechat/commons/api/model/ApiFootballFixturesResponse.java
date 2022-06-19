package br.com.futechat.commons.api.model;

import java.util.List;

public record ApiFootballFixturesResponse(ApiFootballFixture fixture, ApiFootballLeague league,
		ApiFootballFixtureTeams teams, ApiFootballFixtureGoals goals, ApiFootballFixtureScore score,
		List<ApiFootballFixtureEvent> events) {

}
