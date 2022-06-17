package br.com.futechat.commons.api.model;

import java.util.List;

public record ApiFootballLeagueResponse(ApiFootballLeague league, ApiFootballCountry country,
		List<ApiFootballSeason> seasons) {

}
