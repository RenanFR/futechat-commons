package br.com.futechat.commons.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.futechat.commons.entity.TransferEntity;

public interface TransferRepository extends JpaRepository<TransferEntity, Long> {

}
