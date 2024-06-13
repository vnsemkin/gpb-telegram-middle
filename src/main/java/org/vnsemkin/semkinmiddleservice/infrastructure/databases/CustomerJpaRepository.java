package org.vnsemkin.semkinmiddleservice.infrastructure.databases;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.vnsemkin.semkinmiddleservice.infrastructure.entities.CustomerEntity;

import java.util.Optional;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findByTgId(long tgId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<CustomerEntity> findByUsername(String username);
}