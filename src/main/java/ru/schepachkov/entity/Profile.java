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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    //@JoinColumn(name = "user_id") - общий случай, но не наш, если у нас fk является pk (@PrimaryKeyJoinColumn).
    @JoinColumn(name = "user_id")   // JoinColumn по умолчанию берет имя поля, над которым стоит и добавляет "id", разделяя их "_" => user_id
    private User user;

    private String street;
    private String language;

    public void setUser(User user) {
        user.setProfile(this);
        this.user = user;
        //this.id = user.getId();     // важное ограничение - Юзер должен быть сохранен к этому моменту. UPD: если наш pk является fk на другую таблицу
    }
}
