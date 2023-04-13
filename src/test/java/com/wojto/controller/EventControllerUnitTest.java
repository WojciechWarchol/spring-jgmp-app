package com.wojto.controller;

import com.wojto.facade.BookingFacade;
import com.wojto.model.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.math.BigDecimal.valueOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Import(EventController.class)
class EventControllerUnitTest {

    @Autowired
    private EventController eventController;

    @MockBean
    private BookingFacade bookingFacade;
    @MockBean
    private Model model;

    private Pageable pageable = PageRequest.of(0, 10);
    private List<Event> eventList;
    private Event testEvent;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    {
        try {
            testEvent = new Event(4, "Test Event", dateFormat.parse("01-03-2023"), valueOf(10.00));
            eventList = new ArrayList<>();
            eventList.add(new Event(1, "Music Event", dateFormat.parse("01-03-2023"), valueOf(50.00)));
            eventList.add(new Event(2, "IT Event", dateFormat.parse("13-04-2023"), valueOf(40.00)));
            eventList.add(new Event(3, "Culinary Event", dateFormat.parse("13-04-2023"), valueOf(30.00)));
        } catch (ParseException e) {
            System.out.println("Error in parsing dates for test events.");
        }
    }

    @Test
    void testGetEventById() throws Exception {
        long eventId = 1;
        List<Event> expectedList = Arrays.asList(testEvent);
        when(bookingFacade.getEventById(eventId)).thenReturn(testEvent);

        String viewName = eventController.getEventById(eventId, model);

        verify(model).addAttribute("eventList", expectedList);
        assertEquals("showEvents", viewName);
    }

    @Test
    void testGetEventsByTitle() throws Exception {
        String eventTitleFragment = "Event";
        when(bookingFacade.getEventsByTitle(eventTitleFragment, 10, 0)).thenReturn(eventList);

        String viewName = eventController.getEventsByTitle(eventTitleFragment, model);

        verify(model).addAttribute("eventList", eventList);
        assertEquals("showEvents", viewName);
    }

    @Test
    void testGetEventsForDay() throws ParseException {
        String eventDayString = "01-01-2023";
        Date eventDay = dateFormat.parse(eventDayString);
        List<Event> expectedList = Arrays.asList(eventList.get(0));
        when(bookingFacade.getEventsForDay(any(Date.class), anyInt(), anyInt())).thenReturn(expectedList);

        String viewName = eventController.getEventsForDay(eventDayString, model);

        verify(model).addAttribute("eventList", expectedList);
        assertEquals("showEvents", viewName);
    }

    @Test
    void testCreateEvent() throws ParseException {
        String eventTitle = "Test Event";
        String dateString = "01-03-2023";
        BigDecimal ticketPrice = BigDecimal.valueOf(10.00);
        when(bookingFacade.createEvent(new Event(eventTitle, dateFormat.parse(dateString), ticketPrice))).thenReturn(testEvent);

        String viewName = eventController.createEvent( eventTitle, dateString, ticketPrice);

        verify(bookingFacade).createEvent(any(Event.class));
        assertEquals("index", viewName);
    }

    @Test
    void testGoToEditedEventForm() {
        long eventId = 4L;
        when(bookingFacade.getEventById(eventId)).thenReturn(testEvent);

        String viewName = eventController.goToEditedEventForm(eventId, model);

        verify(model).addAttribute("event", testEvent);
        assertEquals("createEvent", viewName);
    }

    @Test
    void testUpdateEvent() throws ParseException {
        long eventId = 4L;
        String eventTitle = "Test Event 2";
        String dateString = "02-03-2023";
        BigDecimal ticketPrice = BigDecimal.valueOf(20.00);
        Event mockedEvent = mock(Event.class);
        when(bookingFacade.getEventById(eventId)).thenReturn(mockedEvent);

        String viewName = eventController.updateEvent(eventId, eventTitle, dateString, ticketPrice);

        assertEquals("index", viewName);
    }

    @Test
    void testDeleteEvent() {
        long eventId = 4L;
        when(bookingFacade.deleteEvent(eventId)).thenReturn(true);

        String viewName = eventController.deleteEvent(eventId);

        assertEquals("index", viewName);
    }
}