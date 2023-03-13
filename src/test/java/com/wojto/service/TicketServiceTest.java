package com.wojto.service;

import com.wojto.dao.DBTicketRepository;
import com.wojto.model.Event;
import com.wojto.model.Ticket;
import com.wojto.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private DBTicketRepository dbTicketRepository;
    @Mock
    private List<Ticket> ticketListMock;
    @Mock
    private Page<Ticket> ticketPageMock;
    @Mock
    private Ticket ticketMock;
    @Mock
    private User userMock;
    @Mock
    private Event eventMock;


    @Test
    void findAllTickets() {
        when(dbTicketRepository.getAllTickets(any())).thenReturn(ticketPageMock);
        when(ticketPageMock.getContent()).thenReturn(ticketListMock);
        List<Ticket> ticketList = ticketService.findAllTickets(4,0);
        assertNotNull(ticketList);
    }

    @Test
    void findTicketById() {
        when(dbTicketRepository.getTicketById(anyLong())).thenReturn(ticketMock);
        Ticket ticket = ticketService.findTicketById(6);
        assertNotNull(ticket);
    }

    @Test
    void getBooketTicketsForUser() {
        when(dbTicketRepository.getBookedTickets(any(User.class),any())).thenReturn(ticketPageMock);
        when(ticketPageMock.getContent()).thenReturn(ticketListMock);
        List<Ticket> ticketList = ticketService.getBooketTickets(userMock, 7, 1);
        assertNotNull(ticketList);
    }

    @Test
    void GetBooketTicketsForEvent() {
        when(dbTicketRepository.getBookedTickets(any(Event.class),any())).thenReturn(ticketPageMock);
        when(ticketPageMock.getContent()).thenReturn(ticketListMock);
        List<Ticket> ticketList = ticketService.getBooketTickets(eventMock, 2, 3);
        assertNotNull(ticketList);
    }

    @Test
    void bookTicket() {
        when(dbTicketRepository.bookTicket(any(Ticket.class))).thenReturn(ticketMock);
        Ticket ticket = ticketService.bookTicket(6, 6, 6, Ticket.Category.BAR);
        assertNotNull(ticket);
    }

    @Test
    void cancelTicket() {
        when(dbTicketRepository.deleteTicket(anyLong())).thenReturn(true);
        boolean ticketDeleted = ticketService.cancelTicket(4);
        assertTrue(ticketDeleted);
    }
}