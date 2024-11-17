package com.ked.tg.services;

import com.ked.tg.entities.GroupChat;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;

import java.util.List;

@Service
public interface GroupChatService {
    void save(ChatMemberUpdated chatMemberUpdated);

    void delete(ChatMemberUpdated chatMemberUpdated);

    List<GroupChat> findAll();

    boolean isBotNewChatMember(ChatMemberUpdated chatMemberUpdated);

    boolean isBotKickedChatMember(ChatMemberUpdated chatMemberUpdated);
}
