package com.wojto.controller;

import com.wojto.EventAppConfig;
import com.wojto.model.User;
import net.sf.ehcache.CacheManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {EventAppConfig.class})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration(value = "/src/main/resources")
class UserControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private static final String SHOW_USER_VIEW_NAME = "showUsers";
    private static final String INDEX_VIEW_NAME = "index";
    private static final String USER_LIST_ATTRIBUTE = "userList";
    private static final String USER_ACCOUNT_ATTRIBUTE = "userAccount";

    @BeforeEach
    void setUp() {
        ApplicationContext context = new AnnotationConfigApplicationContext(EventAppConfig.class);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @AfterEach
    void cleanup() {
        CacheManager.getInstance().getCache("userCache").removeAll();
    }

    @Test
    void testGetUserById() throws Exception {
        long userId = 1L;
        User user = new User(userId, "Jozef Malolepszy", "jozef.malolepszy@gmail.com");

        mockMvc.perform(get("/users/byId")
                        .param("userId", Long.toString(userId)))
                .andExpect(status().isOk())
                .andExpect(view().name(SHOW_USER_VIEW_NAME))
                .andExpect(model().attributeExists(USER_LIST_ATTRIBUTE))
                .andExpect(model().attribute(USER_LIST_ATTRIBUTE, hasSize(1)))
                .andExpect(model().attribute(USER_LIST_ATTRIBUTE, contains(user)));
    }

    @Test
    void testGetUserByEmail() throws Exception {
        String email = "grzegorz.b@gmail.com";

        mockMvc.perform(get("/users/byEmail")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(view().name(SHOW_USER_VIEW_NAME))
                .andExpect(model().attributeExists(USER_LIST_ATTRIBUTE))
                .andExpect(model().attribute(USER_LIST_ATTRIBUTE, hasSize(1)))
                .andExpect(model().attribute(USER_LIST_ATTRIBUTE, hasItem(
                        allOf(
                                hasProperty("email", is(email))
                        )
                )));
    }

    @Test
    void testGetUsersByName() throws Exception {
        String name = "Jan";

        mockMvc.perform(get("/users/byName")
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(view().name(SHOW_USER_VIEW_NAME))
                .andExpect(model().attributeExists(USER_LIST_ATTRIBUTE))
                .andExpect(model().attribute(USER_LIST_ATTRIBUTE, hasSize(2)))
                .andExpect(model().attribute(USER_LIST_ATTRIBUTE, hasItem(
                        allOf(
                                hasProperty("name", is("Jan Nowak"))
                        )
                )))
                .andExpect(model().attribute(USER_LIST_ATTRIBUTE, hasItem(
                        allOf(
                                hasProperty("name", is("Janusz Nosacz"))
                        )
                )));
    }

    @Test
    void testCreateUser() throws Exception {
        long userId = 7L;
        String name = "Pan Nowy";
        String email = "test@test.com";
        User user = new User(userId, name, email);
        BigDecimal funds = BigDecimal.valueOf(0.00).setScale(2);

        mockMvc.perform(post("/users/createUser")
                        .param("name", name)
                        .param("email", email))
                .andExpect(status().isCreated())
                .andExpect(view().name(INDEX_VIEW_NAME));

        mockMvc.perform(get("/users/byId")
                        .param("userId", Long.toString(userId)))
                .andExpect(status().isOk())
                .andExpect(view().name(SHOW_USER_VIEW_NAME))
                .andExpect(model().attributeExists(USER_LIST_ATTRIBUTE))
                .andExpect(model().attribute(USER_LIST_ATTRIBUTE, hasSize(1)))
                .andExpect(model().attribute(USER_LIST_ATTRIBUTE, contains(user)));

        mockMvc.perform(get("/userAccounts/byUserId")
                .param("userId", Long.toString(userId)))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(USER_ACCOUNT_ATTRIBUTE))
                .andExpect(model().attribute(USER_ACCOUNT_ATTRIBUTE, hasProperty("funds", is(funds))));
    }

    @Test
    void testUpdateUser() throws Exception {
        long userId = 1L;
        String name = "Changed Name";
        String email = "changed@email.com";
        User user = new User(userId, name, email);
        BigDecimal funds = BigDecimal.valueOf(100.00).setScale(2);

        mockMvc.perform(put("/users/updateUser")
                        .param("id", Long.toString(userId))
                        .param("name", name)
                        .param("email", email))
                .andExpect(status().isCreated())
                .andExpect(view().name(INDEX_VIEW_NAME));

        mockMvc.perform(get("/users/byId")
                        .param("userId", Long.toString(userId)))
                .andExpect(status().isOk())
                .andExpect(view().name(SHOW_USER_VIEW_NAME))
                .andExpect(model().attributeExists(USER_LIST_ATTRIBUTE))
                .andExpect(model().attribute(USER_LIST_ATTRIBUTE, hasSize(1)))
                .andExpect(model().attribute(USER_LIST_ATTRIBUTE, contains(user)));

        mockMvc.perform(get("/userAccounts/byUserId")
                        .param("userId", Long.toString(userId)))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(USER_ACCOUNT_ATTRIBUTE))
                .andExpect(model().attribute(USER_ACCOUNT_ATTRIBUTE, hasProperty("funds", is(funds))));
    }

    @Test
    void deleteUser() throws Exception {
        long userId = 2L;

        mockMvc.perform(delete("/users/deleteUser")
                        .param("userId", Long.toString(userId)))
                .andExpect(status().isNoContent())
                .andExpect(view().name(INDEX_VIEW_NAME));

        mockMvc.perform(get("/users/byId")
                        .param("userId", Long.toString(userId)))
                .andExpect(view().name(SHOW_USER_VIEW_NAME))
                .andExpect(model().attribute(USER_LIST_ATTRIBUTE, empty()));
    }
}