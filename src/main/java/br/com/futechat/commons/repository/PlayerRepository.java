package br.com.futechat.commons.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.futechat.commons.entity.PlayerEntity;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {
	
	@Query("SELECT player FROM PlayerEntity player " +
            "JOIN FETCH player.transfers transfers "
            + " WHERE player.name = :playerName AND player.team.name = :teamName")
	List<PlayerEntity> findByNameAndTeamName(String playerName, String teamName);

}
