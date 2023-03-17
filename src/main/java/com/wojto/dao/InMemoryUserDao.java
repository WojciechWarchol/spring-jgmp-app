package com.wojto.dao;

import com.wojto.model.User;
import com.wojto.storage.UserInMemoryStorage;
import com.wojto.storage.mappers.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InMemoryUserDao implements UserDao, InMemoryDao{

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryUserDao.class);

    @Autowired
    UserInMemoryStorage userInMemoryStorage;

    @Value("${dao.inmemory.user.contentfile}")
    private String fileName;
    private static final String[] PARAM_NAMES = new String[] { "id", "name", "email" };
    private static final Class<User> SUPPORTED_CLASS_TYPE = User.class;

    @Override
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String[] getParameterNames() {
        return PARAM_NAMES;
    }

    @Override
    public Class<User> getClassType() {
        return SUPPORTED_CLASS_TYPE;
    }

    @Override
    public FieldSetMapper getMapperForObjects() {
        return new UserMapper();
    }

    @Override
    public Page<User> getAllUser(Pageable pageable) {
        LOGGER.info("Calling in memory storage to retrieve all users.");
        List<User> allUsersList = userInMemoryStorage.getAllUsers();
        LOGGER.info("Retrieved list of all users: " + allUsersList );
        Page<User> page = convertListToPage(pageable, allUsersList);
        return page;
    }

    @Override
    public User getUserById(long userId) {
        LOGGER.info("Calling in memory storage for user with id: " + userId);
        User user = userInMemoryStorage.getUserById(userId);
        LOGGER.info("Retrieved user from in memory storage: " + user);
        return user;

    }

    @Override
    public User getUserByEmail(String email) {
        LOGGER.info("Calling in memory storage for user with email: " + email);
        User user = userInMemoryStorage.getUserByEmail(email);
        LOGGER.info("Retrieved user from in memory storage: " + user);
        return user;
    }

    @Override
    public Page<User> getUsersByName(String name, Pageable pageable) {
        LOGGER.info("Calling in memory storage for user containing \"" + name + "\" in name.");
        List<User> usersFoundByName = userInMemoryStorage.getUsersByName(name, pageable);
        LOGGER.info("Retrieved users from in memory storage: " + usersFoundByName);
        Page<User> page = convertListToPage(pageable, usersFoundByName);
        return page;
    }

    @Override
    public User createUser(User user) {
        return userInMemoryStorage.createOrUpdateUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userInMemoryStorage.createOrUpdateUser(user);
    }

    @Override
    public boolean deleteUser(long userId) {
        return userInMemoryStorage.deleteUser(userId);
    }

    private static Page<User> convertListToPage(Pageable pageable, List<User> listOfUsers) {
        LOGGER.debug("Converting user List to Page");
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), listOfUsers.size());
        Page<User> page = new PageImpl<User>(listOfUsers.subList(start, end), pageable, listOfUsers.size());
        LOGGER.debug("Created page of users: " + page);
        return page;
    }

    public UserInMemoryStorage getUserInMemoryStorage() {
        return userInMemoryStorage;
    }

    public void setUserInMemoryStorage(UserInMemoryStorage userInMemoryStorage) {
        this.userInMemoryStorage = userInMemoryStorage;
    }
}
