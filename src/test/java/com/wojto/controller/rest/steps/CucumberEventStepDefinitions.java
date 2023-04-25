package com.wojto.controller.rest.steps;

import com.wojto.controller.rest.EventRestController;
import com.wojto.dao.DBEventRepository;
import com.wojto.dao.DBTicketRepository;
import com.wojto.dao.DBUserAccountRepository;
import com.wojto.dao.DBUserRepository;
import com.wojto.facade.BookingFacade;
import com.wojto.model.Event;
import com.wojto.service.EventService;
import com.wojto.service.TicketService;
import com.wojto.service.UserAccountService;
import com.wojto.service.UserService;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = EventRestController.class)
//@AutoConfigureMockMvc
//@SpringBootTest
@CucumberContextConfiguration
public class CucumberEventStepDefinitions {

    static ResultActions resultActions = null;
    private static final String EVENT_LIST_ATTRIBUTE = "eventList";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    private EventRestController eventRestController;

    @MockBean
    private BookingFacade bookingFacade;

    @MockBean
    private EventService eventService;
    @MockBean
    private UserService userService;
    @MockBean
    private TicketService ticketService;
    @MockBean
    private UserAccountService userAccountService;
    @MockBean
    private DBEventRepository dbEventRepository;
    @MockBean
    private DBUserRepository dbUserRepository;
    @MockBean
    private DBTicketRepository dbTicketRepository;
    @MockBean
    private DBUserAccountRepository dbUserAccountRepository;

    private Event event1;
    private Event event2;
    private Event event3;

    {
        try {
            event1 = new Event(1L, "Music Event", dateFormat.parse("01-01-2023"), BigDecimal.valueOf(50.00).setScale(2));
            event2 = new Event(2L, "IT Event", dateFormat.parse("13-04-2023"), BigDecimal.valueOf(40.00).setScale(2));
            event3 = new Event(3L, "Culinary Event", dateFormat.parse("13-04-2023"), BigDecimal.valueOf(30.00).setScale(2));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @When("the client calls \\/api\\/events\\/byId with id {int}")
    public void callForEventById(final int eventId) throws Exception {
        when(bookingFacade.getEventById(1L)).thenReturn(event1);

        resultActions = mockMvc.perform(get("/api/events/byId").param("eventId", Integer.toString(eventId)));
    }

    @When("the client calls \\/api\\/events\\/byTitle with string {string}")
    public void callForEventsByTitle(final String title) throws Exception {
        List<Event> eventList = List.of(event1, event2, event3);

        when(bookingFacade.getEventsByTitle("Event", 10, 0)).thenReturn(eventList);

        resultActions = mockMvc.perform(get("/api/events/byTitle").param("title", title));
    }

    @Then("the client should receive status code {int}")
    public void checkStatucCode(final int statusCode) throws Exception {
        resultActions.andExpect(status().is(statusCode));
    }

    @Then("the client should receive event with id {int} and title {string}")
    public void checkRecievedEvent(final int eventId, final String title) throws Exception {
        resultActions.andExpect(jsonPath("$.[*].id", hasItem(eventId)))
                .andExpect(jsonPath("$.[*].title", hasItem(title)));
    }

    @Then("the client should receive an empty list")
    public void checkThatListEmpty() throws Exception {
        resultActions.andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Then("the client should receive a list with {int} elements")
    public void checkNumberOfElementsInList(final int numberOfElements) throws Exception {
        resultActions.andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(numberOfElements)));
    }
}
