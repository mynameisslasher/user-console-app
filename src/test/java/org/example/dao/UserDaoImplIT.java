package org.example.dao;

import org.example.model.User;
import org.example.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDaoImplIT {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    static UserDao userDao;

    @BeforeAll
    static void init() {
        Properties p = new Properties();
        p.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        p.setProperty("hibernate.connection.username", postgres.getUsername());
        p.setProperty("hibernate.connection.password", postgres.getPassword());
        p.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        p.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

        p.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        p.setProperty("hibernate.show_sql", "false");

        HibernateUtil.rebuildSessionFactory(p);

        userDao = new UserDaoImpl();
    }

    @AfterAll
    static void tearDown() {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        if (sf != null && !sf.isClosed()) sf.close();
    }

    @Test
    @Order(1)
    void save_and_findById_shouldWork() {
        User u = new User("Alice", "alice@example.com", 25);
        userDao.save(u);
        assertNotNull(u.getId(), "ID должен быть проставлен БД после persist()");

        User fromDb = userDao.findById(u.getId());
        assertNotNull(fromDb);
        assertEquals("Alice", fromDb.getName());
        assertEquals("alice@example.com", fromDb.getEmail());
        assertEquals(25, fromDb.getAge());
    }

    @Test
    @Order(2)
    void findAll_shouldReturnList() {
        userDao.save(new User("Bob", "bob@example.com", 30));
        List<User> all = userDao.findAll();
        assertTrue(all.size() >= 1);
    }

    @Test
    @Order(3)
    void update_shouldChangeFields() {
        User u = new User("Tom", "tom@ex.com", 33);
        userDao.save(u);

        u.setName("Tommy");
        u.setEmail("tommy@ex.com");
        userDao.update(u);

        User updated = userDao.findById(u.getId());
        assertEquals("Tommy", updated.getName());
        assertEquals("tommy@ex.com", updated.getEmail());
    }

    @Test
    @Order(4)
    void deleteById_shouldRemoveEntity() {
        User u = new User("Del", "del@ex.com", 20);
        userDao.save(u);
        Long id = u.getId();

        userDao.deleteById(id);

        assertNull(userDao.findById(id));
    }
}
