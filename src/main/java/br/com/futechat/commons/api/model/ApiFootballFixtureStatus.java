package br.com.futechat.commons.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ApiFootballFixtureStatus(int elapsed, @JsonProperty("long") String longStatus,
		@JsonProperty("short") String shortStatus) {

}
