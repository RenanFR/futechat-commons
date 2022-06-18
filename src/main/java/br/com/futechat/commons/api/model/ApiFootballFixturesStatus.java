package br.com.futechat.commons.api.model;

public enum ApiFootballFixturesStatus {

	TBD("Time To Be Defined"), NS("Not Started"), _1H("First Half, Kick Off"), HT("Halftime"),
	_2H("Second Half, 2nd Half Started"), ET("Extra Time"), P("Penalty In Progress"), FT("Match Finished"),
	AET("Match Finished After Extra Time"), PEN("Match Finished After Penalty"), BT("Break Time (in Extra Time)"),
	SUSP("Match Suspended"), INT("Match Interrupted"), PST("Match Postponed"), CANC("Match Cancelled"),
	ABD("Match Abandoned"), AWD("Technical Loss"), WO("WalkOver"), LIVE("In Progress");

	private ApiFootballFixturesStatus(String description) {
		this.description = description;
	}

	private String description;

	public String getDescription() {
		return description;
	}

}
