package ru.schepachkov;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.schepachkov.converter.BirthdayConverter;
import ru.schepachkov.entity.*;
import ru.schepachkov.util.HibernateUtil;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws SQLException {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory(); Session session = sessionFactory.openSession()) {
            User user = createFullFilledUser();
            session.beginTransaction();

            //session.saveOrUpdate(user.getCompany());
            session.saveOrUpdate(user);
            User user1 = session.get(User.class, 3L);


            session.getTransaction().commit();
            LOG.info("User saved in main method. User - {}", user);
            LOG.debug("Session is closed!");
        }

        LOG.info("main method finished!");
    }

    private static void simpleSaveWithoutTransaction(Session session) {
        // не будет работать тк Хибер требуется наличия транзакции при модификации сущностей
        LOG.trace("Method 'simpleSaveWithoutTransaction' called.");
        String userName = "1";
        User user = User.builder()
            .userName(userName)
            .personalInfo(PersonalInfo.builder()
                .firstName("Ivan25")
                .lastName("Ivan")
                .build())
            .build();
        session.saveOrUpdate(user);
        session.flush();
    }

    private static void simpleSaveWithInternalTransaction(Session session) {
        LOG.trace("Method 'simpleSaveWithInternalTransaction' called.");
        session.getTransaction().begin();
        String pk = "1";
        User user = User.builder()
            .userName(pk)
            .personalInfo(PersonalInfo.builder()
                .firstName("Internal transaction user")
                .lastName("kek")
                .build())
            .build();
        session.saveOrUpdate(user);
        session.getTransaction().commit();
    }

    private static User createFullFilledUser() {
        LOG.trace("Method 'createFullFilledUser' called.");
        String userName = "kek@gmail.com" + UUID.randomUUID().toString().substring(0, 8);
        return User.builder()
            .userName(userName)
            .personalInfo(PersonalInfo.builder()
                .firstName("Petr1")
                .lastName("Petrovich")
                .birthDate(new Birthday(LocalDate.of(1998, 5, 20)))
                .build())
            .info("{\"type\": \"string\",\"game\": \"ps3\"}")
            .role(Role.ADMIN)
            .company(Company.builder()
                .name("Some-company" + UUID.randomUUID().toString().substring(0, 8))
                .build())
            .build();
    }

    private static User findUserById(Session session, Long pk) {
        LOG.trace("Method 'findUserById' called.");
        return session.get(User.class, pk);
    }
}
