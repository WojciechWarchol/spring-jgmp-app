package com.wojto.storage;

import com.wojto.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class EventInMemoryStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventInMemoryStorage.class);

    private Map<Long, Event> eventMap = new HashMap<>();

    public EventInMemoryStorage() {
    }

    public List<Event> getAllEvents() {
        LOGGER.info("Getting all events, current number: " + eventMap.values().size());
        return new ArrayList<>(eventMap.values());
    }

    public Event getEventById(long eventId) {
        LOGGER.info("Retrieving event with id: " + eventId + " from in memory storage.");
        return eventMap.get(eventId);
    }

    public List<Event> getEventsByTitle(String title) {
        LOGGER.info("Retrieving events containing: \"" + title + "\" in title.");
        return eventMap.values().stream().filter(e -> e.getTitle().contains(title)).collect(Collectors.toList());
    }

    public List<Event> getEventsForDay(Date day) {
        LOGGER.info("Retrieving events for day: " + day);
        return eventMap.values().stream()
                .filter(e -> e.getDate().toInstant().truncatedTo(ChronoUnit.DAYS)
                        .equals(day.toInstant().truncatedTo(ChronoUnit.DAYS)))
                .collect(Collectors.toList());
    }

    public Event addOrUpdateEvent(Event event) {
        LOGGER.info("Adding event to in memory db: " + event);
        eventMap.put(event.getId(), event);
        return event;
    }

    public boolean deleteEvent(long eventId) {
        LOGGER.info("deleting event with id: " + eventId);
        return eventMap.remove(eventId) != null;
    }
}
