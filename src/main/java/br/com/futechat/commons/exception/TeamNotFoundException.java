package br.com.futechat.commons.exception;

public class TeamNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String teamName;

	public TeamNotFoundException(String teamName) {
		super("O time " + teamName + " nao foi encontrado");
		this.teamName = teamName;
	}

	public String getTeamName() {
		return teamName;
	}
}
