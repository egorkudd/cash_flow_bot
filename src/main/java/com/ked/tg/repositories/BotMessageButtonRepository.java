package com.ked.tg.repositories;

import com.ked.tg.entities.BotMessageButton;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BotMessageButtonRepository extends JpaRepository<BotMessageButton, Long> {
    List<BotMessageButton> findAllByBotMessageId(long botMessageId);

    @Modifying
    @Transactional
    void deleteAllByBotMessageId(long botMessageId);
}
