package br.com.futechat.commons.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.futechat.commons.entity.LeagueEntity;

@Repository
public interface LeagueRepository extends JpaRepository<LeagueEntity, Long> {
	
	Optional<LeagueEntity> findByApiFootballId(Integer apiFootballId);
	
	Optional<LeagueEntity> findByName(String leagueName);
	
	Optional<LeagueEntity> findByNameAndCountry(String teamName, String countryName);
}
