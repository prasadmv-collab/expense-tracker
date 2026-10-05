package com.prasad.expensetracker.repository;

import com.prasad.expensetracker.entity.Income;
import com.prasad.expensetracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IncomeRepository extends JpaRepository<Income, Long> {

    List<Income> findByUser(User user);

    Optional<Income> findByIdAndUser(Long id, User user);

    @Query("""
            SELECT COALESCE(SUM(i.amount), 0)
            FROM Income i
            WHERE i.user = :user
            """)
    BigDecimal getTotalIncomeByUser(User user);

    @Query("""
            SELECT COALESCE(SUM(i.amount), 0)
            FROM Income i
            WHERE i.user = :user
            AND i.date BETWEEN :startDate AND :endDate
            """)
    BigDecimal getMonthlyIncome(
            @Param("user") User user,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
            SELECT FUNCTION('TO_CHAR', i.date, 'YYYY-MM'),
                   COALESCE(SUM(i.amount), 0)
            FROM Income i
            WHERE i.user = :user
            GROUP BY FUNCTION('TO_CHAR', i.date, 'YYYY-MM')
            ORDER BY FUNCTION('TO_CHAR', i.date, 'YYYY-MM')
            """)
    List<Object[]> getMonthlyIncomeHistory(User user);
}