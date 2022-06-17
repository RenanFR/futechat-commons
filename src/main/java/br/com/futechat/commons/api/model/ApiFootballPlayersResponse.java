package br.com.futechat.commons.api.model;

import java.util.List;

public record ApiFootballPlayersResponse(ApiFootballPlayer player, List<ApiFootballPlayerStatistics> statistics) {

}
