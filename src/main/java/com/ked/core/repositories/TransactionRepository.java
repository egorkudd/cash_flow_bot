package com.ked.core.repositories;

import com.ked.core.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByUserIdAndCreatedAtIsNull(Long userId);

    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId AND t.createdAt BETWEEN :startDate AND :endDate")
    List<Transaction> findByUserIdAndCreatedAtBetween(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId " +
            "AND t.createdAt >= :startOfMonth AND t.createdAt < :endOfMonth")
    List<Transaction> findByUserIdAndMonth(
            @Param("userId") Long userId,
            @Param("startOfMonth") LocalDateTime startOfMonth,
            @Param("endOfMonth") LocalDateTime endOfMonth);

    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId " +
            "AND t.createdAt >= :startOfYear AND t.createdAt < :endOfYear")
    List<Transaction> findByUserIdAndYear(
            @Param("userId") Long userId,
            @Param("startOfYear") LocalDateTime startOfYear,
            @Param("endOfYear") LocalDateTime endOfYear);
}
