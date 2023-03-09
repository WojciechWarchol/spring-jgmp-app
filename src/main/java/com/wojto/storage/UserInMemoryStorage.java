package com.wojto.storage;

import com.wojto.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserInMemoryStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserInMemoryStorage.class);

    private Map<Long, User> userMap = new HashMap<>();

    public UserInMemoryStorage() {
    }

    public List<User> getAllUsers() {
        LOGGER.info("Getting all users, current number: " + userMap.values().size());
        return new ArrayList<>(userMap.values());
    }

    public User getUserById(long userId) {
        LOGGER.info("Retrieving user with id: " + userId + " from in memory storage.");
        return userMap.get(userId);
    }

    public User getUserByEmail(String email) {
        LOGGER.info("Retrieving user with email: " + email + " from in memory storage.");
        return userMap.values().stream().filter(u -> u.getEmail().equals(email)).findFirst().get();
    }

    public List<User> getUsersByName(String name, Pageable pageable) {
        LOGGER.info("Retrieving users containing name: " + name + " from in memory storage.");
        return userMap.values().stream().filter(u -> u.getName().contains(name)).collect(Collectors.toList());
    }

    public User createOrUpdateUser(User user) {
        LOGGER.info("Adding user to in memory db: " + user);
        userMap.put(user.getId(), user);
        return user;
    }

    public boolean deleteUser(long userId) {
        LOGGER.info("deleting user with id: " + userId);
        return userMap.remove(userId) != null;
    }
}
