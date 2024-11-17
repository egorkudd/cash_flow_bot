package com.ked.core.repositories;

import com.ked.core.entities.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long> {
    Optional<Statistic> findByUserIdAndCreatedAtIsNull(Long userId);
}
