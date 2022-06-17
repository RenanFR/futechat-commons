package br.com.futechat.commons.model;

import java.time.LocalDate;

public record Transfer(LocalDate date, String type, String teamIn, String teamOut) {

}
