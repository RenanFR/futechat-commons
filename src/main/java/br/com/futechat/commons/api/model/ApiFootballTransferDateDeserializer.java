package br.com.futechat.commons.api.model;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class ApiFootballTransferDateDeserializer extends JsonDeserializer<LocalDate> {

	@Override
	public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
		LocalDate transferDate = null;
		try {
			transferDate = LocalDate.parse(p.getText(), DateTimeFormatter.ISO_LOCAL_DATE);

		} catch (Exception e) {
			transferDate = LocalDate.parse(p.getText(), DateTimeFormatter.ofPattern("ddMMyy"));
		}
		return transferDate;
	}

}
