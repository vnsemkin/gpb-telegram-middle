package org.vnsemkin.semkinmiddleservice.infrastructure.databases;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vnsemkin.semkinmiddleservice.infrastructure.entities.TransactionEntity;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, Long> {
}