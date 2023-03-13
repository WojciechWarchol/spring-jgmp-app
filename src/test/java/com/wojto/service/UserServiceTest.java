package com.wojto.service;

import com.wojto.dao.DBUserRepository;
import com.wojto.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private DBUserRepository dbUserRepository;
    @Mock
    private List<User> userListMock;
    @Mock
    private Page<User> userPageMock;
    @Mock
    private User userMock;


    @Test
    void getAllUser() {
        when(dbUserRepository.getAllUser(any())).thenReturn(userPageMock);
        when(userPageMock.getContent()).thenReturn(userListMock);
        List<User> userList = userService.getAllUser(2, 4);
        assertNotNull(userList);
    }

    @Test
    void getUserById() {
        when(dbUserRepository.getUserById(anyLong())).thenReturn(userMock);
        User user = userService.getUserById(1);
        assertNotNull(user);
    }

    @Test
    void getUserByEmail() {
        when(dbUserRepository.getUserByEmail(anyString())).thenReturn(userMock);
        User user = userService.getUserByEmail("lol@gmail.com");
        assertNotNull(user);
    }

    @Test
    void getUsersByName() {
        when(dbUserRepository.getUsersByName(anyString(), any())).thenReturn(userPageMock);
        when(userPageMock.getContent()).thenReturn(userListMock);
        List<User> userList = userService.getUsersByName("Andrzej", 3, 3);
        assertNotNull(userList);
    }

    @Test
    void createUser() {
        when(dbUserRepository.createUser(userMock)).thenReturn(userMock);
        User user = userService.createUser(userMock);
        assertEquals(userMock, user);
    }

    @Test
    void updateUser() {
        when(dbUserRepository.updateUser(userMock)).thenReturn(userMock);
        User user = userService.updateUser(userMock);
        assertEquals(userMock, user);
    }

    @Test
    void deleteUser() {
        when(dbUserRepository.deleteUser(anyLong())).thenReturn(true);
        boolean userDeleted = userService.deleteUser(5);
        assertTrue(userDeleted);
    }
}