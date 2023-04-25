package com.wojto.controller.rest;

import com.wojto.facade.BookingFacade;
import com.wojto.model.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/userAccounts")
public class UserAccountRestController {

    private static Logger LOGGER = LoggerFactory.getLogger(UserAccountRestController.class);

    @Autowired
    BookingFacade bookingFacade;

    @GetMapping("/byUserId")
    @ResponseStatus(HttpStatus.OK)
    UserAccount getUserAccountByUserId(@RequestParam("userId") long userId) {
        LOGGER.debug("UserAccountRestController.getUserAccountByUserId() method called");
        UserAccount userAccount = bookingFacade.getUserAccountByUserId(userId);

        return userAccount;
    }

    @PostMapping("/topUp")
    @ResponseStatus(HttpStatus.OK)
    UserAccount topUp(@RequestParam("userId") long userId, @RequestParam("amount") BigDecimal amount) {
        LOGGER.debug("UserAccountRestController.topUp() method called");
        UserAccount userAccount = bookingFacade.topUpUserAccount(userId, amount);

        return userAccount;
    }
}
