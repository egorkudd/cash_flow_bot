package com.ked.core.repositories;

import com.ked.core.entities.Category;
import com.ked.core.enums.ETransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByUserIdAndType(Long userId, ETransaction type);

    Optional<Category> findByUserIdAndCreatedAtIsNull(Long userId);

    List<Category> findAllByUserId(Long userId);
}
