package com.wojto.controller;

import com.wojto.facade.BookingFacade;
import com.wojto.model.Event;
import com.wojto.model.User;
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
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    BookingFacade bookingFacade;

    @GetMapping("/{userId}")
    String getUserById(@RequestParam("userId") long userId, Model model) {
        LOGGER.debug("userController.getUserById() method called");
        User user = bookingFacade.getUserById(userId);
        List<User> userList = Arrays.asList(user);
        model.addAttribute("userList", userList);
        return "showUsers";
    }

    @GetMapping("/byEmail")
    String getUserByEmail(@RequestParam("email") String email, Model model) {
        LOGGER.debug("UserController.getUserByEmail() method called");
        User user = bookingFacade.getUserByEmail(email);
        List<User> userList = Arrays.asList(user);
        model.addAttribute("userList", userList);
        return "showUsers";
    }

    @GetMapping("/byName")
    String getUsersForDay(@RequestParam("name") String name, Model model) throws ParseException {
        LOGGER.debug("UserController.getUsersByName() method called");
        List<User> userList = bookingFacade.getUsersByName(name, 10, 0);
        model.addAttribute("userList", userList);
        return "showUsers";
    }

    @GetMapping("/createUserForm")
    String goToCreateUserForm() {
        LOGGER.debug("UserController.goToCreateUserForm() method called");
        return "createUser";
    }

    @PostMapping("/createUser")
    String createUser(@RequestParam("email") String email, @RequestParam("name") String name) throws ParseException {
        LOGGER.debug("UserController.createUser() method called");
        User user = bookingFacade.createUser(
                new User(email, name));
        return "index";
    }

    @GetMapping("/updateUserForm")
    public String goToEditUserForm(@RequestParam("userId") Long userId, Model model) {
        LOGGER.debug("UserController.goToEditUserForm() method called");
        User user = bookingFacade.getUserById(userId);
        model.addAttribute("user", user);
        return "createUser";
    }

    @PostMapping("/updateUser")
    String updateUser(@RequestParam("id") long id,
                       @RequestParam("email") String email,
                       @RequestParam("name") String name) throws ParseException {
        LOGGER.debug("UserController.updateUser() method called");
        User user = bookingFacade.getUserById(id);

        user.setEmail(email);
        user.setName(name);

        bookingFacade.updateUser(user);

        return "index";
    }

    @PostMapping("/deleteUser")
    String deleteUser(@RequestParam("userId") long userId) {
        LOGGER.debug("UserController.deleteUser() method called");
        boolean userDeleted = bookingFacade.deleteUser(userId);
        // TODO Probably attach a "successful delete to the model
        return "index";
    }

}
