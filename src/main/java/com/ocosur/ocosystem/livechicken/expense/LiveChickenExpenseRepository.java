package com.ocosur.ocosystem.livechicken.expense;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ocosur.ocosystem.livechicken.expense.dto.ExpenseFlatRowDTO;

public interface LiveChickenExpenseRepository extends JpaRepository<Expense, Long> {
    Page<Expense> findAllByOrderByIdDesc(Pageable pageable);

    List<Expense> findByDateBetweenOrderByDateDesc(LocalDate start, LocalDate end);

    List<Expense> findByDateBetween(LocalDate start, LocalDate end);

    @Query("""
                SELECT new com.ocosur.ocosystem.livechicken.expense.dto.ExpenseFlatRowDTO(
                    e.id,
                    e.categoryCode,
                    c.name,
                    c.expenseType,
                    e.reason,
                    e.amount,
                    e.date,

                    fe.cedis.id,
                    fe.cedis.name,
                    fe.weight,

                    ve.vehicle.id,
                    ve.vehicle.name,
                    ve.employee.id,
                    ve.employee.name,
                    ve.category,

                    fu.route.id,
                    fu.route.name
                )
                FROM LiveChickenExpense e
                JOIN LiveChickenExpenseCategory c ON c.code = e.categoryCode

                LEFT JOIN e.foodExpense fe
                LEFT JOIN e.vehicleExpense ve
                LEFT JOIN e.fuelExpense fu

                WHERE e.date BETWEEN :start AND :end
                ORDER BY e.date DESC
            """)
    List<ExpenseFlatRowDTO> findFlatBetween(
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);

    @Query(value = """
            SELECT new com.ocosur.ocosystem.livechicken.expense.dto.ExpenseFlatRowDTO(
                e.id,
                e.categoryCode,
                c.name,
                c.expenseType,
                e.reason,
                e.amount,
                e.date,

                fe.cedis.id,
                fe.cedis.name,
                fe.weight,

                ve.vehicle.id,
                ve.vehicle.name,
                ve.employee.id,
                ve.employee.name,
                ve.category,

                fu.route.id,
                fu.route.name
            )
            FROM LiveChickenExpense e
            JOIN LiveChickenExpenseCategory c ON c.code = e.categoryCode
            LEFT JOIN e.foodExpense fe
            LEFT JOIN e.vehicleExpense ve
            LEFT JOIN e.fuelExpense fu
            ORDER BY e.date DESC
            """, countQuery = """
            SELECT COUNT(e)
            FROM LiveChickenExpense e
            """)
    Page<ExpenseFlatRowDTO> findLatestFlat(Pageable pageable);

    Page<Expense> findAllByOrderByDateDesc(Pageable pageable);

    List<Expense> findAllByDateBetweenOrderByDateDesc(
            LocalDate start,
            LocalDate end);
}
