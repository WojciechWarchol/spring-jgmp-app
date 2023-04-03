package com.wojto.controller;

import com.wojto.EventAppConfig;
import com.wojto.model.Event;
import com.wojto.model.Ticket;
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
class TicketControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private static final String SHOW_TICKET_VIEW_NAME = "showTickets";
    private static final String INDEX_VIEW_NAME = "index";
    private static final String TICKET_LIST_ATTRIBUTE = "ticketList";
    private static final String USER_ACCOUNT_ATTRIBUTE = "userAccount";

    @BeforeEach
    void setUp() {
        ApplicationContext context = new AnnotationConfigApplicationContext(EventAppConfig.class);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @AfterEach
    void tearDown() {
        CacheManager.getInstance().getCache("ticketCache").removeAll();
    }

    @Test
    void testGetTicketsForUser() throws Exception {
        long userId = 1L;

        mockMvc.perform(get("/tickets/forUser")
                        .param("userId", Long.toString(userId)))
                .andExpect(status().isOk())
                .andExpect(view().name(SHOW_TICKET_VIEW_NAME))
                .andExpect(model().attributeExists(TICKET_LIST_ATTRIBUTE))
                .andExpect(model().attribute(TICKET_LIST_ATTRIBUTE, hasSize(2)));
    }

    @Test
    void testGetTicketsForEvent() throws Exception {
        long eventId = 3L;

        mockMvc.perform(get("/tickets/forEvent")
                        .param("eventId", Long.toString(eventId)))
                .andExpect(status().isOk())
                .andExpect(view().name(SHOW_TICKET_VIEW_NAME))
                .andExpect(model().attributeExists(TICKET_LIST_ATTRIBUTE))
                .andExpect(model().attribute(TICKET_LIST_ATTRIBUTE, hasSize(3)));
    }

    @Test
    void testBookTicket() throws Exception {
        long userId = 1L;
        long eventId = 3L;
        int place = 4;
        Ticket.Category category = Ticket.Category.BAR;

        mockMvc.perform(post("/tickets/bookTicket")
                        .param("userId", Long.toString(userId))
                        .param("eventId", Long.toString(eventId))
                        .param("place", Integer.toString(place))
                        .param("category", Ticket.Category.BAR.name()))
                .andExpect(status().isCreated())
                .andExpect(view().name(INDEX_VIEW_NAME));

        mockMvc.perform(get("/tickets/forUser")
                        .param("userId", Long.toString(userId)))
                .andExpect(model().attribute(TICKET_LIST_ATTRIBUTE, hasSize(3)));
    }

    @Test
    void testCancelTicket() throws Exception {
        Long userId = 4L;
        Long ticketIdToDelete = 7L;
        BigDecimal userAccountAfterRefund = BigDecimal.valueOf(180.30).setScale(2);

        mockMvc.perform(post("/tickets/cancelTicket")
                        .param("ticketId", Long.toString(ticketIdToDelete)))
                .andExpect(status().isNoContent())
                .andExpect(view().name(INDEX_VIEW_NAME));

        mockMvc.perform(get("/tickets/forUser")
                        .param("userId", Long.toString(userId)))
                .andExpect(model().attribute(TICKET_LIST_ATTRIBUTE, hasSize(0)));

        mockMvc.perform(get("/userAccounts/byUserId")
                .param("userId", Long.toString(userId)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("userAccount", hasProperty("funds", is(userAccountAfterRefund))));
    }

    /*    @Test
        void bookTickets() {
        }*/

    /*    @Test
        void getPdfForUser() {
        }*/
}