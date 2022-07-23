package br.com.futechat.commons.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.futechat.commons.entity.PlayerEntity;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {
	
	List<PlayerEntity> findByNameAndTeamName(String playerName, String teamName);

}
