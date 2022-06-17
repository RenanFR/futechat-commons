package br.com.futechat.commons.api.model;

import java.time.LocalDate;

public record ApiFootballSeason(int year, LocalDate start, LocalDate end, boolean current, ApiFootballCoverage coverage) {

}
