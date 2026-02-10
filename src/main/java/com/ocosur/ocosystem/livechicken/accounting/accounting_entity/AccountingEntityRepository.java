package com.ocosur.ocosystem.livechicken.accounting.accounting_entity;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ocosur.ocosystem.livechicken.accounting.common.AccountingEntityType;

import java.util.List;
import java.util.Optional;

public interface AccountingEntityRepository
                extends JpaRepository<AccountingEntity, Long> {

        Optional<AccountingEntity> findByEntityTypeAndEntityId(
                        AccountingEntityType entityType,
                        Long entityId);

        Optional<AccountingEntity> findByEntityTypeAndId(
                        AccountingEntityType entityType,
                        Long id);

        List<AccountingEntity> findAllByOrderByEntityTypeAsc();
        List<AccountingEntity> findAllByOrderByEntityTypeDesc();
}
