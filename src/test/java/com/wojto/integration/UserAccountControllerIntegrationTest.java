package com.wojto.integration;

import com.wojto.EventAppConfig;
import com.wojto.model.User;
import com.wojto.model.UserAccount;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {EventAppConfig.class})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration(value = "/src/main/resources")
class UserAccountControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private static final String SHOW_USER_ACCOUNT_VIEW_NAME = "showUserAccount";
    private static final String USER_ACCOUNT_ATTRIBUTE = "userAccount";

    @BeforeEach
    void setUp() {
        ApplicationContext context = new AnnotationConfigApplicationContext(EventAppConfig.class);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @AfterEach
    void cleanup() {

    }

    @Test
    void testGetUserAccountByUserId() throws Exception {
        long userId = 5L;
        BigDecimal funds = BigDecimal.valueOf(100.10).setScale(2);
        UserAccount userAccount = new UserAccount(userId, userId);
        userAccount.setFunds(funds);

        mockMvc.perform(get("/userAccounts/byUserId")
                        .param("userId", Long.toString(userId)))
                .andExpect(status().isOk())
                .andExpect(view().name(SHOW_USER_ACCOUNT_VIEW_NAME))
                .andExpect(model().attributeExists(USER_ACCOUNT_ATTRIBUTE))
                .andExpect(model().attribute(USER_ACCOUNT_ATTRIBUTE, hasProperty("id", is(userId))))
                .andExpect(model().attribute(USER_ACCOUNT_ATTRIBUTE, hasProperty("userId", is(userId))))
                .andExpect(model().attribute(USER_ACCOUNT_ATTRIBUTE, hasProperty("funds", is(funds))));
    }

    @Test
    void testTopUp() throws Exception {
        long userId = 5L;
        BigDecimal amount = BigDecimal.valueOf(9.90).setScale(2);
        BigDecimal funds = BigDecimal.valueOf(110.00).setScale(2);

        mockMvc.perform(post("/userAccounts/topUp")
                        .param("userId", Long.toString(userId))
                        .param("amount", amount.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name(SHOW_USER_ACCOUNT_VIEW_NAME))
                .andExpect(model().attributeExists(USER_ACCOUNT_ATTRIBUTE))
                .andExpect(model().attribute(USER_ACCOUNT_ATTRIBUTE, hasProperty("funds", is(funds))));
    }
}