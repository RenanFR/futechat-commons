package br.com.futechat.commons.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "team_tbl")
public class TeamEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String code;
	private String country;
	private String logo;
	private Integer founded;
	
	private String stadium;
	
	@Column(name = "stadium_capacity")
	private Integer stadiumCapacity;
	
	@Column(name = "stadium_image")
	private String stadiumImage;
	
	@Column(name = "stadium_api_football_id")
	private Integer stadiumApiFootballId;
	
	@Column(name = "stadium_address")
	private String stadiumAddress;
	
    @OneToMany(mappedBy="team")
    private List<PlayerEntity> players;

	@Column(name = "api_football_id")
	private Integer apiFootballId;
	
    @ManyToOne
    @JoinColumn(name="league_id", nullable=false)
    private LeagueEntity league;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getFounded() {
		return founded;
	}

	public void setFounded(Integer founded) {
		this.founded = founded;
	}

	public Integer getApiFootballId() {
		return apiFootballId;
	}

	public void setApiFootballId(Integer apiFootballId) {
		this.apiFootballId = apiFootballId;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public List<PlayerEntity> getPlayers() {
		return players;
	}

	public void setPlayers(List<PlayerEntity> players) {
		this.players = players;
	}

	public LeagueEntity getLeague() {
		return league;
	}

	public void setLeague(LeagueEntity league) {
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

}
