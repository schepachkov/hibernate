import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.schepachkov.entity.Company;
import ru.schepachkov.entity.Profile;
import ru.schepachkov.entity.User;
import ru.schepachkov.util.HibernateUtil;

import java.util.UUID;

@Slf4j
public class MainTest {

    @Test
    public void checkOneToOne_PkIsFkInProfileTableCase() {
        // тест для ситуации, когда pk юзера является одновременно и внешним ключом в таблице профилей и первичным у нее же
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory(); Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Company company = Company.builder()
                .name("kek company" + UUID.randomUUID().toString().substring(0, 5))
                .build();
            User user = User.builder()
                .userName(UUID.randomUUID().toString().substring(0, 8))
                .build();
            company.addUser(user);
            Profile profile = Profile.builder()
                .language("ru")
                .street("kek street")
                .build();
            session.save(company);
            profile.setUser(user);      // такая запись говнище, суть в том, что сделали user.setProfile(this); и как следсвтие подтянули профиль при комите
            session.getTransaction().commit();
        }
    }

    @Test
    public void checkOneToOne_CommonCase() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory(); Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Company company = Company.builder()
                .name("kek company" + UUID.randomUUID().toString().substring(0, 5))
                .build();
            User user = User.builder()
                .userName(UUID.randomUUID().toString().substring(0, 8))
                .build();

            // exchange keys USER - COMPANY
            company.getUsers().add(user);
            user.setCompany(company);


            Profile profile = Profile.builder()
                .language("ru")
                .street("kek street")
                .build();

            // exchange keys USER - PROFILE
            user.setProfile(profile);
            profile.setUser(user);

            session.save(company);
            session.getTransaction().commit();
            // ИТОГО: если не накинуть связи в обе стороны то будем выхватывать ошибки.
            // Видимо хиберу это необходимо, чтобы понять, что эта пачка взаимосвязана, в одну сторону не прокатит
            // company.getUsers().add(user); + user.setProfile(profile);
        }
    }

    @Test
    public void testCascadeDelete() {
        // цель - запуск метода позволяет заметить разницу между использованием каскада на уровне Хибера и БД
        // Результат
        // - CascadeType.ALL - на уровне хибера на каждого Юзера создается отдельный delete запрос
        // - CascadeType.PERSIST - не сохраняет автоматически юзеров - нужно сохранять вручную
        // - без CascadeType - по идее должен в базе 1 запросом стирать все сущности, но для проверки нужно настроить лог лвл на БД, чет сходу не вышло


        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory(); Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Company company = Company.builder()
                .name("for-delete-company" + UUID.randomUUID().toString().substring(0, 8))
                .build();

            company.addUser(User.builder()
                    .userName(UUID.randomUUID().toString().substring(0, 8))
                .build());
            company.addUser(User.builder()
                .userName(UUID.randomUUID().toString().substring(0, 8))
                .build());
            company.addUser(User.builder()
                .userName(UUID.randomUUID().toString().substring(0, 8))
                .build());
            company.addUser(User.builder()
                .userName(UUID.randomUUID().toString().substring(0, 8))
                .build());
            session.save(company);
            company.getUsers().forEach(session::save);
            session.getTransaction().commit();

            session.beginTransaction();
            session.delete(company);
            session.getTransaction().commit();
        }
    }
}
