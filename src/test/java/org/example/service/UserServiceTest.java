package org.example.service;

import org.example.dao.UserDao;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;

class UserServiceTest {

    private UserDao userDao;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDao = mock(UserDao.class);
        userService = new UserService(userDao);
    }

    @Test
    @DisplayName("createUser: должен вызывать dao.save с корректными полями")
    void testCreateUser_shouldCallSaveWithCorrectUser() {
        userService.createUser("John", "john@example.com", 30);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userDao, times(1)).save(captor.capture());

        User saved = captor.getValue();
        assertEquals("John", saved.getName());
        assertEquals("john@example.com", saved.getEmail());
        assertEquals(30, saved.getAge());
    }

    @Test
    @DisplayName("createUser: должен пробрасывать RuntimeException, если dao.save бросает исключение")
    void testCreateUser_shouldRethrowIfDaoFails() {
        doThrow(new RuntimeException("DB error")).when(userDao).save(any(User.class));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.createUser("Bad", "bad@example.com", 18));
        assertEquals("DB error", ex.getMessage());
        verify(userDao, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("getUserById: должен вернуть пользователя из dao")
    void testGetUserById_shouldReturnUserFromDao() {
        Long id = 1L;
        User expected = new User("Alice", "alice@example.com", 25);
        expected.setId(id);
        when(userDao.findById(id)).thenReturn(expected);

        User actual = userService.getUserById(id);

        verify(userDao, times(1)).findById(id);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getAllUsers: должен возвращать список из dao")
    void testGetAllUsers_shouldReturnListFromDao() {
        List<User> expected = List.of(
                new User("A", "a@ex.com", 20),
                new User("B", "b@ex.com", 21)
        );
        when(userDao.findAll()).thenReturn(expected);

        List<User> actual = userService.getAllUsers();

        verify(userDao, times(1)).findAll();
        assertEquals(expected, actual);
        assertEquals(2, actual.size());
    }

    @Test
    @DisplayName("updateUser: должен вызвать dao.update с переданным пользователем")
    void testUpdateUser_shouldCallDaoUpdate() {
        User user = new User("Tom", "tom@ex.com", 33);

        userService.updateUser(user);

        verify(userDao, times(1)).update(user);
    }

    @Test
    @DisplayName("updateUser: должен пробрасывать RuntimeException, если dao.update бросает исключение")
    void testUpdateUser_shouldRethrowOnDaoFailure() {
        User user = new User("Err", "err@ex.com", 40);
        doThrow(new RuntimeException("update failed")).when(userDao).update(user);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.updateUser(user));

        assertEquals("update failed", ex.getMessage());
        verify(userDao, times(1)).update(user);
    }

    @Test
    @DisplayName("deleteUserById: должен вызвать dao.deleteById с корректным id")
    void testDeleteUserById_shouldCallDaoDelete() {
        userService.deleteUserById(7L);

        verify(userDao, times(1)).deleteById(7L);
    }

    @Test
    @DisplayName("deleteUserById: должен пробрасывать RuntimeException, если dao.deleteById бросает исключение")
    void testDeleteUserById_shouldRethrowOnDaoFailure() {
        doThrow(new RuntimeException("delete failed")).when(userDao).deleteById(9L);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.deleteUserById(9L));

        assertEquals("delete failed", ex.getMessage());
        verify(userDao, times(1)).deleteById(9L);
    }
}
