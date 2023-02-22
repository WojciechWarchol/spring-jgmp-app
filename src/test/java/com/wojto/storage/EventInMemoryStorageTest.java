package com.wojto.storage;

import com.wojto.model.Event;
import com.wojto.model.EventImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
            event1 = new EventImpl(1, "Music Event", dateFormat.parse("01-03-2023"));
            event2 = new EventImpl(2, "IT Event", dateFormat.parse("13-04-2023"));
            event3 = new EventImpl(3, "Culinary Event", dateFormat.parse("13-04-2023"));
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
        Event newEvent = new EventImpl(4, "New Event", dateFormat.parse("06-06-2023"));
        eventInMemoryStorage.addOrUpdateEvent(newEvent);
        assertEquals(4, eventInMemoryStorage.getAllEvents().size());
        assertEquals(newEvent, eventInMemoryStorage.getEventById(4));
    }

    @Test
    void deleteEvent() {
        eventInMemoryStorage.deleteEvent(1);
        List<Event> foundEvents = eventInMemoryStorage.getAllEvents();
        assertEquals(2, foundEvents.size());
        assertFalse(foundEvents.contains(event1));
    }

    // TODO add test cases for failed operations maybe?
}