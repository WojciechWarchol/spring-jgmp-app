package com.wojto;

import com.wojto.model.Event;
import com.wojto.model.EventImpl;
import com.wojto.model.Ticket;
import com.wojto.service.EventService;
import com.wojto.service.TicketService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EventApp {

    private static ApplicationContext context;

    public static void main(String[] args) {
        context = new ClassPathXmlApplicationContext("file:src/main/java/ApplicationContext.xml");

        EventService eventService = (EventService) context.getBean("eventService");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Event event;
        try {
            event = eventService.createEvent(new EventImpl(1, "Music Event", dateFormat.parse("01-03-2023")));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        System.out.println(eventService.findEventById(1).getTitle());

        System.out.println(eventService.getEventDao() != null);

        TicketService ticketService = (TicketService) context.getBean("ticketService");

        ticketService.bookTicket(1, 1, 1, Ticket.Category.STANDARD);
        System.out.println(ticketService.getBooketTickets(event, 1, 0));
    }

}
