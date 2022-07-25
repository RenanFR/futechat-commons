package br.com.futechat.commons.model;

import java.io.Serializable;
import java.util.Objects;

public class Team implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5022328501134528135L;
	private Integer id;
	private Integer apiFootballId;
	private String name;
	private String code;
	private String country;
	private String logo;
	private String stadium;
	private Integer stadiumCapacity;
	private String stadiumImage;
	private Integer stadiumApiFootballId;
	private String stadiumAddress;
	private int founded;
	private League league;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getApiFootballId() {
		return apiFootballId;
	}

	public void setApiFootballId(Integer apiFootballId) {
		this.apiFootballId = apiFootballId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public int getFounded() {
		return founded;
	}

	public void setFounded(int founded) {
		this.founded = founded;
	}

	public League getLeague() {
		return league;
	}

	public void setLeague(League league) {
		this.league = league;
	}

	public String getStadium() {
		return stadium;
	}

	public void setStadium(String stadium) {
		this.stadium = stadium;
	}

	public Integer getStadiumCapacity() {
		return stadiumCapacity;
	}

	public void setStadiumCapacity(Integer stadiumCapacity) {
		this.stadiumCapacity = stadiumCapacity;
	}

	public String getStadiumImage() {
		return stadiumImage;
	}

	public void setStadiumImage(String stadiumImage) {
		this.stadiumImage = stadiumImage;
	}

	public Integer getStadiumApiFootballId() {
		return stadiumApiFootballId;
	}

	public void setStadiumApiFootballId(Integer stadiumApiFootballId) {
		this.stadiumApiFootballId = stadiumApiFootballId;
	}

	public String getStadiumAddress() {
		return stadiumAddress;
	}

	public void setStadiumAddress(String stadiumAddress) {
		this.stadiumAddress = stadiumAddress;
	}

	@Override
	public int hashCode() {
		return Objects.hash(apiFootballId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Team other = (Team) obj;
		return Objects.equals(apiFootballId, other.apiFootballId);
	}
}
