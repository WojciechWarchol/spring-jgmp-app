package com.wojto.dao;

import com.wojto.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface EventDao {

    public Page<Event> getAllEvents(Pageable pageable);

    public Event getEventById(long id);

    public Page<Event> getEventsByTitle(String title, Pageable pageable);

    public Page<Event> getEventsForDay(Date day, Pageable pageable);

    public Event createEvent(Event event);

    public Event updateEvent(Event event);

    public boolean deleteEvent(long eventId);

}
