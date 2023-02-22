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
        return new ArrayList<>(ticketMap.values());
    }

    public Ticket getTicketById(long TicketId) {
        return ticketMap.get(TicketId);
    }

    public List<Ticket> getBooketTickets(User user, Pageable pageable) {
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
        long eventId = event.getId();
        return getAllTickets().stream()
                .filter(t -> t.getEventId() == eventId)
                .collect(Collectors.toList());
    }

    public Ticket addTicket(Ticket ticket) {
        if (ticket.getId() == 0) {
            ticket.setId(++TOTAL_TICKET_COUNT);
        }
        ticketMap.put(ticket.getId(), ticket);
        return ticket;
    }

    public long getNewTicketId() {
        return TOTAL_TICKET_COUNT;
    }

    public boolean deleteTicket(long TicketId) {
        return ticketMap.remove(TicketId).getId() == TicketId;
    }
}
