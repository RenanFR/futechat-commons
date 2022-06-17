package br.com.futechat.commons.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ApiFootballCoverage(ApiFootballFixtures fixtures, boolean standings, boolean players,
		@JsonProperty("top_scorers") boolean topScorers, @JsonProperty("top_assists") boolean topAssists,
		@JsonProperty("top_cards") boolean topCards, boolean injuries, boolean predictions, boolean odds) {

}
