package br.com.futechat.commons.exception;

public class LeagueNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String leagueName;
	
	public LeagueNotFoundException(String leagueName) {
		super("A liga " + leagueName + " nao foi encontrada");
		this.leagueName = leagueName;
	}

	public String getLeagueName() {
		return leagueName;
	}

}
