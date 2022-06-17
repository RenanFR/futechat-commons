package br.com.futechat.commons.service;

public enum SourceApi {

	API_FOOTBALL(SourceApi.API_FOOTBALL_NAME),
	TESTING(SourceApi.TESTING_NAME);

	public static final String API_FOOTBALL_NAME = "apiFootball";
	public static final String TESTING_NAME = "testing";

	String apiName;

	SourceApi(String apiName) {
		this.apiName = apiName;
	}

	public String getApiName() {
		return apiName;
	}

}
