package com.wojto.dao;

import com.wojto.model.Event;
import com.wojto.model.Ticket;
import com.wojto.model.TicketImpl;
import com.wojto.model.User;
import com.wojto.storage.TicketInMemoryStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InMemoryTicketDaoTest {

    @InjectMocks
    private InMemoryTicketDao inMemoryTicketDao;

    @Mock
    private TicketInMemoryStorage ticketInMemoryStorageMock;
    @Mock
    private User userMock;
    @Mock
    private Event eventMock;

    private Pageable pageable1 = PageRequest.of(0, 10);
    private Pageable pageable2 = PageRequest.of(1, 2);
    private List<Ticket> ticketTestList;
    private Ticket testTicket;

    @BeforeEach
    void setUp() {
        testTicket = new TicketImpl(1, 1, 1, Ticket.Category.STANDARD, 1);
        ticketTestList = new ArrayList<>();
        ticketTestList.add(new TicketImpl(2, 1, 2, Ticket.Category.STANDARD, 2));
        ticketTestList.add(new TicketImpl(3, 2, 1, Ticket.Category.PREMIUM, 1));
        ticketTestList.add(new TicketImpl(4, 2, 2, Ticket.Category.BAR, 2));
    }

    @Test
    void getAllTicketsOnBigPage() {
        when(ticketInMemoryStorageMock.getAllTickets()).thenReturn(ticketTestList);
        Page<Ticket> ticketPage = inMemoryTicketDao.getAllTickets(pageable1);
        assertEquals(ticketTestList, ticketPage.getContent());
    }

    @Test
    void getAllTicketsOnSmallPage() {
        when(ticketInMemoryStorageMock.getAllTickets()).thenReturn(ticketTestList);
        Page<Ticket> ticketPage = inMemoryTicketDao.getAllTickets(pageable2);
        assertEquals(1, ticketPage.getContent().size());
    }

    @Test
    void getTicketById() {
        when(ticketInMemoryStorageMock.getTicketById(anyLong())).thenReturn(testTicket);
        Ticket ticket = inMemoryTicketDao.getTicketById(1);
        assertEquals(testTicket, ticket);
    }

    @Test
    void getBookedTicketsForUser() {
        when(ticketInMemoryStorageMock.getBooketTickets(any(User.class), any(Pageable.class))).thenReturn(ticketTestList);
        Page<Ticket> ticketPage = inMemoryTicketDao.getBookedTickets(userMock, pageable1);
        assertEquals(ticketTestList, ticketPage.getContent());
    }

    @Test
    void getBookedTicketsForEvent() {
        when(ticketInMemoryStorageMock.getBooketTickets(any(Event.class), any(Pageable.class))).thenReturn(ticketTestList);
        Page<Ticket> ticketPage = inMemoryTicketDao.getBookedTickets(eventMock, pageable1);
        assertEquals(ticketTestList, ticketPage.getContent());
    }

    @Test
    void getBookedTicketsForEventId() {
        when(ticketInMemoryStorageMock.getAllTickets()).thenReturn(ticketTestList);
        List<Ticket> ticketPage = inMemoryTicketDao.getBookedTickets(1);
        assertEquals(ticketTestList, ticketTestList);
    }

    @Test
    void bookTicket() {
        when(ticketInMemoryStorageMock.addTicket(any(Ticket.class))).thenReturn(testTicket);
        Ticket ticket = inMemoryTicketDao.bookTicket(testTicket);
        assertEquals(testTicket, ticket);
    }

    @Test
    void deleteTicket() {
        when(ticketInMemoryStorageMock.deleteTicket(anyLong())).thenReturn(true);
        boolean ticketDeleted = inMemoryTicketDao.deleteTicket(1);
        assertTrue(ticketDeleted);
    }
}