package br.com.futechat.commons.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class League implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6221035712351692106L;
	private Integer id;
	private Integer apiFootballId;
	private String name;
	private String type;
	private String country;
	private String flag;
	private Integer season;
	private String logo;
	private List<Team> teams;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public List<Team> getTeams() {
		return teams;
	}

	public void setTeams(List<Team> teams) {
		this.teams = teams;
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
		League other = (League) obj;
		return Objects.equals(apiFootballId, other.apiFootballId);
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Integer getSeason() {
		return season;
	}

	public void setSeason(Integer season) {
		this.season = season;
	}

}
