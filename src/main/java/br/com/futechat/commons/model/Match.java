package br.com.futechat.commons.model;

import java.time.LocalDateTime;

public record Match(String homeTeam, String awayTeam, Integer homeScore, Integer awayScore, LocalDateTime schedule) {

}
