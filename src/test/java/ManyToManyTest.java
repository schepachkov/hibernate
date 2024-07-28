import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import ru.schepachkov.entity.*;
import ru.schepachkov.util.HibernateUtil;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class ManyToManyTest {


    // для удаления из коллекции можно использовать .clear() или .remove(Object o)
    // для удаления сущности так же проблем нет - делаем через session.remove(Object o);
    // cascade не используем, легко можно грохнуть много всего лишнего
    @Test
    public void manyToManyTest() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory(); Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = createUser();
            Chat chat = Chat.builder()
                .name("chat-name" + UUID.randomUUID().toString().substring(0, 8))
                .build();
            session.save(user.getCompany());        // компанию тоже только создали, поэтому сохраняем в первую очередь
            session.save(user);                     // сохраняем, что б не получить TransientPropertyValueException. Если б брали из БД, то это не нужно.
            session.save(chat);

            UserChat userChat = UserChat.builder()
                .createdAt(Instant.now())
                .createdBy(user.getUserName())
                .build();

            // добавляем ссылки
            userChat.setUser(user);
            user.getUserChats().add(userChat);      // если сущность доставалась из Хибера, то get Коллекцию генерил бы запрос в БД на поиск => outer/inner join
            // добавляем ссылки
            userChat.setChat(chat);
            chat.getUserChats().add(userChat);

            session.save(userChat);
            session.getTransaction().commit();
        }
    }


    private User createUser() {
        String userName = "many-to-may-check@gmail.com" + UUID.randomUUID().toString().substring(0, 8);
        return User.builder()
            .userName(userName)
            .personalInfo(PersonalInfo.builder()
                .firstName("many-to-many")
                .lastName("many-to-many")
                .birthDate(new Birthday(LocalDate.of(1998, 5, 20)))
                .build())
            .info("{\"type\": \"string\",\"game\": \"ps3\"}")
            .role(Role.ADMIN)
            .company(Company.builder()
                .name("Some-company-for-MANY2MANY" + UUID.randomUUID().toString().substring(0, 8))
                .build())
            .build();
    }
}
