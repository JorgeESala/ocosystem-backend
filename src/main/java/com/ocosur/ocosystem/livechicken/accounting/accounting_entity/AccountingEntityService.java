package com.ocosur.ocosystem.livechicken.accounting.accounting_entity;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocosur.ocosystem.livechicken.accounting.accounting_entity.dto.AccountingEntityResponseDTO;
import com.ocosur.ocosystem.livechicken.accounting.common.AccountingEntityType;

@Service
public class AccountingEntityService {

        private final AccountingEntityRepository repository;

        public AccountingEntityService(AccountingEntityRepository repository) {
                this.repository = repository;
        }

        // --------------------------------------------------
        // Branch
        // --------------------------------------------------

        @Transactional
        public AccountingEntity getOrCreateForBranch(
                        Long branchId,
                        String branchName) {
                return repository
                                .findByEntityTypeAndId(
                                                AccountingEntityType.BRANCH,
                                                branchId)
                                .orElseGet(() -> repository.save(
                                                new AccountingEntity(
                                                                AccountingEntityType.BRANCH,
                                                                branchId,
                                                                branchName)));
        }

        // --------------------------------------------------
        // Supplier
        // --------------------------------------------------

        @Transactional
        public AccountingEntity getOrCreateForSupplier(
                        Long supplierId,
                        String supplierName) {
                return repository
                                .findByEntityTypeAndId(
                                                AccountingEntityType.SUPPLIER,
                                                supplierId)
                                .orElseGet(() -> repository.save(
                                                new AccountingEntity(
                                                                AccountingEntityType.SUPPLIER,
                                                                supplierId,
                                                                supplierName)));
        }

        // --------------------------------------------------
        // Generic
        // --------------------------------------------------

        @Transactional
        public AccountingEntity getOrCreate(
                        AccountingEntityType type,
                        Long entityId,
                        String displayName) {
                return repository
                                .findByEntityTypeAndId(type, entityId)
                                .orElseGet(() -> repository.save(
                                                new AccountingEntity(
                                                                type,
                                                                entityId,
                                                                displayName)));
        }

        public AccountingEntity getByTypeAndEntityId(
                        AccountingEntityType type,
                        Long entityId) {
                return repository
                                .findByEntityTypeAndId(type, entityId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "AccountingEntity not found for " +
                                                                type + " with id " + entityId));
        }

        @Transactional
        public AccountingEntity create(
                        AccountingEntityType type,
                        Long entityId,
                        String displayName) {
                if (repository.findByEntityTypeAndId(type, entityId).isPresent()) {
                        throw new IllegalStateException(
                                        "AccountingEntity already exists");
                }

                return repository.save(
                                new AccountingEntity(type, entityId, displayName));
        }

        // --------------------------------------------------
        // Read
        // --------------------------------------------------

        public AccountingEntity getById(Long id) {
                return repository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "AccountingEntity not found: " + id));
        }

        public List<AccountingEntityResponseDTO> getAll() {
                return repository.findAllByOrderByEntityTypeDesc().stream()
                                .map(entity -> {
                                        AccountingEntityResponseDTO dto = new AccountingEntityResponseDTO();
                                        dto.setId(entity.getId());
                                        dto.setName(entity.getDisplayName());
                                        dto.setEntityType(entity.getEntityType());
                                        dto.setEntityId(entity.getEntityId());
                                        return dto;
                                })
                                .toList();
        }
}
