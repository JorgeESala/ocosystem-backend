package com.ocosur.ocosystem.livechicken.accounting.read;

import java.sql.Types;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ocosur.ocosystem.livechicken.accounting.read.dto.AccountsPayableAgingDTO;
import com.ocosur.ocosystem.livechicken.accounting.read.dto.AccountsPayableHistoryDTO;
import com.ocosur.ocosystem.livechicken.accounting.read.dto.AccountsPayableOpenDTO;
import com.ocosur.ocosystem.livechicken.accounting.read.dto.EntityAccountStatementDTO;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccountsPayableReadRepository {

        private final JdbcTemplate jdbc;
        private final NamedParameterJdbcTemplate namedJdbcTemplate;

        public List<AccountsPayableOpenDTO> findOpen(Long debtorId, Long creditorId) {

                String sql = """
                                    SELECT accounts_payable_id,
                                           debtor_id,
                                           debtor_name,
                                           creditor_id,
                                           creditor_name,
                                           total_amount,
                                           balance,
                                           created_at
                                    FROM v_accounts_payable_open
                                    WHERE (:debtorId IS NULL OR debtor_id = :debtorId)
                                      AND (:creditorId IS NULL OR creditor_id = :creditorId)
                                    ORDER BY created_at
                                """;

                MapSqlParameterSource params = new MapSqlParameterSource()
                                .addValue("debtorId", debtorId, Types.BIGINT)
                                .addValue("creditorId", creditorId, Types.BIGINT);

                return namedJdbcTemplate.query(sql, params, (rs, i) -> new AccountsPayableOpenDTO(
                                rs.getLong("accounts_payable_id"),
                                rs.getLong("debtor_id"),
                                rs.getString("debtor_name"),
                                rs.getLong("creditor_id"),
                                rs.getString("creditor_name"),
                                rs.getBigDecimal("total_amount"),
                                rs.getBigDecimal("balance"),
                                rs.getTimestamp("created_at").toLocalDateTime()));

        }

        public List<AccountsPayableHistoryDTO> findHistory(Long accountsPayableId) {

                String sql = """
                                    SELECT movement_id,
                                           movement_type,
                                           amount,
                                           balance_before,
                                           balance_after,
                                           created_at,
                                           compensation_folio,
                                           note
                                    FROM v_accounts_payable_history
                                    WHERE accounts_payable_id = ?
                                    ORDER BY created_at
                                """;

                return jdbc.query(sql, (rs, i) -> new AccountsPayableHistoryDTO(
                                rs.getLong("movement_id"),
                                rs.getString("movement_type"),
                                rs.getBigDecimal("amount"),
                                rs.getBigDecimal("balance_before"),
                                rs.getBigDecimal("balance_after"),
                                rs.getDate("created_at").toLocalDate(),
                                rs.getString("compensation_folio"),
                                rs.getString("note")),
                                accountsPayableId);
        }

        public List<AccountsPayableAgingDTO> findAging(Long debtorId, Long creditorId) {

                String sql = """
                                    SELECT accounts_payable_id,
                                           debtor_id,
                                           creditor_id,
                                           balance,
                                           aging_bucket
                                    FROM v_accounts_payable_aging
                                    WHERE (:debtorId IS NULL OR debtor_id = :debtorId)
                                      AND (:creditorId IS NULL OR creditor_id = :creditorId)
                                """;

                Map<String, Object> params = Map.of(
                                "debtorId", debtorId,
                                "creditorId", creditorId);

                return new NamedParameterJdbcTemplate(jdbc)
                                .query(sql, params, (rs, i) -> new AccountsPayableAgingDTO(
                                                rs.getLong("accounts_payable_id"),
                                                rs.getLong("debtor_id"),
                                                rs.getLong("creditor_id"),
                                                rs.getBigDecimal("balance"),
                                                rs.getString("aging_bucket")));
        }

        public List<EntityAccountStatementDTO> findEntityAccountStatement(
                        Long debtorId,
                        Long creditorId,
                        LocalDate from,
                        LocalDate to) {

                String sql = """
                                    SELECT
                                        debtor_entity_id,
                                        creditor_entity_id,
                                        movement_date,
                                        movement_type,
                                        balance_before,
                                        amount,
                                        balance_after,
                                        folio,
                                        note
                                    FROM v_entity_account_statement
                                    WHERE (:debtorId IS NULL OR debtor_entity_id = :debtorId)
                                      AND (:creditorId IS NULL OR creditor_entity_id = :creditorId)
                                      AND (:from IS NULL OR movement_date >= :from)
                                      AND (:to IS NULL OR movement_date <= :to)
                                    ORDER BY movement_date
                                """;

                

                MapSqlParameterSource params = new MapSqlParameterSource()
                                .addValue("debtorId", debtorId, Types.BIGINT)
                                .addValue("creditorId", creditorId, Types.BIGINT)
                                .addValue("from", from, Types.DATE)
                                .addValue("to", to, Types.DATE);

                return new NamedParameterJdbcTemplate(jdbc)
                                .query(sql, params, (rs, i) -> new EntityAccountStatementDTO(
                                                rs.getLong("debtor_entity_id"),
                                                rs.getLong("creditor_entity_id"),
                                                rs.getDate("movement_date").toLocalDate(),
                                                rs.getString("movement_type"),
                                                rs.getBigDecimal("balance_before"),
                                                rs.getBigDecimal("amount"),
                                                rs.getBigDecimal("balance_after"),
                                                rs.getString("folio"),
                                                rs.getString("note")));
        }

}
