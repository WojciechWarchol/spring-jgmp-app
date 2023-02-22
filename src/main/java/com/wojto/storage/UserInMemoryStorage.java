package com.wojto.storage;

import com.wojto.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserInMemoryStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserInMemoryStorage.class);

    private Map<Long, User> userMap = new HashMap<>();

    public UserInMemoryStorage() {
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    public User getUserById(long id) {
        return userMap.get(id);
    }

    public User getUserByEmail(String email) {
        return userMap.values().stream().filter(u -> u.getEmail().equals(email)).findFirst().get();
    }

    public List<User> getUsersByName(String name, Pageable pageable) {
        return userMap.values().stream().filter(u -> u.getName().contains(name)).collect(Collectors.toList());
    }

    public User createOrUpdateUser(User user) {
        userMap.put(user.getId(), user);
        return user;
    }

    public boolean deleteUser(long userId) {
        return userMap.remove(userId).getId() == userId;
    }
}
