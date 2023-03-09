package com.wojto.facade;

import com.wojto.model.Event;
import com.wojto.model.Ticket;
import com.wojto.model.User;
import com.wojto.service.EventService;
import com.wojto.service.TicketService;
import com.wojto.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Component
public class BookingFacadeImpl implements BookingFacade{

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingFacadeImpl.class);

    @Autowired
    EventService eventService;
    @Autowired
    UserService userService;
    @Autowired
    TicketService ticketService;

    public BookingFacadeImpl(EventService eventService, UserService userService, TicketService ticketService) {
        this.eventService = eventService;
        this.userService = userService;
        this.ticketService = ticketService;
    }

    @Override
    public Event getEventById(long eventId) {
        LOGGER.info("Calling EventService for event with id: " + eventId);
        return eventService.findEventById(eventId);
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        LOGGER.info(String.format("Calling EventService for events containing \"%s\" in title with page size/num: %d/%d", title, pageSize, pageNum));
        return eventService.findEventsByTitle(title, pageSize, pageNum);
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        LOGGER.info(String.format("Calling EventService for events on day \"%s\" in title with page size/num: %d/%d", day, pageSize, pageNum));
        return eventService.getEventsForDay(day, pageSize, pageNum);
    }

    @Override
    public Event createEvent(Event event) {
        LOGGER.info("Calling EventService to create event: " + event);
        return eventService.createEvent(event);
    }

    @Override
    public Event updateEvent(Event event) {
        LOGGER.info("Calling EventService to update event: " + event);
        return eventService.updateEvent(event);
    }

    @Override
    public boolean deleteEvent(long eventId) {
        LOGGER.info("Calling EventService to delete event with id,: " + eventId);
        return eventService.deleteEvent(eventId);
    }

    @Override
    public User getUserById(long userId) {
        LOGGER.info("Calling UserService for user with id: " + userId);
        return userService.getUserById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        LOGGER.info("Calling UserService for user with email: " + email);
        return userService.getUserByEmail(email);
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        LOGGER.info(String.format("Calling UserService for user with name containing \"%s\" with page size/num: %d/%d", name, pageSize, pageNum));
        return userService.getUsersByName(name, pageSize, pageNum);
    }

    @Override
    public User createUser(User user) {
        LOGGER.info("Calling UserService to create user: " + user);
        return userService.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        LOGGER.info("Calling UserService to update user: " + user);
        return userService.updateUser(user);
    }

    @Override
    public boolean deleteUser(long userId) {
        LOGGER.info("Calling UserService to delete user id: " + userId);
        return userService.deleteUser(userId);
    }

    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        LOGGER.info(
                String.format("Calling TicketService to book ticket with userId: %d, eventId: %d, place: %d, category: %s",
                        userId, eventId, place, category));
        return ticketService.bookTicket(userId, eventId, place, category);
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        LOGGER.info(String.format("Calling TicketService for booked tickets for user: %s with page size/num: %d/%d", user, pageSize, pageNum));
        return ticketService.getBooketTickets(user, pageSize, pageNum);
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        LOGGER.info(String.format("Calling TicketService for booked tickets for event: %s with page size/num: %d/%d", event, pageSize, pageNum));
        return ticketService.getBooketTickets(event, pageSize, pageNum);
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        LOGGER.info("Calling TicketService to cancel ticket with id: " + ticketId);
        return ticketService.cancelTicket(ticketId);
    }
}
