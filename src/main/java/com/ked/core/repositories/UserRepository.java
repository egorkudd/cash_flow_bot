package com.ked.core.repositories;

import com.ked.core.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByTgId(Long tgId);

    boolean existsByTgId(Long tgId);
}
