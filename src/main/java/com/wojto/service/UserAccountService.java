package com.wojto.service;

import com.wojto.dao.DBUserAccountRepository;
import com.wojto.model.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAccountService.class);

    @Autowired
    @Qualifier("DBUserAccountRepository")
    DBUserAccountRepository userAccountRepository;

    public List<UserAccount> getAllUserAccounts(int pageSize, int pageNum) {
        LOGGER.info("Calling DBUserAccountRepository for all user accounts.");
        Page<UserAccount> page = userAccountRepository.findAll(PageRequest.of(pageNum, pageSize));
        return page.getContent();
    }

    public UserAccount getUserAccountById(long accountId) {
        LOGGER.info("Calling DBUserAccountRepository for account with id: " + accountId);
        return userAccountRepository.findById(accountId);
    }

    public UserAccount getUserAccountByUserId(long userId) {
        LOGGER.info("Calling DBUserAccountRepository for account with userId: " + userId);
        return userAccountRepository.findById(userId);
    }

    public UserAccount topUpUserAccount(long userId, BigDecimal amount) {
        LOGGER.info("Topping up account for user: " + userId + " in the ammount of: " + amount );
        UserAccount userAccount = getUserAccountByUserId(userId);
        BigDecimal fundsAfterTopUp = userAccount.topUp(amount);
        LOGGER.info("Funds of user: " + userId + " after top up: " + fundsAfterTopUp);
        return userAccountRepository.save(userAccount);
    }

    public boolean deductFundsFromAccount(long userId, BigDecimal amount) {
        LOGGER.info("Attempting to deduct payment for ticket from user account.");
        UserAccount userAccount = getUserAccountByUserId(userId);
        boolean fundsWereDeducted = userAccount.deductFunds(amount);
        if (fundsWereDeducted) {
            LOGGER.info("Funds successfully deducted! Remaining funds: " + userAccount.getFunds());
            userAccountRepository.save(userAccount);
            return true;
        }
        LOGGER.error("Funds could not be deducted. Probable reason: Insufficient funds. User funds: " + userAccount.getFunds() + " amount to deduct: " + amount);
        return false;
    }
}
