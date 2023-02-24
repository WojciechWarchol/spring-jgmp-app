package com.wojto.dao;

import com.wojto.model.Event;
import com.wojto.storage.EventInMemoryStorage;
import com.wojto.storage.mappers.EventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public class InMemoryEventDao implements EventDao, InMemoryDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryEventDao.class);

    EventInMemoryStorage eventInMemoryStorage;

    private static String fileName;
    private static final String[] PARAM_NAMES = new String[] { "id", "title", "date" };
    private static final Class<Event> SUPPORTED_CLASS_TYPE = Event.class;

    @Override
    public String getFileName() {
        return fileName;
    }

    public static void setFileName(String fileName) {
        InMemoryEventDao.fileName = fileName;
    }

    @Override
    public String[] getParameterNames() {
        return PARAM_NAMES;
    }

    @Override
    public Class<Event> getClassType() {
        return SUPPORTED_CLASS_TYPE;
    }

    @Override
    public FieldSetMapper getMapperForObjects() {
        return new EventMapper();
    }

    @Override
    public Page<Event> getAllEvents(Pageable pageable) {
        LOGGER.info("Calling in memory storage to retrieve all events.");
        List<Event> allEventsList = eventInMemoryStorage.getAllEvents();
        LOGGER.info("Retrieved list of all events: " + allEventsList );
        Page<Event> page = convertListToPage(pageable, allEventsList);
        return page;
    }

    @Override
    public Event getEventById(long id) {
        LOGGER.info("Calling in memory storage for event with id: " + id);
        Event event = eventInMemoryStorage.getEventById(id);
        LOGGER.info("Retrieved event from in memory storage: " + event);
        return event;
    }

    @Override
    public Page<Event> getEventsByTitle(String title, Pageable pageable) {
        LOGGER.info("Calling in memory storage for event containing \"" + title + "\" in title.");
        List<Event> eventsFoundByTitle = eventInMemoryStorage.getEventsByTitle(title);
        LOGGER.info("Retrieved events from in memory storage: " + eventsFoundByTitle);
        Page<Event> page = convertListToPage(pageable, eventsFoundByTitle);
        return page;
    }

    @Override
    public Page<Event> getEventsForDay(Date day, Pageable pageable) {
        LOGGER.info("Calling in memory storage for events on day: " + day );
        List<Event> eventsFoundByDay = eventInMemoryStorage.getEventsForDay(day);
        LOGGER.info("Retrieved events from in memory storage: " + eventsFoundByDay);
        Page<Event> page = convertListToPage(pageable, eventsFoundByDay);
        return page;
    }

    @Override
    public Event createEvent(Event event) {
        LOGGER.info("Adding event to in memory storage: " + event);
        return eventInMemoryStorage.addOrUpdateEvent(event);
    }

    @Override
    public Event updateEvent(Event event) {
        LOGGER.info("Updating event in in memory storage: " + event);
        return eventInMemoryStorage.addOrUpdateEvent(event);
    }

    @Override
    public boolean deleteEvent(long eventId) {
        LOGGER.info("Deleting event from in memory storage with id: " + eventId);
        return eventInMemoryStorage.deleteEvent(eventId);
    }

    private static Page<Event> convertListToPage(Pageable pageable, List<Event> listOfEvents) {
        LOGGER.debug("Converting event List to Page");
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), listOfEvents.size());
        Page<Event> page = new PageImpl<Event>(listOfEvents.subList(start, end), pageable, listOfEvents.size());
        LOGGER.debug("Created page of events: " + page);
        return page;
    }

    public EventInMemoryStorage getEventInMemoryStorage() {
        return eventInMemoryStorage;
    }

    public void setEventInMemoryStorage(EventInMemoryStorage eventInMemoryStorage) {
        this.eventInMemoryStorage = eventInMemoryStorage;
    }
}
