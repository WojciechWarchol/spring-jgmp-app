package com.wojto.service;

import com.wojto.dao.TicketDao;
import com.wojto.model.Event;
import com.wojto.model.Ticket;
import com.wojto.model.TicketImpl;
import com.wojto.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class TicketService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketService.class);
    
    TicketDao ticketDao;

    public List<Ticket> findAllTickets(int pageSize, int pageNum) {
        LOGGER.info("Calling TicketDao for all events.");
        Page<Ticket> page = ticketDao.getAllTickets(PageRequest.of(pageNum, pageSize));
        return page.getContent();
    }
    
    public Ticket findTicketById(long ticketId) {
        LOGGER.info("Calling TicketDao for ticket with id: " + ticketId);
        return ticketDao.getTicketById(ticketId);
    }

    public List<Ticket> getBooketTickets(User user, int pageSize, int pageNum) {
        LOGGER.info("Calling TicketDao for tickets booked for user: " + user);
        Page<Ticket> page = ticketDao.getBookedTickets(user, PageRequest.of(pageNum, pageSize));
        return page.getContent();
    }

    public List<Ticket> getBooketTickets(Event event, int pageSize, int pageNum) {
        LOGGER.info("Calling TicketDao for tickets booked for event: " + event);
        Page<Ticket> page = ticketDao.getBookedTickets(event, PageRequest.of(pageNum, pageSize));
        return page.getContent();
    }

    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        LOGGER.info("Searching if place on event is free for booking");
        Ticket newTicket;
        List<Ticket> ticketsBookedForEvent = ticketDao.getBookedTickets(eventId);
        boolean placeBooked = ticketsBookedForEvent.stream().anyMatch(t -> t.getPlace() == place);
        if (placeBooked) {
            LOGGER.error(String.format("The place: %d, on event: %d is already booked!", place, eventId));
            throw new IllegalStateException();
        } else {
            newTicket = new TicketImpl(0, eventId, userId, category, place);
            LOGGER.info("Place is free, booked ticket: " + newTicket);
            ticketDao.bookTicket(newTicket);
        }
        return newTicket;
    }

    public boolean cancelTicket(long ticketId) {
        LOGGER.info("Cancelling ticket with id: " + ticketId);
        return ticketDao.deleteTicket(ticketId);
    }

    public TicketDao getTicketDao() {
        return ticketDao;
    }

    public void setTicketDao(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }
}
