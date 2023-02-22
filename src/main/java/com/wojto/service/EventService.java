package com.wojto.service;

import com.wojto.dao.EventDao;
import com.wojto.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;


public class EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventService.class);

    EventDao eventDao;

    public List<Event> findAllEvents(int pageSize, int pageNum) {
        Page<Event> page = eventDao.getAllEvents(PageRequest.of(pageNum, pageSize));
        return page.getContent();
    }

    public Event findEventById(long id) {
        return eventDao.getEventById(id);
    }

    public List<Event> findEventsByTitle(String title, int pageSize, int pageNum) {
        Page<Event> page = eventDao.getEventsByTitle(title, PageRequest.of(pageNum, pageSize));
        return page.getContent();
    }

    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        Page<Event> page = eventDao.getEventsForDay(day, PageRequest.of(pageNum, pageSize));
        return page.getContent();
    }

    public Event createEvent(Event event) {
        return eventDao.createEvent(event);
    }

    public Event updateEvent(Event event) {
        return eventDao.updateEvent(event);
    }

    public boolean deleteEvent(long eventId) {
        return eventDao.deleteEvent(eventId);
    }

    public EventDao getEventDao() {
        return eventDao;
    }

    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }
}
