package br.com.futechat.commons.api.model;

public record ApiFootballFixture(int id, String referee, String timezone, String date, Long timestamp,
		ApiFootballFixturePeriods periods, ApiFootballFixtureVenue venue, ApiFootballFixtureStatus status) {

}
