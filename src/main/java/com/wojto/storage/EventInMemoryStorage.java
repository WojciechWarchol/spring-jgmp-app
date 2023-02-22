package com.wojto.storage;

import com.wojto.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class EventInMemoryStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventInMemoryStorage.class);

    private Map<Long, Event> eventMap = new HashMap<>();

    public EventInMemoryStorage() {
    }

    public List<Event> getAllEvents() {
        return new ArrayList<>(eventMap.values());
    }

    public Event getEventById(long eventId) {
        return eventMap.get(eventId);
    }

    public List<Event> getEventsByTitle(String title) {
        return eventMap.values().stream().filter(e -> e.getTitle().contains(title)).collect(Collectors.toList());
    }

    public List<Event> getEventsForDay(Date day) {
        return eventMap.values().stream()
                .filter(e -> e.getDate().toInstant().truncatedTo(ChronoUnit.DAYS)
                        .equals(day.toInstant().truncatedTo(ChronoUnit.DAYS)))
                .collect(Collectors.toList());
    }

    public Event addOrUpdateEvent(Event event) {
        return eventMap.put(event.getId(), event);
    }

    public boolean deleteEvent(long eventId) {
        return eventMap.remove(eventId).getId() == eventId;
    }
}
