package com.wojto.async.receive;

import com.wojto.facade.BookingFacade;
import com.wojto.model.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JmsTicketBookingReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(JmsTicketBookingReceiver.class);

    @Autowired
    private BookingFacade bookingFacade;

    @JmsListener(destination = "spring-jgmp-app.ticket.book.queue")
    public void bookTicket(Ticket ticket) {
        LOGGER.info("Recieved message with ticket: " + ticket.toString() + " Calling bookingFacade");
        bookingFacade.bookTicket(ticket);
    }
}
