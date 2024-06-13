package org.vnsemkin.semkinmiddleservice.infrastructure.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "transactions")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "account_id", nullable = false)
    private long accountId;
    @Column(name = "customer_id", nullable = false)
    private long customerId;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(name = "new_balance", nullable = false)
    private BigDecimal newBalance;
    @Column(name = "transaction_uuid", nullable = false)
    private String transactionUuid;

    @PrePersist
    protected void onCreate() {
        timestamp = new Date();
    }
}
