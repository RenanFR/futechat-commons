package br.com.futechat.commons.api.model;

public enum ApiFootballStatisticsType {
	SHOTS_ON_GOAL("Shots on Goal"), SHOTS_OFF_GOAL("Shots off Goal"), TOTAL_SHOTS("Total Shots"),
	BLOCKED_SHOTS("Blocked Shots"), SHOTS_INSIDEBOX("Shots insidebox"), SHOTS_OUTSIDEBOX("Shots outsidebox"),
	FOULS("Fouls"), CORNER_KICKS("Corner Kicks"), OFFSIDES("Offsides"), BALL_POSSESSION("Ball Possession"),
	YELLOW_CARDS("Yellow Cards"), RED_CARDS("Red Cards"), GOALKEEPER_SAVES("Goalkeeper Saves"),
	TOTAL_PASSES("Total passes"), PASSES_ACCURATE("Passes accurate"), PASSES("Passes %");

	private String rawValue;

	private ApiFootballStatisticsType(String rawValue) {
		this.rawValue = rawValue;
	}

	public String getRawValue() {
		return rawValue;
	}
}
