package ru.schepachkov.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users_chat")
public class UserChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)    // оптимизация для замены outer join на left join
    @JoinColumn(name = "user_id")   // можно не указывать тк оно собирается "имя java поля" + "_id"
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    private Instant createdAt;

    private String createdBy;

}
