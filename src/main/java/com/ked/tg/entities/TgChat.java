package com.ked.tg.entities;

import com.ked.interaction.enums.EConversation;
import com.ked.interaction.enums.EConversationStep;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "tg_chat")
@Getter
@Setter
public class TgChat implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EConversation eConversation;

    @Enumerated(EnumType.STRING)
    private EConversationStep eConversationStep;

    private Integer prevBotMessageId;

    private Integer prevBotMessagePageNumber;

    private Long chatId;

    public void setDefaultPrevBotMessagePageNumber() {
        prevBotMessagePageNumber = 0;
    }

}
