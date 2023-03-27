package com.wojto.controller;

import com.wojto.facade.BookingFacade;
import com.wojto.model.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
@RequestMapping("/userAccounts")
public class UserAccountController {

    private static Logger LOGGER = LoggerFactory.getLogger(UserAccountController.class);

    @Autowired
    BookingFacade bookingFacade;

    @GetMapping("/getUserAccountByUserId")
    String getUserAccountByUserId(@RequestParam("userId") long userId, Model model) {
        LOGGER.debug("userAccountController.getUserAccountByUserId() method called");
        UserAccount userAccount = bookingFacade.getUserAccountByUserId(userId);
        model.addAttribute("userAccount", userAccount);
        return "showUserAccount";
    }

    @PostMapping("/topUp")
    String topUp(@RequestParam("userId") long userId, @RequestParam("amount") BigDecimal amount, Model model) {
        LOGGER.debug("userAcconuntController.topUp() method called");
        UserAccount userAccount = bookingFacade.topUpUserAccount(userId, amount);
        model.addAttribute("userAccount", userAccount);
        return "showUserAccount";
    }

}
