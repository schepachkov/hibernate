package ru.schepachkov.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "users")
@EqualsAndHashCode(exclude = "users")
@Builder
@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Builder.Default    // для инициализации в билдере дефолтного значения new HashSet<>();
    @OneToMany(mappedBy = "company", orphanRemoval = true)// mappedBy - используется для установки двунаправленной связи - ссылаемся на имя поля в классе User
    //@JoinColumn(name = "company_id") - используется для односторонней связи - ссылка на колонку, являющуюся fk в реляционной таблице.
    private Set<User> users = new HashSet<>();

    public void addUser(User user) {
        users.add(user);
        user.setCompany(this);
    }

}
