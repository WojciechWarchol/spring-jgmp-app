package com.wojto.dao;

import com.wojto.model.User;
import com.wojto.storage.UserInMemoryStorage;
import com.wojto.storage.mappers.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class InMemoryUserDao implements UserDao, InMemoryDao{

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryUserDao.class);

    UserInMemoryStorage userInMemoryStorage;

    private static String fileName;
    private static final String[] PARAM_NAMES = new String[] { "id", "name", "email" };
    private static final Class<User> SUPPORTED_CLASS_TYPE = User.class;

    @Override
    public String getFileName() {
        return fileName;
    }

    public static void setFileName(String fileName) {
        InMemoryUserDao.fileName = fileName;
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
        List<User> allUsersList = userInMemoryStorage.getAllUsers();
        Page<User> page = convertListToPage(pageable, allUsersList);
        return page;
    }

    @Override
    public User getUserById(long userId) {
        return userInMemoryStorage.getUserById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        return userInMemoryStorage.getUserByEmail(email);
    }

    @Override
    public Page<User> getUsersByName(String name, Pageable pageable) {
        List<User> userList = userInMemoryStorage.getUsersByName(name, pageable);
        Page<User> page = convertListToPage(pageable, userList);
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
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), listOfUsers.size());
        Page<User> page = new PageImpl<User>(listOfUsers.subList(start, end), pageable, listOfUsers.size());
        return page;
    }

    public UserInMemoryStorage getUserInMemoryStorage() {
        return userInMemoryStorage;
    }

    public void setUserInMemoryStorage(UserInMemoryStorage userInMemoryStorage) {
        this.userInMemoryStorage = userInMemoryStorage;
    }
}
