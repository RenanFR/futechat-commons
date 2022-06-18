package br.com.futechat.commons.api.model;

public record ApiFootballFixturesResponse(ApiFootballFixture fixture, ApiFootballLeague league,
		ApiFootballFixtureTeams teams, ApiFootballFixtureGoals goals, ApiFootballFixtureScore score) {

}
