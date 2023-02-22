package com.wojto.dao;

import com.wojto.model.Event;
import com.wojto.storage.EventInMemoryStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public class InMemoryEventDao implements EventDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryEventDao.class);

    EventInMemoryStorage eventInMemoryStorage;

    @Override
    public Page<Event> getAllEvents(Pageable pageable) {
        List<Event> allEventsList = eventInMemoryStorage.getAllEvents();
        Page<Event> page = convertListToPage(pageable, allEventsList);
        return page;
    }

    @Override
    public Event getEventById(long id) {
        return eventInMemoryStorage.getEventById(id);
    }

    @Override
    public Page<Event> getEventsByTitle(String title, Pageable pageable) {
        List<Event> eventsFoundByTitle = eventInMemoryStorage.getEventsByTitle(title);
        Page<Event> page = convertListToPage(pageable, eventsFoundByTitle);
        return page;
    }

    @Override
    public Page<Event> getEventsForDay(Date day, Pageable pageable) {
        List<Event> eventsFoundByDay = eventInMemoryStorage.getEventsForDay(day);
        Page<Event> page = convertListToPage(pageable, eventsFoundByDay);
        return page;
    }

    @Override
    public Event createEvent(Event event) {
        return eventInMemoryStorage.addOrUpdateEvent(event);
    }

    @Override
    public Event updateEvent(Event event) {
        return eventInMemoryStorage.addOrUpdateEvent(event);
    }

    @Override
    public boolean deleteEvent(long eventId) {
        return eventInMemoryStorage.deleteEvent(eventId);
    }

    private static Page<Event> convertListToPage(Pageable pageable, List<Event> listOfEvents) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), listOfEvents.size());
        Page<Event> page = new PageImpl<Event>(listOfEvents.subList(start, end), pageable, listOfEvents.size());
        return page;
    }

    public EventInMemoryStorage getEventInMemoryStorage() {
        return eventInMemoryStorage;
    }

    public void setEventInMemoryStorage(EventInMemoryStorage eventInMemoryStorage) {
        this.eventInMemoryStorage = eventInMemoryStorage;
    }
}
