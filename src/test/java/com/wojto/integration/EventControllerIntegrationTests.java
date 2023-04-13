package com.wojto.integration;

import com.wojto.model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.cache.CacheManager;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WebAppConfiguration(value = "/src/main/resources")
@Transactional
public class EventControllerIntegrationTests {

    @Autowired
    CacheManager cacheManager;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    private static final String SHOW_EVENT_VIEW_NAME = "showEvents";
    private static final String INDEX_VIEW_NAME = "index";
    private static final String EVENT_LIST_ATTRIBUTE = "eventList";

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @AfterEach
    void cleanup() {
        cacheManager.getCacheNames().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
    }

    @Test
    public void testGetEventById() throws Exception {
        long eventId = 1L;
        Event event = new Event(eventId, "Music Event", dateFormat.parse("01-01-2023"), BigDecimal.valueOf(50.00).setScale(2));

        mockMvc.perform(get("/events/byId")
                        .param("eventId", Long.toString(eventId)))
                .andExpect(status().isOk())
                .andExpect(view().name(SHOW_EVENT_VIEW_NAME))
                .andExpect(model().attributeExists(EVENT_LIST_ATTRIBUTE))
                .andExpect(model().attribute(EVENT_LIST_ATTRIBUTE, hasSize(1)))
                .andExpect(model().attribute(EVENT_LIST_ATTRIBUTE, contains(event)));
    }

    @Test
    public void testGetEventsByTitle() throws Exception {
        String eventTitleFragment = "Event";

        mockMvc.perform(get("/events/byTitle")
                        .param("title", eventTitleFragment))
                .andExpect(status().isOk())
                .andExpect(view().name(SHOW_EVENT_VIEW_NAME))
                .andExpect(model().attributeExists(EVENT_LIST_ATTRIBUTE))
                .andExpect(model().attribute(EVENT_LIST_ATTRIBUTE, hasSize(3)));
    }

    @Test
    public void testGetEventsForDay() throws Exception {
        String eventDayString = "2023-01-01";

        mockMvc.perform(get("/events/forDay")
                        .param("day", eventDayString))
                .andExpect(status().isOk())
                .andExpect(view().name(SHOW_EVENT_VIEW_NAME))
                .andExpect(model().attributeExists(EVENT_LIST_ATTRIBUTE))
                .andExpect(model().attribute(EVENT_LIST_ATTRIBUTE, hasSize(1)));
    }

    @Test
    public void testCreateEvent() throws Exception {
        long eventId = 4L;
        String eventTitle = "Test Event";
        String dateString = "2023-09-01";
        String ticketPrice = "10.00";

        mockMvc.perform(post("/events/createEvent")
                        .param("title", eventTitle)
                        .param("day", dateString)
                        .param("ticketPrice", ticketPrice))
                .andExpect(status().isCreated())
                .andExpect(view().name(INDEX_VIEW_NAME));

        mockMvc.perform(get("/events/byId")
                        .param("eventId", Long.toString(eventId)))
                .andExpect(status().isOk())
                .andExpect(view().name(SHOW_EVENT_VIEW_NAME))
                .andExpect(model().attributeExists(EVENT_LIST_ATTRIBUTE))
                .andExpect(model().attribute(EVENT_LIST_ATTRIBUTE, hasSize(1)))
                .andExpect(model().attribute(EVENT_LIST_ATTRIBUTE, hasItem(
                        allOf(
                                hasProperty("title", is(eventTitle))
                        )
                )));
    }

    @Test
    public void testUpdateEvent() throws Exception {
        long eventId = 1L;
        String eventTitle = "Test Event 2";
        String dateString = "2023-09-01";
        String ticketPrice = "20.00";
        Event updatedEvent = new Event(eventId, eventTitle, dateFormat.parse("01-09-2023"), BigDecimal.valueOf(20.00).setScale(2));

        mockMvc.perform(put("/events/updateEvent")
                        .param("id", "1")
                        .param("title", eventTitle)
                        .param("day", dateString)
                        .param("ticketPrice", ticketPrice))
                .andExpect(status().isCreated())
                .andExpect(view().name(INDEX_VIEW_NAME));

        mockMvc.perform(get("/events/byId")
                        .param("eventId", Long.toString(eventId)))
                .andExpect(status().isOk())
                .andExpect(view().name(SHOW_EVENT_VIEW_NAME))
                .andExpect(model().attributeExists(EVENT_LIST_ATTRIBUTE))
                .andExpect(model().attribute(EVENT_LIST_ATTRIBUTE, hasSize(1)))
                .andExpect(model().attribute(EVENT_LIST_ATTRIBUTE, contains(updatedEvent)));
    }

    @Test
    public void deleteEvent() throws Exception {
        long eventId = 1L;

        mockMvc.perform(delete("/events/deleteEvent")
                        .param("eventId", Long.toString(eventId)))
                .andExpect(status().isNoContent())
                .andExpect(view().name(INDEX_VIEW_NAME));

        mockMvc.perform(get("/events/byId")
                        .param("eventId", Long.toString(eventId)))
                .andExpect(view().name(SHOW_EVENT_VIEW_NAME))
                .andExpect(model().attribute(EVENT_LIST_ATTRIBUTE, empty()));
    }
}
