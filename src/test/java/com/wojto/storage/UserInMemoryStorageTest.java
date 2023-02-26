package com.wojto.storage;

import com.wojto.model.Event;
import com.wojto.model.User;
import com.wojto.model.UserImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserInMemoryStorageTest {

    private UserInMemoryStorage userInMemoryStorage;
    private User user1;
    private User user2;
    private User user3;
    private Pageable pageable = PageRequest.of(0, 10);

    {
        user1 = new UserImpl(1, "Jozef Malolepszy", "jozef.malolepszy@gmail.com");
        user2 = new UserImpl(2, "Jan Nowak", "j.nowak@gmail.com");
        user3 = new UserImpl(3, "Adam Mickiewicz", "a.mickiewicz@gmail.com");
    }

    @BeforeEach
    void setUp() {
        userInMemoryStorage = new UserInMemoryStorage();
        userInMemoryStorage.createOrUpdateUser(user1);
        userInMemoryStorage.createOrUpdateUser(user2);
        userInMemoryStorage.createOrUpdateUser(user3);
    }

    @Test
    void getAllUsers() {
        int numberOfElements = userInMemoryStorage.getAllUsers().size();
        assertEquals(3, numberOfElements);
    }

    @Test
    void getUserById() {
        User user = userInMemoryStorage.getUserById(1);
        assertEquals(user1, user);
    }

    @Test
    void getUserByEmail() {
        User user = userInMemoryStorage.getUserByEmail("j.nowak@gmail.com");
        assertEquals(user2, user);
    }

    @Test
    void getUsersByName() {
        List<User> userList = userInMemoryStorage.getUsersByName("J", pageable);
        assertEquals(2, userList.size());
    }

    @Test
    void createOrUpdateUser() {
        User newUser = userInMemoryStorage.createOrUpdateUser(new UserImpl(4, "New User", "n.n@gmail.com"));
        assertEquals(4, userInMemoryStorage.getAllUsers().size());
        assertEquals(newUser, userInMemoryStorage.getUserById(4));
    }

    @Test
    void deleteUser() {
        boolean userDeleted = userInMemoryStorage.deleteUser(1);
        List<User> foundUsers = userInMemoryStorage.getAllUsers();
        assertTrue(userDeleted);
        assertEquals(2, foundUsers.size());
        assertFalse(foundUsers.contains(user1));
    }

    @Test
    void noUserDeletedWhenProvidedIdDoesntExist() {
        boolean userDeleted = userInMemoryStorage.deleteUser(5);
        List<User> foundUsers = userInMemoryStorage.getAllUsers();
        assertFalse(userDeleted);
        assertEquals(3, foundUsers.size());
    }
}