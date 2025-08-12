package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.dao.UserDao;
import org.example.dao.UserDaoImpl;
import org.example.model.User;

import java.util.List;

@Slf4j
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserService() {
        this.userDao = new UserDaoImpl();
    }

    public void createUser(String name, String email, int age) {
        log.info("Creating user with name={} email={} age={}", name, email, age);
        try {
            userDao.save(new User(name, email, age));
        } catch (RuntimeException e) {
            log.error("Failed to create user", e);
            throw e;
        }
    }

    public User getUserById(Long id) {
        log.info("Fetching user by ID {}", id);
        return userDao.findById(id);
    }

    public List<User> getAllUsers() {
        log.info("Fetching all users");
        return userDao.findAll();
    }

    public void updateUser(User user) {
        log.info("Updating user: {}", user);
        try {
            userDao.update(user);
        } catch (RuntimeException e) {
            log.error("Failed to update user", e);
            throw e;
        }
    }

    public void deleteUserById(Long id) {
        log.info("Deleting user by ID {}", id);
        try {
            userDao.deleteById(id);
        } catch (RuntimeException e) {
            log.error("Failed to delete user id {}", id, e);
            throw e;
        }
    }
}
