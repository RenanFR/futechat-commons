package br.com.futechat.commons.service;

public enum SourceApi {

	API_FOOTBALL(SourceApi.API_FOOTBALL_NAME);

	public static final String API_FOOTBALL_NAME = "apiFootball";

	String apiName;

	SourceApi(String apiName) {
		this.apiName = apiName;
	}

	public String getApiName() {
		return apiName;
	}

}
