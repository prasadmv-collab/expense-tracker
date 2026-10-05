package com.prasad.expensetracker.repository;

import com.prasad.expensetracker.entity.Expense;
import com.prasad.expensetracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUser(User user);

    Optional<Expense> findByIdAndUser(Long id, User user);

    @Query("""
            SELECT COALESCE(SUM(e.amount), 0)
            FROM Expense e
            WHERE e.user = :user
            """)
    BigDecimal getTotalExpenseByUser(User user);

    @Query("""
            SELECT COALESCE(SUM(e.amount), 0)
            FROM Expense e
            WHERE e.user = :user
            AND e.date BETWEEN :startDate AND :endDate
            """)
    BigDecimal getMonthlyExpense(
            @Param("user") User user,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
            SELECT e.category.name, COALESCE(SUM(e.amount), 0)
            FROM Expense e
            WHERE e.user = :user
            GROUP BY e.category.name
            ORDER BY SUM(e.amount) DESC
            """)
    List<Object[]> getExpenseByCategory(User user);

    @Query("""
            SELECT FUNCTION('TO_CHAR', e.date, 'YYYY-MM'),
                   COALESCE(SUM(e.amount), 0)
            FROM Expense e
            WHERE e.user = :user
            GROUP BY FUNCTION('TO_CHAR', e.date, 'YYYY-MM')
            ORDER BY FUNCTION('TO_CHAR', e.date, 'YYYY-MM')
            """)
    List<Object[]> getMonthlyExpenseHistory(User user);
}