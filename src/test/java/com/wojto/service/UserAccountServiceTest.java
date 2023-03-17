package com.wojto.service;

import com.wojto.dao.DBUserAccountRepository;
import com.wojto.model.UserAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

    @InjectMocks
    private UserAccountService userAccountService;

    @Mock
    private DBUserAccountRepository dbUserAccountRepository;
    @Mock
    private List<UserAccount> userAccountListMock;
    @Mock
    private Page<UserAccount> userAccountPageMock;
    @Mock
    private UserAccount userAccountMock;
    @Mock
    private BigDecimal bigDecimalMock;

    @Test
    void getAllUserAccounts() {
        when(dbUserAccountRepository.findAll(any(Pageable.class))).thenReturn(userAccountPageMock);
        when(userAccountPageMock.getContent()).thenReturn(userAccountListMock);
        List<UserAccount> userAccountList = userAccountService.getAllUserAccounts(2, 0);
        assertNotNull(userAccountList);
    }

    @Test
    void getUserAccountById() {
        when(dbUserAccountRepository.findById(anyLong())).thenReturn(userAccountMock);
        UserAccount userAccount = userAccountService.getUserAccountById(1);
    }

    @Test
    void getUserAccountByUserId() {
        when(dbUserAccountRepository.findById(anyLong())).thenReturn(userAccountMock);
        UserAccount userAccount = userAccountService.getUserAccountByUserId(1);
    }

    @Test
    void createUserAccount() {
        when(dbUserAccountRepository.save(userAccountMock)).thenReturn(userAccountMock);
        UserAccount userAccount = userAccountService.createUserAccount(userAccountMock);
        assertEquals(userAccountMock, userAccount);
    }

    @Test
    void topUpUserAccount() {
        when(dbUserAccountRepository.findById(anyLong())).thenReturn(userAccountMock);
        when(userAccountMock.topUp(any(BigDecimal.class))).thenReturn(bigDecimalMock);
        when(dbUserAccountRepository.save(any(UserAccount.class))).thenReturn(userAccountMock);

        UserAccount userAccount = userAccountService.topUpUserAccount(1, BigDecimal.valueOf(100.00));
        assertEquals(userAccountMock, userAccount);
    }

    @Test
    void successfullyDeductFundsFromAccount() {
        when(dbUserAccountRepository.findById(anyLong())).thenReturn(userAccountMock);
        when(userAccountMock.deductFunds(any(BigDecimal.class))).thenReturn(true);

        boolean fundsDeducted = userAccountService.deductFundsFromAccount(1, BigDecimal.valueOf(100.00));
        assertTrue(fundsDeducted);
    }

    @Test
    void unsuccessfullyDeductFundsFromAccount() {
        when(dbUserAccountRepository.findById(anyLong())).thenReturn(userAccountMock);
        when(userAccountMock.deductFunds(any(BigDecimal.class))).thenReturn(false);

        boolean fundsDeducted = userAccountService.deductFundsFromAccount(1, BigDecimal.valueOf(500.00));
        assertFalse(fundsDeducted);
    }
}