package com.ked.tg.repositories;

import com.ked.tg.entities.GroupChat;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupChatRepository extends JpaRepository<GroupChat, Long> {
    @Modifying
    @Transactional
    void deleteByChatId(long chatId);
}
