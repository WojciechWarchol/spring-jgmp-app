package com.wojto.dao;

import com.wojto.model.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DBUserAccountRepository extends JpaRepository<UserAccount, Long> {

    List<UserAccount> findAll();

    Page<UserAccount> findAll(Pageable pageable);

    UserAccount findById(long accountId);

    UserAccount findByUserId(long userId);

    UserAccount save(UserAccount userAccount);

    void deleteById(long accountId);
}
