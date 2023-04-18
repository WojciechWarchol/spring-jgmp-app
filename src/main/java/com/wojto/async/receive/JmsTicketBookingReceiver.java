package com.wojto.async.receive;

import com.wojto.facade.BookingFacade;
import com.wojto.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JmsTicketBookingReceiver {

    @Autowired
    private BookingFacade bookingFacade;

    @JmsListener(destination = "spring-jgmp-app.ticket.book.queue")
    public void bookTicket(Ticket ticket) {
        bookingFacade.bookTicket(ticket);
    }
}
