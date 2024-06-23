package ru.schepachkov;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.schepachkov.converter.BirthdayConverter;
import ru.schepachkov.entity.Birthday;
import ru.schepachkov.entity.Role;
import ru.schepachkov.entity.User;

import java.sql.SQLException;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) throws SQLException {
        Configuration configuration = new Configuration();
        configuration.addAttributeConverter(BirthdayConverter.class, true);
        configuration.registerTypeOverride(new JsonBinaryType());
        configuration.configure();


        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = User.builder()
                .userName("someMail9@gmail.com")
                .firstName("Ivan")
                .lastName("Ivanon")
                .birthDate(new Birthday(LocalDate.of(1998, 5, 20)))
                .info("{\"type\": \"string\",\"game\": \"ps3\"}")
                .role(Role.ADMIN)
                .build();
            session.save(user);
            session.getTransaction().commit();

            System.out.println("Закончил работу!");
        }

    }
}
