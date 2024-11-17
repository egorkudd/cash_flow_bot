package com.ked.tg.entities;

import com.ked.tg.enums.EBotMessage;
import com.ked.tg.enums.EChat;
import jakarta.persistence.*;
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
