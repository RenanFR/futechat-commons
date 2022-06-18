package br.com.futechat.commons.api.model;

public record ApiFootballFixtureScore(ApiFootballFixtureGoals halftime, ApiFootballFixtureGoals fulltime,
		ApiFootballFixtureGoals extratime, ApiFootballFixtureGoals penalty) {

}
