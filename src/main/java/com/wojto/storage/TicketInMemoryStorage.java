package com.wojto.storage;

import com.wojto.model.Event;
import com.wojto.model.Ticket;
import com.wojto.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TicketInMemoryStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketInMemoryStorage.class);

    private Map<Long, Ticket> ticketMap = new HashMap<>();
    private long TOTAL_TICKET_COUNT = 0;

    public TicketInMemoryStorage() {
    }

    public List<Ticket> getAllTickets() {
        LOGGER.info("Getting all tickets, current number: " + ticketMap.values().size());
        return new ArrayList<>(ticketMap.values());
    }

    public Ticket getTicketById(long ticketId) {
        LOGGER.info("Retrieving ticket with id: " + ticketId + " from in memory storage.");
        return ticketMap.get(ticketId);
    }

    public List<Ticket> getBooketTickets(User user, Pageable pageable) {
        LOGGER.info("Retrieving tickets booked for user: " + user);
        long userId = user.getId();
        List<Ticket> ticketsForUser = new ArrayList<>();
        for (Ticket ticket : getAllTickets()) {
            if (ticket.getUserId() == userId) {
                ticketsForUser.add(ticket);
            }
        }
        return ticketsForUser;
    }

    public List<Ticket> getBooketTickets(Event event, Pageable pageable) {
        LOGGER.info("Retrieving tickets booked for event: " + event);
        long eventId = event.getId();
        return getAllTickets().stream()
                .filter(t -> t.getEventId() == eventId)
                .collect(Collectors.toList());
    }

    public Ticket addTicket(Ticket ticket) {
        LOGGER.info("Adding ticket to in memory db: " + ticket);
        TOTAL_TICKET_COUNT++;
        if (ticket.getId() < TOTAL_TICKET_COUNT) {
            ticket.setId(TOTAL_TICKET_COUNT);
        }
        ticketMap.put(ticket.getId(), ticket);
        return ticket;
    }

    public long getNewTicketId() {
        LOGGER.info("Generating new ticket id: " + TOTAL_TICKET_COUNT);
        return TOTAL_TICKET_COUNT + 1;
    }

    public boolean deleteTicket(long ticketId) {
        LOGGER.info("deleting ticket with id: " + ticketId);
        return ticketMap.remove(ticketId) != null;
    }
}
