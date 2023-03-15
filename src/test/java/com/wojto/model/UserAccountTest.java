package com.wojto.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UserAccountTest {

    private UserAccount userAccount;

    @BeforeEach
    void setUp() {
        userAccount = new UserAccount(1, 1);
    }

    @Test
    void userAccountHasZeroFundsOnCreation() {
        BigDecimal initialFunds = userAccount.getFunds();
        assertEquals(BigDecimal.ZERO.setScale(2), initialFunds);
    }

    @Test
    void topUpOnce() {
        BigDecimal singleTopUp = BigDecimal.TEN.setScale(2);
        BigDecimal valueReturnedAfterTopUpMethod = userAccount.topUp(singleTopUp);

        assertEquals(singleTopUp, valueReturnedAfterTopUpMethod);
        assertEquals(singleTopUp, userAccount.getFunds());
    }

    @Test
    void topUpsSumUp() {
        BigDecimal singleTopUp = BigDecimal.TEN.setScale(2);
        BigDecimal ammountAfterTwoTopUps = BigDecimal.valueOf(20.00).setScale(2);
        userAccount.topUp(singleTopUp);
        userAccount.topUp(singleTopUp);

        assertEquals(ammountAfterTwoTopUps, userAccount.getFunds());
    }

    @Test
    void deductFundsSuccessfully() {
        BigDecimal expectedAmountAfterDeduction = BigDecimal.valueOf(5.50).setScale(2);
        userAccount.topUp(BigDecimal.TEN);
        boolean deductionSuccessful = userAccount.deductFunds(BigDecimal.valueOf(4.50));

        assertEquals(expectedAmountAfterDeduction, userAccount.getFunds());
        assertTrue(deductionSuccessful);
    }

    @Test
    void deductFundsFailed() {
        BigDecimal expectedAmountAfterDeduction = BigDecimal.valueOf(21.00).setScale(2);
        userAccount.topUp(expectedAmountAfterDeduction);
        boolean deductionSuccessful = userAccount.deductFunds(BigDecimal.valueOf(30.00));

        assertEquals(expectedAmountAfterDeduction, userAccount.getFunds());
        assertFalse(deductionSuccessful);
    }
}