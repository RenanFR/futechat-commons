package br.com.futechat.commons.exception;

public class PlayerNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String playerName;

	public PlayerNotFoundException(String playerName) {
		super("O jogador " + playerName + " nao foi encontrado");
		this.playerName = playerName;
	}

	public String getPlayerName() {
		return playerName;
	}

}
