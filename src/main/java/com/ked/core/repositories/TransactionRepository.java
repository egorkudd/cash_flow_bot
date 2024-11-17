package com.ked.core.repositories;

import com.ked.core.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByUserIdAndCreatedAtIsNull(Long userId);

    List<Transaction> findByUserIdAndCreatedAt(Long userId, Instant dateTime);
    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId AND t.createdAt BETWEEN :startDate AND :endDate")
    List<Transaction> findByUserIdAndCreatedAtBetween(
            @Param("userId") Long userId,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate);

    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId " +
            "AND MONTH(t.createdAt) = MONTH(:date) AND YEAR(t.createdAt) = YEAR(:date)")
    List<Transaction> findByUserIdAndMonth(
            @Param("userId") Long userId,
            @Param("date") Instant date);

    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId " +
            "AND YEAR(t.createdAt) = YEAR(:date)")
    List<Transaction> findByUserIdAndYear(
            @Param("userId") Long userId,
            @Param("date") Instant date);
}
