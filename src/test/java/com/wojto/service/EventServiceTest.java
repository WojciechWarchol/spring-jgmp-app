package com.wojto.service;

import com.wojto.dao.InMemoryEventDao;
import com.wojto.model.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private InMemoryEventDao inMemoryEventDaoMock;
    @Mock
    private List<Event> eventListMock;
    @Mock
    private Page<Event> eventPageMock;
    @Mock
    private Event eventMock;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");


    @Test
    void findAllEvents() {
        when(inMemoryEventDaoMock.getAllEvents(any())).thenReturn(eventPageMock);
        when(eventPageMock.getContent()).thenReturn(eventListMock);
        List<Event> eventList = eventService.findAllEvents(5, 0);
        assertNotNull(eventList);
    }

    @Test
    void findEventById() {
        when(inMemoryEventDaoMock.getEventById(anyLong())).thenReturn(eventMock);
        Event event = eventService.findEventById(1);
        assertNotNull(event);
    }

    @Test
    void findEventsByTitle() {
        when(inMemoryEventDaoMock.getEventsByTitle(anyString(), any())).thenReturn(eventPageMock);
        when(eventPageMock.getContent()).thenReturn(eventListMock);
        List<Event> eventList = eventService.findEventsByTitle("Title", 5, 0);
        assertNotNull(eventList);
    }

    @Test
    void getEventsForDay() throws ParseException {
        when(inMemoryEventDaoMock.getEventsForDay(any(), any())).thenReturn(eventPageMock);
        when(eventPageMock.getContent()).thenReturn(eventListMock);
        List<Event> eventList = eventService.getEventsForDay(dateFormat.parse("01-01-2023"), 5, 2);
        assertNotNull(eventList);
    }

    @Test
    void createEvent() {
        when(inMemoryEventDaoMock.createEvent(eventMock)).thenReturn(eventMock);
        Event event = eventService.createEvent(eventMock);
        assertEquals(eventMock, event);
    }

    @Test
    void updateEvent() {
        when(inMemoryEventDaoMock.updateEvent(eventMock)).thenReturn(eventMock);
        Event event = eventService.updateEvent(eventMock);
        assertEquals(eventMock, event);
    }

    @Test
    void deleteEvent() {
        when(inMemoryEventDaoMock.deleteEvent(anyLong())).thenReturn(true);
        boolean eventDeleted = eventService.deleteEvent(1);
        assertTrue(eventDeleted);
    }
}