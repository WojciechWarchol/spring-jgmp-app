package com.wojto.integration;

import com.wojto.EventAppConfig;
import com.wojto.model.Event;
import net.sf.ehcache.CacheManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {EventAppConfig.class})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration(value = "/src/main/resources")
public class EventControllerIntegrationTests {

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
        CacheManager.getInstance().getCache("eventCache").removeAll();
        CacheManager.getInstance().getCache("userCache").removeAll();
        CacheManager.getInstance().getCache("userAccountCache").removeAll();
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesGreetController() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(webApplicationContext.getBean("eventController"));
    }

    @Test
    public void testGetEventById() throws Exception {
        long eventId = 1L;
        Event event = new Event(1l, "Music Event", dateFormat.parse("01-01-2023"), BigDecimal.valueOf(50.00).setScale(2));

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
                .andExpect(status().isOk())
                .andExpect(view().name(INDEX_VIEW_NAME));

        mockMvc.perform(get("/events/byId")
                        .param("eventId", Long.toString(eventId)))
                .andExpect(status().isOk())
                .andExpect(view().name(SHOW_EVENT_VIEW_NAME))
                .andExpect(model().attributeExists(EVENT_LIST_ATTRIBUTE))
                .andExpect(model().attribute(EVENT_LIST_ATTRIBUTE, hasSize(1)));
    }

    @Test
    public void testUpdateEvent() throws Exception {
        long eventId = 1L;
        String eventTitle = "Test Event 2";
        String dateString = "02-03-2023";
        String ticketPrice = "20.00";
        Event updatedEvent = new Event(eventId, eventTitle, dateFormat.parse(dateString), BigDecimal.valueOf(20.00));

        mockMvc.perform(post("/events/createEvent")
                        .param("id", "1")
                        .param("title", eventTitle)
                        .param("day", dateString)
                        .param("ticketPrice", ticketPrice))
                .andExpect(status().isOk())
                .andExpect(view().name(INDEX_VIEW_NAME));

        mockMvc.perform(get("/events/byId")
                        .param("eventId", Long.toString(eventId)))
                .andExpect(status().isOk())
                .andExpect(view().name(SHOW_EVENT_VIEW_NAME))
                .andExpect(model().attributeExists(EVENT_LIST_ATTRIBUTE))
                .andExpect(model().attribute(EVENT_LIST_ATTRIBUTE, hasSize(1)))
                .andExpect(model().attribute(EVENT_LIST_ATTRIBUTE, contains(updatedEvent)));;
    }

    @Test
    public void deleteEvent() throws Exception {
        long eventId = 1L;

        mockMvc.perform(post("/events/deleteEvent")
                .param("eventId", Long.toString(eventId)))
                .andExpect(status().isOk())
                .andExpect(view().name(INDEX_VIEW_NAME));

        mockMvc.perform(get("/events/byId")
                        .param("eventId", Long.toString(eventId)))
//                .andExpect(status().isOk())
                .andExpect(view().name(SHOW_EVENT_VIEW_NAME))
                .andExpect(model().attribute(EVENT_LIST_ATTRIBUTE, hasSize(0)));
    }
}
