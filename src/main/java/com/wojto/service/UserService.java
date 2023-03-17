package com.wojto.service;

import com.wojto.dao.UserDao;
import com.wojto.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    @Qualifier("DBUserRepository")
    UserDao userDao;

    public List<User> getAllUser(int pageSize, int pageNum) {
        LOGGER.info("Calling UserDao for all users.");
        Page<User> page = userDao.getAllUser(PageRequest.of(pageNum, pageSize));
        return page.getContent();
    }

    @Cacheable("userCache")
    public User getUserById(long userId) {
        LOGGER.info("Calling UserDao for user with id: " + userId);
        return userDao.getUserById(userId);
    }

    public User getUserByEmail(String email) {
        LOGGER.info("Calling UserDao for user with email: " + email);
        return userDao.getUserByEmail(email);
    }

    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        LOGGER.info("Calling UserDao for users containing \"" + name + "\" in name");
        Page<User> page = userDao.getUsersByName(name, PageRequest.of(pageNum, pageSize));
        return page.getContent();
    }

    public User createUser(User user) {
        LOGGER.info("Calling UserDao to create user: " + user);
        return userDao.createUser(user);
    }

    public User updateUser(User user) {
        LOGGER.info("Calling UserDao to update user: " + user);
        return userDao.updateUser(user);
    }

    public boolean deleteUser(long userId) {
        LOGGER.info("Calling UserDao to delete user with id: " + userId);
        return userDao.deleteUser(userId);
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
