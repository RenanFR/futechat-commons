package br.com.futechat.commons.model;

import java.time.LocalDateTime;
import java.util.List;

public record Match(String homeTeam, String awayTeam, Integer homeScore, Integer awayScore, LocalDateTime schedule,
		String status,
		List<MatchEvent> events) {

}
