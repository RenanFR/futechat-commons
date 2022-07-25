package br.com.futechat.commons.model;

public record Team(int id, int apiFootballId, String name, String code, String country, String logo, int founded, League league) {
}
