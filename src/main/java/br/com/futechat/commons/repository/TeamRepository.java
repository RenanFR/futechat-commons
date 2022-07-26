package br.com.futechat.commons.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.futechat.commons.entity.TeamEntity;

public interface TeamRepository extends JpaRepository<TeamEntity, Long> {
	
	Optional<TeamEntity> findByNameAndCountry(String teamName, String countryName);
	
	Optional<TeamEntity> findByNameAndApiFootballId(String teamName, Integer apiFootballId);
	
	Optional<TeamEntity> findByName(String teamName);

}
