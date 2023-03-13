package com.wojto.dao;

import com.wojto.model.User;
import com.wojto.storage.UserInMemoryStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InMemoryUserDaoTest {

    @InjectMocks
    private InMemoryUserDao inMemoryUserDao;

    @Mock
    private UserInMemoryStorage userInMemoryStorageMock;

    private Pageable pageable1 = PageRequest.of(0, 5);
    private Pageable pageable2 = PageRequest.of(1, 2);
    private List<User> userTestList;
    private User testUser;


    @BeforeEach
    void setUp() {
        testUser = new User(4, "Test User", "test.user@gmail.com");
        userTestList = new ArrayList<>();
        userTestList.add(new User(1, "Jozef Malolepszy", "jozef.malolepszy@gmail.com"));
        userTestList.add(new User(2, "Jan Nowak", "j.nowak@gmail.com"));
        userTestList.add(new User(3, "Adam Mickiewicz", "a.mickiewicz@gmail.com"));
    }

    @Test
    void getAllUserOnBigPage() {
        when(userInMemoryStorageMock.getAllUsers()).thenReturn(userTestList);
        Page<User> userPage = inMemoryUserDao.getAllUser(pageable1);
        assertEquals(userTestList, userPage.getContent());
    }

    @Test
    void getAllUsersOnSmallPage() {
        when(userInMemoryStorageMock.getAllUsers()).thenReturn(userTestList);
        Page<User> userPage = inMemoryUserDao.getAllUser(pageable2);
        assertEquals(1, userPage.getContent().size());
        assertEquals(userTestList.get(2), userPage.getContent().get(0));
    }

    @Test
    void getUserById() {
        when(userInMemoryStorageMock.getUserById(anyLong())).thenReturn(testUser);
        User user = inMemoryUserDao.getUserById(4);
        assertEquals(testUser, user);
    }

    @Test
    void getUserByEmail() {
        when(userInMemoryStorageMock.getUserByEmail(anyString())).thenReturn(testUser);
        User user = inMemoryUserDao.getUserByEmail("lol@gmail.com");
        assertEquals(testUser, user);
    }

    @Test
    void getUsersByName() {
        when(userInMemoryStorageMock.getUsersByName(anyString(), any(Pageable.class))).thenReturn(userTestList);
        Page<User> userPage = inMemoryUserDao.getUsersByName("A", pageable1);
        assertEquals(userTestList, userPage.getContent());
    }

    @Test
    void createUser() {
        when(userInMemoryStorageMock.createOrUpdateUser(any(User.class))).thenReturn(testUser);
        User user = inMemoryUserDao.createUser(testUser);
        assertEquals(testUser, user);
    }

    @Test
    void updateUser() {
        when(userInMemoryStorageMock.createOrUpdateUser(any(User.class))).thenReturn(testUser);
        User user = inMemoryUserDao.updateUser(testUser);
        assertEquals(testUser, user);
    }

    @Test
    void deleteUser() {
        when(userInMemoryStorageMock.deleteUser(anyLong())).thenReturn(true);
        boolean userDeleted = inMemoryUserDao.deleteUser(6);
        assertTrue(userDeleted);
    }
}