package ru.schepachkov.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
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

    @OneToMany(mappedBy = "company")// mappedBy - используется для установки двунаправленной связи - ссылаемся на имя поля в классе User
    //@JoinColumn(name = "company_id") - используется для односторонней связи - ссылка на колонку, являющуюся fk в реляционной таблице.
    private Set<User> users;

}
