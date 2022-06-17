package br.com.futechat.commons.api.model;

import java.time.LocalDate;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public record ApiFootballTransfer(@JsonDeserialize(using = ApiFootballTransferDateDeserializer.class) LocalDate date,
		String type, @JsonProperty("teams") Map<String, ApiFootballTeam> teamsInvolved) {

}
