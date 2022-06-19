package br.com.futechat.commons.api.model;

import java.util.List;

public record ApiFootballStatisticsResponse(List<ApiFootballStatistics> statistics, ApiFootballTeam team) {

}
