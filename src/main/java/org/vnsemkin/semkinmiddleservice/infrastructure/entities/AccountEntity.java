package org.vnsemkin.semkinmiddleservice.infrastructure.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "accounts")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "account_id", unique = true)
    private String accountId;
    @Column(name = "account_name", nullable = false)
    private String accountName;
    @Column(nullable = false)
    private BigDecimal  balance;
    @OneToOne(mappedBy = "account")
    private CustomerEntity customer;
}