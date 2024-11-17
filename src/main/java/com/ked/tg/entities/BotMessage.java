package com.ked.tg.entities;

import com.ked.tg.enums.EBotMessage;
import com.ked.tg.enums.EChat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bot_message")
@Getter
@Setter
public class BotMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @Column(name = "writer_id")
    private Long writerId;

    @Enumerated(EnumType.STRING)
    private EBotMessage status;

    private EChat eChat;
}
