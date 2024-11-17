package com.ked.tg.services.impl;

import com.ked.tg.entities.GroupChat;
import com.ked.tg.mappers.GroupChatMapper;
import com.ked.tg.repositories.GroupChatRepository;
import com.ked.tg.services.BotUserService;
import com.ked.tg.services.GroupChatService;
import com.ked.tg.utils.LogUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupChatServiceImpl implements GroupChatService {
    private final GroupChatRepository groupChatRepository;
    private final GroupChatMapper groupChatMapper;
    private final BotUserService botUserService;

    @Override
    public void save(ChatMemberUpdated chatMemberUpdated) {
        long groupChatId = chatMemberUpdated.getChat().getId();
        long addedByUserId = chatMemberUpdated.getFrom().getId();

        if (botUserService.existsByTgId(addedByUserId)) {
            GroupChat groupChat = groupChatMapper.groupChat(groupChatId, addedByUserId);
            groupChatRepository.saveAndFlush(groupChat);
        }

        log.info(LogUtil.getAddToGroupChatLog(groupChatId, addedByUserId));
    }

    @Override
    public void delete(ChatMemberUpdated chatMemberUpdated) {
        long groupChatId = chatMemberUpdated.getChat().getId();
        groupChatRepository.deleteByChatId(groupChatId);

        log.info(LogUtil.getKickFromGroupChatLog(groupChatId));
    }

    @Override
    public List<GroupChat> findAll() {
        return groupChatRepository.findAll();
    }

    @Override
    public boolean isBotNewChatMember(ChatMemberUpdated chatMemberUpdated) {
        String oldStatus = chatMemberUpdated.getOldChatMember().getStatus();
        boolean notExistsBefore = "left".equals(oldStatus) || "kicked".equals(oldStatus);
        String newStatus = chatMemberUpdated.getNewChatMember().getStatus();
        boolean existsNow = "member".equals(newStatus) || "administrator".equals(newStatus);
        return notExistsBefore && existsNow;
    }

    @Override
    public boolean isBotKickedChatMember(ChatMemberUpdated chatMemberUpdated) {
        String newStatus = chatMemberUpdated.getNewChatMember().getStatus();
        boolean notExists = "left".equals(newStatus);
        boolean kicked = "kicked".equals(newStatus);
        return notExists || kicked;
    }
}
