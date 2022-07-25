package br.com.futechat.commons.model;

import java.util.List;

public record League(int id, int apiFootballId, String name, String type, String country, String logo, List<Team> teams) {

}
