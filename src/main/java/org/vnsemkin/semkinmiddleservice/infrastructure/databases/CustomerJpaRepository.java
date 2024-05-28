package org.vnsemkin.semkinmiddleservice.infrastructure.databases;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vnsemkin.semkinmiddleservice.infrastructure.entities.CustomerEntity;

import java.util.Optional;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findByEmail(String email);
    Optional<CustomerEntity> findByName(String name);
}

