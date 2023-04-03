package com.wojto.controller;

import com.wojto.facade.BookingFacade;
import com.wojto.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    BookingFacade bookingFacade;

    @GetMapping("/byId")
    @ResponseStatus(HttpStatus.OK)
    String getUserById(@RequestParam("userId") long userId, Model model) {
        LOGGER.debug("userController.getUserById() method called");
        List<User> userList = new ArrayList<>();
        User user = bookingFacade.getUserById(userId);

        if (user != null) userList.add(user);
        model.addAttribute("userList", userList);

        return "showUsers";
    }

    @GetMapping("/byEmail")
    @ResponseStatus(HttpStatus.OK)
    String getUserByEmail(@RequestParam("email") String email, Model model) {
        LOGGER.debug("UserController.getUserByEmail() method called");
        List<User> userList = new ArrayList<>();
        User user = bookingFacade.getUserByEmail(email);
        List<User> foundUsers = Arrays.asList(user);

        if (foundUsers != null) userList.addAll(foundUsers);
        model.addAttribute("userList", userList);

        return "showUsers";
    }

    @GetMapping("/byName")
    @ResponseStatus(HttpStatus.OK)
    String getUsersByName(@RequestParam("name") String name, Model model) throws ParseException {
        LOGGER.debug("UserController.getUsersByName() method called");
        List<User> userList = new ArrayList<>();
        List<User> foundUsers = bookingFacade.getUsersByName(name, 10, 0);

        if (foundUsers != null) userList.addAll(foundUsers);
        model.addAttribute("userList", userList);

        return "showUsers";
    }

    @GetMapping("/createUserForm")
    String goToCreateUserForm() {
        LOGGER.debug("UserController.goToCreateUserForm() method called");
        return "createUser";
    }

    @PostMapping("/createUser")
    @ResponseStatus(HttpStatus.CREATED)
    String createUser(@RequestParam("name") String name, @RequestParam("email") String email) throws ParseException {
        LOGGER.debug("UserController.createUser() method called");
        User user = bookingFacade.createUser(
                new User(name, email));
        return "index";
    }

    @GetMapping("/updateUserForm")
    public String goToEditUserForm(@RequestParam("userId") Long userId, Model model) {
        LOGGER.debug("UserController.goToEditUserForm() method called");
        User user = bookingFacade.getUserById(userId);
        model.addAttribute("user", user);
        return "createUser";
    }

    @PutMapping("/updateUser")
    @ResponseStatus(HttpStatus.CREATED)
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

    @DeleteMapping("/deleteUser")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    String deleteUser(@RequestParam("userId") long userId) {
        LOGGER.debug("UserController.deleteUser() method called");
        boolean userDeleted = bookingFacade.deleteUser(userId);
        // TODO Probably attach a "successful delete to the model
        return "index";
    }

}
