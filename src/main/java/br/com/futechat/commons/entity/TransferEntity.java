package br.com.futechat.commons.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "transfer_tbl")
public class TransferEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
    @ManyToOne
    @JoinColumn(name="team_in_id", nullable=false)
    private TeamEntity teamIn;
    
    @ManyToOne
    @JoinColumn(name="team_out_id", nullable=false)
    private TeamEntity teamOut;
    
    @ManyToOne
    @JoinColumn(name="player_id", nullable=false)
    private PlayerEntity player;
    
    @Column(name = "transfer_date")
    private LocalDate transferDate;
    
    @Column(name = "transfer_type")
    private String transferType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TeamEntity getTeamIn() {
		return teamIn;
	}

	public void setTeamIn(TeamEntity teamIn) {
		this.teamIn = teamIn;
	}

	public TeamEntity getTeamOut() {
		return teamOut;
	}

	public void setTeamOut(TeamEntity teamOut) {
		this.teamOut = teamOut;
	}

	public PlayerEntity getPlayer() {
		return player;
	}

	public void setPlayer(PlayerEntity player) {
		this.player = player;
	}

	public LocalDate getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(LocalDate transferDate) {
		this.transferDate = transferDate;
	}

	public String getTransferType() {
		return transferType;
	}

	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}

}
