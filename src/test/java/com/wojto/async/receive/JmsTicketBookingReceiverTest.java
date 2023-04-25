package com.wojto.async.receive;

import com.wojto.facade.BookingFacade;
import com.wojto.model.Ticket;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.jms.core.JmsTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Testcontainers
class JmsTicketBookingReceiverTest {

    public static final String ARTEMIS_IMAGE = "vromero/activemq-artemis";
    public static final int ARTEMIS_PORT = 61616;

    @Autowired
    @InjectMocks
    private JmsTicketBookingReceiver jmsTicketBookingReceiver;

    @Container
    private static final GenericContainer artemis = new GenericContainer(ARTEMIS_IMAGE)
            .withExposedPorts(ARTEMIS_PORT);

    @SpyBean
    BookingFacade bookingFacade;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Test
    void testBookTicket() {
        Ticket ticket = new Ticket(1, 1, Ticket.Category.BAR, 10 );

        jmsTemplate.convertAndSend("spring-jgmp-app.ticket.book.queue", ticket);

        verify(bookingFacade, timeout(100)).bookTicket(ticket);
    }
}