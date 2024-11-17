package com.ked.tg.repositories;

import com.ked.tg.entities.TgChat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TgChatRepository extends CrudRepository<TgChat, Long> {
    Optional<TgChat> findByChatId(long chatId);
}
