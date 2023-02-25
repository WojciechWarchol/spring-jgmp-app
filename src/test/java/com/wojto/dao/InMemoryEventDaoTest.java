package com.wojto.dao;

import com.wojto.model.Event;
import com.wojto.model.EventImpl;
import com.wojto.storage.EventInMemoryStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration("file:src/main/java/ApplicationContext.xml")
class InMemoryEventDaoTest {

    @InjectMocks
    private InMemoryEventDao inMemoryEventDAO;
    @Mock
    private EventInMemoryStorage eventInMemoryStorageMock;
    private Pageable pageable = PageRequest.of(0, 10);;

//    @Mock
    private List<Event> eventListMock;
    @Mock
    private Event eventMock;
    private Event testEvent;
    @Mock
    private PageImpl<Event> pageMock;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    {
        try {
            testEvent = new EventImpl(4, "Test Event", dateFormat.parse("01-03-2023"));
            eventListMock = new ArrayList<>();
            eventListMock.add(new EventImpl(1, "Music Event", dateFormat.parse("01-03-2023")));
            eventListMock.add(new EventImpl(2, "IT Event", dateFormat.parse("13-04-2023")));
            eventListMock.add(new EventImpl(3, "Culinary Event", dateFormat.parse("13-04-2023")));
        } catch (ParseException e) {
            System.out.println("Error in parsing dates for test events.");
        }
    }


    @Test
    void findAllEvents() {
        when(eventInMemoryStorageMock.getAllEvents()).thenReturn(eventListMock);
        Page<Event> recievedPage = inMemoryEventDAO.getAllEvents(pageable);
        assertNotNull(recievedPage);
    }

    @Test
    void findEventById() {
        when(eventInMemoryStorageMock.getEventById(anyLong())).thenReturn(eventMock);
        Event event = inMemoryEventDAO.getEventById(1);
        assertEquals(eventMock, event);
    }

    @Test
    void findEventsByTitle() {
        when(eventInMemoryStorageMock.getEventsByTitle(anyString())).thenReturn(eventListMock);
        Page<Event> receivedPage = inMemoryEventDAO.getEventsByTitle(anyString(), pageable);
        assertEquals(eventListMock, receivedPage.getContent());
    }

    @Test
    void findEventsByTitleWithPageOfSizeOne() {
        when(eventInMemoryStorageMock.getEventsByTitle(anyString())).thenReturn(eventListMock);
        Pageable pageOfSizeOne = PageRequest.of(1, 1);
        Page<Event> receivedPage = inMemoryEventDAO.getEventsByTitle(anyString(), pageOfSizeOne);
        assertEquals(eventListMock.get(1), receivedPage.getContent().get(0));
    }

    @Test
    void getEventsForDay() throws ParseException {
        Date testDate = dateFormat.parse("01-03-2023");
        when(eventInMemoryStorageMock.getEventsForDay(testDate)).thenReturn(eventListMock.subList(0, 1));
        Page<Event> receivedPage = inMemoryEventDAO.getEventsForDay(testDate, pageable);
        assertEquals(eventListMock.get(0), receivedPage.getContent().get(0));
    }

    @Test
    void createEvent() throws ParseException {
        when(eventInMemoryStorageMock.addOrUpdateEvent(testEvent)).thenReturn(testEvent);
        Event returnedEvent = inMemoryEventDAO.createEvent(testEvent);
        assertEquals(testEvent, returnedEvent);
    }

    @Test
    void updateEvent() throws ParseException {
        when(eventInMemoryStorageMock.addOrUpdateEvent(testEvent)).thenReturn(testEvent);
        Event returnedEvent = inMemoryEventDAO.updateEvent(testEvent);
        assertEquals(testEvent, returnedEvent);
    }

    @Test
    void deleteEvent() {
        when(eventInMemoryStorageMock.deleteEvent(anyLong())).thenReturn(true);
        boolean deletd = inMemoryEventDAO.deleteEvent(1);
        assertTrue(deletd);
    }
}