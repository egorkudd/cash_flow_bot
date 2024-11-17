package com.ked.tg.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bot_message_button")
@Getter
@Setter
public class BotMessageButton {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "button_name")
    private String buttonName;

    @Column(name = "button_link")
    private String buttonLink;

    @Column(name = "bot_message_id")
    private Long botMessageId;
}
