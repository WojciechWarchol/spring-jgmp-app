package com.wojto.service;

import com.wojto.dao.DBEventRepository;
import com.wojto.dao.EventDao;
import com.wojto.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventService.class);

    @Autowired
    @Qualifier("DBEventRepository")
    EventDao eventDao;

    public List<Event> findAllEvents(int pageSize, int pageNum) {
        LOGGER.info("Calling EventDao for all events.");
        Page<Event> page = eventDao.getAllEvents(PageRequest.of(pageNum, pageSize));
        return page.getContent();
    }

    @Cacheable("eventCache")
    public Event findEventById(long id) {
        LOGGER.info("Calling EventDao for event with id: " + id);
        return eventDao.getEventById(id);
    }

    public List<Event> findEventsByTitle(String title, int pageSize, int pageNum) {
        LOGGER.info("Calling EventDao for events containing \"" + title + "\" in title");
        Page<Event> page = eventDao.getEventsByTitle(title, PageRequest.of(pageNum, pageSize));
        return page.getContent();
    }

    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        LOGGER.info("Calling EventDao for events on day: " + day);
        Page<Event> page = eventDao.getEventsForDay(day, PageRequest.of(pageNum, pageSize));
        return page.getContent();
    }

    public Event createEvent(Event event) {
        LOGGER.info("Calling EventDao to create event: " + event);
        return eventDao.createEvent(event);
    }

    public Event updateEvent(Event event) {
        LOGGER.info("Calling EventDao to update event: " + event);
        return eventDao.updateEvent(event);
    }

    public boolean deleteEvent(long eventId) {
        LOGGER.info("Calling EventDao to delete event with id: " + eventId);
        return eventDao.deleteEvent(eventId);
    }

    public EventDao getEventDao() {
        return eventDao;
    }

    public void setEventDao(DBEventRepository eventDao) {
        this.eventDao = eventDao;
    }
}
