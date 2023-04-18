package com.wojto.async.receive;

import com.wojto.model.Ticket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.jms.core.JmsTemplate;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
class JmsTicketBookingReceiverTest {

    @SpyBean
    private JmsTicketBookingReceiver jmsTicketBookingReceiver;



//    @MockBean
    @Autowired
    private JmsTemplate jmsTemplate;

    @Test
    void testBookTicket() {
        Ticket ticket = new Ticket(1, 1, Ticket.Category.BAR, 10 );

        jmsTemplate.convertAndSend("spring-jgmp-app.ticket.book.queue", ticket);

        verify(jmsTicketBookingReceiver, timeout(100)).bookTicket(ticket);
    }
}