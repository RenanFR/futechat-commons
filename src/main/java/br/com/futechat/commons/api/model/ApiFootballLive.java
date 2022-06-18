package br.com.futechat.commons.api.model;

public enum ApiFootballLive {
	ALL("all"),
	IDS("id-id");
	
	private ApiFootballLive(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}

}
