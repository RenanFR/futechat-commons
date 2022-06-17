package br.com.futechat.commons.api.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public record ApiFootballTransfersResponse(ApiFootballPlayer player,
		@JsonDeserialize(using = ApiFootballTransfersResponseUpdateDeserializer.class) LocalDateTime update,
		List<ApiFootballTransfer> transfers) {

}
