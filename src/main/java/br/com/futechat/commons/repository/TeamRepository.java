package br.com.futechat.commons.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.futechat.commons.entity.TeamEntity;

public interface TeamRepository extends JpaRepository<TeamEntity, Long> {

}
