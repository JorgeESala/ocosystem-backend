package com.ocosur.ocosystem.livechicken.expense;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiveChickenExpenseRepository extends JpaRepository<LiveChickenExpense, Long> {
    Page<LiveChickenExpense> findAllByOrderByIdDesc(Pageable pageable);
}
