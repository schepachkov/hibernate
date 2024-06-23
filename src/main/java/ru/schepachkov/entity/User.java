package ru.schepachkov.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.schepachkov.converter.BirthdayConverter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users", schema = "public")
@TypeDef(name = "my-alias-for-jsonb", typeClass = JsonBinaryType.class)
public class User {

    @Id
    @Column(name = "username")
    private String userName;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "birthdate")
    //@Convert(converter = BirthdayConverter.class)
    private Birthday birthDate;

    @Enumerated(EnumType.STRING)
    private Role role;

    //@Type(type = "com.vladmihalcea.hibernate.type.json.JsonBinaryType")
    //@Type(type = "jsonb") // - грубо говоря алиас, чтобы не писать класс
    @Type(type = "my-alias-for-jsonb")
    private String info;
}
