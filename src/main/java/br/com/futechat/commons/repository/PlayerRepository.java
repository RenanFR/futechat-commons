package br.com.futechat.commons.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.futechat.commons.entity.PlayerEntity;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {
	
	List<PlayerEntity> findByTeamLeagueApiFootballId(Integer leagueId);
	
	Optional<PlayerEntity> findByNameAndTeamName(String playerName, String teamName);
	
}
