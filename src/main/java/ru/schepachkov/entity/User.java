package ru.schepachkov.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"company", "profile", "userChats"})// чтоб случайно lazy объект не дернуть. Дебаг в идее использует toString, например
@EqualsAndHashCode(exclude = {"company", "profile", "userChats"})
@Builder
@Entity
@Table(name = "users", schema = "public")
@TypeDef(name = "my-alias-for-jsonb", typeClass = JsonBinaryType.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String userName;

    @Embedded   // не обязательная аннотация, но полезна для наглядности
    private PersonalInfo personalInfo;

    @Enumerated(EnumType.STRING)
    private Role role;

    //@Type(type = "com.vladmihalcea.hibernate.type.json.JsonBinaryType")
    //@Type(type = "jsonb") // - грубо говоря алиас, чтобы не писать класс
    @Type(type = "my-alias-for-jsonb")
    private String info;

    // @ManyToOne или @ManyToOne(optional = true) - дефолт, который будет использовать outer join
    // @ManyToOne(optional = false) - заменит outer join на inner join, это лучше для производительности и используем, если в БД company_id содержит constraint not null
    @ManyToOne(optional = false)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile profile;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<UserChat> userChats = new ArrayList<>();

}
