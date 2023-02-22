package com.wojto.dao;

import com.wojto.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface EventDao {

    public Page<Event> getAllEvents(Pageable pageable);

    public Event getEventById(long id);

    public Page<Event> getEventsByTitle(String title, Pageable pageable);

    Page<Event> getEventsForDay(Date day, Pageable pageable);

    Event createEvent(Event event);

    Event updateEvent(Event event);

    boolean deleteEvent(long eventId);

}
