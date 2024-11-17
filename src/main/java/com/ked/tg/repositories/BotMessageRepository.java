package com.ked.tg.repositories;

import com.ked.tg.entities.BotMessage;
import com.ked.tg.enums.EBotMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BotMessageRepository extends JpaRepository<BotMessage, Long> {
    Optional<BotMessage> findByWriterIdAndStatus(long writerId, EBotMessage status);
}
