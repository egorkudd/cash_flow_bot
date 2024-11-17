package com.ked.tg.repositories;

import com.ked.tg.entities.BotUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BotUserRepository extends JpaRepository<BotUser, Long> {
    boolean existsByTgId(long tgId);

    Optional<BotUser> findByTgId(long tgId);
}
