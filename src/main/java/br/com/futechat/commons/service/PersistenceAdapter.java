package br.com.futechat.commons.service;

import br.com.futechat.commons.model.League;
import br.com.futechat.commons.model.Team;

public abstract class PersistenceAdapter {
	
	public abstract Team getTeamByNameAndCountry(String teamName, String countryName);
	
	public abstract Team getTeamByName(String teamName);
	
	public abstract League getLeagueByName(String leagueName);
	
	public abstract League getLeagueByNameAndCountry(String leagueName, String countryName);

}
