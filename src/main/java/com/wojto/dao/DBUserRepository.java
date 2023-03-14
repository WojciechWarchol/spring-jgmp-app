package com.wojto.dao;

import com.wojto.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DBUserRepository extends JpaRepository<User, Long>, UserDao {

    /**
     * Methods from the original interface
     */
    @Override
    default Page<User> getAllUser(Pageable pageable) {
        return findAll(pageable);
    }

    @Override
    default User getUserById(long userId) {
        return findById(userId);
    }

    @Override
    default User getUserByEmail(String email) {
        return findByEmail(email);
    }

    @Override
    default Page<User> getUsersByName(String name, Pageable pageable) {
        return findByNameContaining(name, pageable);
    }

    @Override
    default User createUser(User user) {
        return save(user);
    }

    @Override
    default User updateUser(User user) {
        return save(user);
    }

    @Override
    default boolean deleteUser(long userId) {
        if (existsById(userId)) {
            deleteById(userId);
            return true;
        }
        return false;
    }

    /**
     * Methods from JpaRepository interface
     */

    List<User> findAll();

    Page<User> findAll(Pageable pageable);

    User findById(long id);

    User findByEmail(String email);

    Page<User> findByNameContaining(String name, Pageable pageable);

    User save(User user);

    void deleteById(long id);
}
