package org.example.util;

import org.example.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.util.Properties;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    private HibernateUtil() {
        throw new UnsupportedOperationException("Utility class");
    }


    static {
        try {
            Configuration configuration = new Configuration();

            Properties properties = new Properties();
            properties.load(HibernateUtil.class.getClassLoader().getResourceAsStream("hibernate.properties"));
            configuration.setProperties(properties);

            configuration.addAnnotatedClass(User.class);
            System.out.println("✅ User.class добавлен в конфигурацию");

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (IOException e) {
            System.err.println("Ошибка чтения hibernate.properties: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ошибка инициализации SessionFactory: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    // Для тестов
    public static synchronized void rebuildSessionFactory(Properties override) {
        shutdown();
        try {
            Configuration configuration = new Configuration();

            Properties props = new Properties();
            props.load(HibernateUtil.class.getClassLoader().getResourceAsStream("hibernate.properties"));

            if (override != null) {
                props.putAll(override);
            }

            configuration.setProperties(props);
            configuration.addAnnotatedClass(User.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception e) {
            throw new RuntimeException("Failed to rebuild SessionFactory for tests", e);
        }
    }

    public static synchronized void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
        sessionFactory = null;
    }

}
