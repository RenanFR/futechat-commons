package br.com.futechat.commons.model;

import java.time.LocalDate;

public record Player(Integer id, int apiFootballId, String name, LocalDate birth, String placeOfBirth, String nationality,
		String countryOfBirth, String height, String weight, String photo, Team team) {
}
