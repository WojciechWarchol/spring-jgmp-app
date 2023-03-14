package com.wojto.storage;

import com.wojto.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TicketInMemoryStorageTest {

    private TicketInMemoryStorage ticketInMemoryStorage;
    private Ticket ticket1;
    private Ticket ticket2;
    private Ticket ticket3;
    private Event event;
    private User user;
    private Pageable pageable = PageRequest.of(0, 10);

    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        ticket1 = new Ticket(1, 1, 1, Ticket.Category.STANDARD, 1);
        ticket2 = new Ticket(2, 1, 2, Ticket.Category.PREMIUM, 2);
        ticket3 = new Ticket(3, 2, 1, Ticket.Category.BAR, 1);
        try {
            event = new Event(1, "Music Event", dateFormat.parse("01-01-2023"), BigDecimal.valueOf(50.00));
        } catch (ParseException e) {
            System.out.println("Error in parsing dates for test events.");
        }
        user = new User(1, "Jan Kowalski", "j.k@gmail.com");
    }

    @BeforeEach
    void setUp() {
        ticketInMemoryStorage = new TicketInMemoryStorage();
        ticketInMemoryStorage.addTicket(ticket1);
        ticketInMemoryStorage.addTicket(ticket2);
        ticketInMemoryStorage.addTicket(ticket3);
    }

    @Test
    void getAllTickets() {
        int numberOfElements = ticketInMemoryStorage.getAllTickets().size();
        assertEquals(3, numberOfElements);
    }

    @Test
    void getTicketById() {
        Ticket foundTicket = ticketInMemoryStorage.getTicketById(3);
        assertEquals(ticket3, foundTicket);
    }

    @Test
    void getBooketTicketsForUser() {
        List<Ticket> ticketList = ticketInMemoryStorage.getBooketTickets(user, pageable);
        assertEquals(2, ticketList.size());
        assertTrue(ticketList.contains(ticket1));
        assertTrue(ticketList.contains(ticket3));
    }

    @Test
    void getBooketTicketsForEvent() {
        List<Ticket> ticketList = ticketInMemoryStorage.getBooketTickets(event, pageable);
        assertEquals(2, ticketList.size());
        assertTrue(ticketList.contains(ticket1));
        assertTrue(ticketList.contains(ticket2));
    }

    @Test
    void addTicket() {
        Ticket newTicket = ticketInMemoryStorage.addTicket(new Ticket(4, 2, 2, Ticket.Category.STANDARD, 3));
        assertEquals(4, ticketInMemoryStorage.getAllTickets().size());
        assertEquals(newTicket, ticketInMemoryStorage.getTicketById(4));
    }

    @Test
    void getNewTicketId() {
        long newTicketId = ticketInMemoryStorage.getNewTicketId();
        assertEquals(4, newTicketId);
    }

    @Test
    void deleteTicket() {
        boolean ticketDeleted = ticketInMemoryStorage.deleteTicket(1);
        List<Ticket> foundTickets = ticketInMemoryStorage.getAllTickets();
        assertTrue(ticketDeleted);
        assertEquals(2, foundTickets.size());
        assertFalse(foundTickets.contains(ticket1));
    }

    @Test
    void noTicketDeletedWhenProvidedIdDoesntExist() {
        boolean ticketDeleted = ticketInMemoryStorage.deleteTicket(5);
        List<Ticket> foundTickets = ticketInMemoryStorage.getAllTickets();
        assertFalse(ticketDeleted);
        assertEquals(3, foundTickets.size());
    }
}