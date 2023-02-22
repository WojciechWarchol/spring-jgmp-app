package com.wojto.dao;

import com.wojto.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserDao {

    Page<User> getAllUser(Pageable pageable);

    User getUserById(long userId);

    User getUserByEmail(String email);

    Page<User> getUsersByName(String name, Pageable pageable);

    User createUser(User user);

    User updateUser(User user);

    boolean deleteUser(long userId);
}
