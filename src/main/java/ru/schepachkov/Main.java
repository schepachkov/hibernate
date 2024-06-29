package ru.schepachkov;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.schepachkov.converter.BirthdayConverter;
import ru.schepachkov.entity.Birthday;
import ru.schepachkov.entity.Role;
import ru.schepachkov.entity.User;

import java.sql.SQLException;
import java.time.LocalDate;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws SQLException {
        LOG.info("main method starts!");
        Configuration configuration = new Configuration();
        configuration.addAttributeConverter(BirthdayConverter.class, true);
        configuration.registerTypeOverride(new JsonBinaryType());
        configuration.configure();


        try (SessionFactory sessionFactory = configuration.buildSessionFactory(); Session session = sessionFactory.openSession()) {
            //simpleSaveWithoutTransaction(session);
            LOG.debug("Session is created!");
            simpleSaveWithInternalTransaction(session);
            User user = createFullFilledUser();

            session.beginTransaction();
            session.saveOrUpdate(user);
            session.getTransaction().commit();
            LOG.info("User saved in main method. User - {}", user);

            User userFromHibernate = findUserById(session, user.getUserName());
            LOG.debug("Session is closed!");
        }

        LOG.info("main method finished!");
    }

    private static void simpleSaveWithoutTransaction(Session session) {
        // не будет работать тк Хибер требуется наличия транзакции при модификации сущностей
        LOG.trace("Method 'simpleSaveWithoutTransaction' called.");
        String pk = "1";
        User user = User.builder()
            .userName(pk)
            .firstName("Ivan25")
            .lastName("Ivan")
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
            .firstName("Internal transaction user")
            .lastName("kek")
            .build();
        session.saveOrUpdate(user);
        session.getTransaction().commit();
    }

    private static User createFullFilledUser() {
        LOG.trace("Method 'createFullFilledUser' called.");
        String pk = "someMail9@gmail.com";
        return User.builder()
            .userName(pk)
            .firstName("Ivan25")
            .lastName("Ivan")
            .birthDate(new Birthday(LocalDate.of(1998, 5, 20)))
            .info("{\"type\": \"string\",\"game\": \"ps3\"}")
            .role(Role.ADMIN)
            .build();
    }

    private static User findUserById(Session session, String pk) {
        LOG.trace("Method 'findUserById' called.");
        return session.get(User.class, pk);
    }
}
