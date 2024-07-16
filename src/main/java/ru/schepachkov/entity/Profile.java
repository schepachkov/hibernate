package ru.schepachkov.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
@Builder
public class Profile {

    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne
    //@JoinColumn(name = "user_id") - общий случай, но не наш, тк у нас fk является pk.
    @PrimaryKeyJoinColumn
    private User user;

    private String street;
    private String language;

    public void setUser(User user) {
        user.setProfile(this);
        this.user = user;
        this.id = user.getId();     // важное ограничение - Юзер должен быть сохранен к этому моменту
    }
}
