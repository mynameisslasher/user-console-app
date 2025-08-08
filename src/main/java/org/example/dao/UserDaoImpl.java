package org.example.dao;

import lombok.extern.slf4j.Slf4j;
import org.example.model.User;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.PersistenceException;
import org.hibernate.HibernateException;

import java.util.List;

@Slf4j
public class UserDaoImpl implements UserDao {

    @Override
    public void save(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            log.info("Saving user: {}", user);
            session.persist(user);
            tx.commit();
            log.info("User saved with id: {}", user.getId());
        } catch (PersistenceException e) {
            if (tx != null) tx.rollback();
            log.error("Error saving user", e);
            throw new RuntimeException("DB error on save", e);
        }
    }

    @Override
    public User findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        } catch (HibernateException e) {
            log.error("Error finding user by id {}", id, e);
            throw new RuntimeException("DB error on findById", e);
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from User", User.class).list();
        } catch (HibernateException e) {
            log.error("Error finding all users", e);
            throw new RuntimeException("DB error on findAll", e);
        }
    }

    @Override
    public void update(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            log.info("Updating user: {}", user);
            session.merge(user);
            tx.commit();
            log.info("User updated: {}", user);
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            log.error("Error updating user", e);
            throw new RuntimeException("DB error on update", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                log.info("Deleting user: {}", user);
                session.remove(user);
            } else {
                log.warn("User with id {} not found, nothing to delete", id);
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            log.error("Error deleting user id {}", id, e);
            throw new RuntimeException("DB error on delete", e);
        }
    }
}
