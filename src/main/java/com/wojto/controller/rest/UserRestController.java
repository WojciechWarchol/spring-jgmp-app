package com.wojto.controller.rest;

import com.wojto.facade.BookingFacade;
import com.wojto.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private static Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);

    @Autowired
    BookingFacade bookingFacade;

    @GetMapping("/byId")
    @ResponseStatus(HttpStatus.OK)
    List<User> getUserById(@RequestParam("userId") long userId) {
        LOGGER.debug("UserRestController.getUserById() method called");
        List<User> userList = new ArrayList<>();
        User user = bookingFacade.getUserById(userId);

        if (user != null) userList.add(user);

        return userList;
    }

    @GetMapping("/byEmail")
    @ResponseStatus(HttpStatus.OK)
    List<User> getUserByEmail(@RequestParam("email") String email) {
        LOGGER.debug("UserRestController.getUserByEmail() method called");
        List<User> userList = new ArrayList<>();
        User user = bookingFacade.getUserByEmail(email);
        List<User> foundUsers = Arrays.asList(user);

        if (foundUsers != null) userList.addAll(foundUsers);

        return userList;
    }

    @GetMapping("/byName")
    @ResponseStatus(HttpStatus.OK)
    List<User> getUsersByName(@RequestParam("name") String name) {
        LOGGER.debug("UserRestController.getUsersByName() method called");
        List<User> userList = new ArrayList<>();
        List<User> foundUsers = bookingFacade.getUsersByName(name, 10, 0);

        if (foundUsers != null) userList.addAll(foundUsers);

        return userList;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    User createUser(@RequestBody User user) {
        LOGGER.debug("UserRestController.createUser() method called");
        User createdUser = bookingFacade.createUser(user);
        return createdUser;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    User updateUser(@RequestBody User user) {
        LOGGER.debug("UserRestController.updateUser() method called");
        User createdUser = bookingFacade.updateUser(user);
        return createdUser;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<Void> deleteUser(@RequestParam("userId") long userId) {
        LOGGER.debug("UserRestController.deleteUser() method called");
        boolean userDeleted = bookingFacade.deleteUser(userId);
        if (userDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
