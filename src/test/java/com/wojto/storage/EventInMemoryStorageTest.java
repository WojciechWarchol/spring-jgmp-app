package com.wojto.storage;

import com.wojto.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventInMemoryStorageTest {

    private EventInMemoryStorage eventInMemoryStorage;
    private Event event1;
    private Event event2;
    private Event event3;

    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            event1 = new Event(1, "Music Event", dateFormat.parse("01-03-2023"), BigDecimal.valueOf(50.00));
            event2 = new Event(2, "IT Event", dateFormat.parse("13-04-2023"), BigDecimal.valueOf(40.00));
            event3 = new Event(3, "Culinary Event", dateFormat.parse("13-04-2023"), BigDecimal.valueOf(30.00));
        } catch (ParseException e) {
            System.out.println("Error in parsing dates for test events.");
        }
    }


    @BeforeEach
    void initializeTestEnvironemnt() {
        eventInMemoryStorage = new EventInMemoryStorage();
        eventInMemoryStorage.addOrUpdateEvent(event1);
        eventInMemoryStorage.addOrUpdateEvent(event2);
        eventInMemoryStorage.addOrUpdateEvent(event3);
    }

    @Test
    void getAllEvents()  {
        int numberOfElements = eventInMemoryStorage.getAllEvents().size();
        assertEquals(3, numberOfElements);
    }

    @Test
    void getEventById() {
        Event foundEvent = eventInMemoryStorage.getEventById(1);
        assertEquals(event1, foundEvent);
    }

    @Test
    void getEventByExactTitle() {
        Event foundEvent = eventInMemoryStorage.getEventsByTitle("IT Event").get(0);
        assertEquals(event2, foundEvent);
    }

    @Test
    void getEventsContainingWordInTitle() {
        List<Event> foundEvents = eventInMemoryStorage.getEventsByTitle("Event");
        assertEquals(3, foundEvents.size());
    }

    @Test
    void getEventsForDay() {
        List<Event> foundEvents = eventInMemoryStorage.getEventsForDay(event2.getDate());
        assertTrue(foundEvents.contains(event2));
        assertTrue(foundEvents.contains(event3));
        assertEquals(2, foundEvents.size());
    }

    @Test
    void addOrUpdateEvent() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Event newEvent = new Event(4, "New Event", dateFormat.parse("06-06-2023"), BigDecimal.valueOf(10.00));
        eventInMemoryStorage.addOrUpdateEvent(newEvent);
        assertEquals(4, eventInMemoryStorage.getAllEvents().size());
        assertEquals(newEvent, eventInMemoryStorage.getEventById(4));
    }

    @Test
    void deleteEvent() {
        boolean eventDeleted = eventInMemoryStorage.deleteEvent(1);
        List<Event> foundEvents = eventInMemoryStorage.getAllEvents();
        assertTrue(eventDeleted);
        assertEquals(2, foundEvents.size());
        assertFalse(foundEvents.contains(event1));
    }

    @Test
    void noEventDeletedWhenProvidedIdDoesntExist() {
        boolean eventDeleted = eventInMemoryStorage.deleteEvent(5);
        List<Event> foundEvents = eventInMemoryStorage.getAllEvents();
        assertFalse(eventDeleted);
        assertEquals(3, foundEvents.size());
    }
}