package com.wojto.dao;

import com.wojto.model.Event;
import com.wojto.model.Ticket;
import com.wojto.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TicketDao {

    public Page<Ticket> getAllTickets(Pageable pageable);

    public Ticket getTicketById(long ticketId);

    public Page<Ticket> getBookedTickets(User user, Pageable pageable);

    public Page<Ticket> getBookedTickets(Event event, Pageable pageable);

    public List<Ticket> getBookedTickets(long eventId);

    public Ticket bookTicket(Ticket ticket);

    public boolean deleteTicket(long ticketId);

}
