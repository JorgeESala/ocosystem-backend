package com.ocosur.ocosystem.livechicken.accounting.accounting_entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLRestriction;

import com.ocosur.ocosystem.livechicken.accounting.common.AccountingEntityType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    schema = "live_chicken",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_accounting_entity_type_id",
            columnNames = {"entity_type", "entity_id"}
        )
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("active = true")
public class AccountingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false, length = 30)
    private AccountingEntityType entityType;

    /**
     * ID of the real entity:
     * - branches.id when entityType = BRANCH
     * - suppliers.id when entityType = SUPPLIER
     */
    @Column(nullable = false)
    private Long entityId;

    /**
     * Snapshot name for reporting/debugging.
     */
    @Column(nullable = false, length = 50)
    private String displayName;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // -----------------------
    // JPA lifecycle
    // -----------------------

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // -----------------------
    // Constructor (domain)
    // -----------------------

    public AccountingEntity(
            AccountingEntityType entityType,
            Long entityId,
            String displayName
    ) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.displayName = displayName;
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}