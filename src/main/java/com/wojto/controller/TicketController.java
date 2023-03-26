package com.wojto.controller;

import com.wojto.facade.BookingFacade;
import com.wojto.model.Event;
import com.wojto.model.Ticket;
import com.wojto.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private static Logger LOGGER = LoggerFactory.getLogger(TicketController.class);

    @Autowired
    BookingFacade bookingFacade;

    @GetMapping("/forUser")
    String getTicketsForUser(@RequestParam("userId") long userId, Model model) {
        LOGGER.info("TicketController.getTicketsForUser() method called");
        User user = bookingFacade.getUserById(userId);
        List<Ticket> ticketList = bookingFacade.getBookedTickets(user, 10, 0);
        model.addAttribute("ticketList", ticketList);
        return "showTickets";
    }

    @GetMapping("/forEvent")
    String getTicketsForEvent(@RequestParam("eventId") long eventId, Model model){
        LOGGER.info("TicketController.getTicketsForEvent() method called");
        Event event = bookingFacade.getEventById(eventId);
        List<Ticket> ticketList = bookingFacade.getBookedTickets(event, 10, 0);
        model.addAttribute("ticketList", ticketList);
        return "showTickets";
    }

    @GetMapping("/bookTicketForm")
    String goToBookTicketForm() {
        return "bookTicket";
    }

    @PostMapping("/bookTicket")
    String bookTicket(@RequestParam("userId") long userId,
                      @RequestParam("eventId") long eventId,
                      @RequestParam("place") int place,
                      @RequestParam("category") Ticket.Category category) {
        Ticket ticket = bookingFacade.bookTicket(userId, eventId, place, category);
        return "index";
    }

    @PostMapping("/cancelTicket")
    String cancelTicket(@RequestParam("ticketId") long ticketId) {
        LOGGER.info("TicketController.deleteTicket() method called");
        boolean ticketDeleted = bookingFacade.cancelTicket(ticketId);
        // TODO Probably attach a "successful delete to the model
        return "index";
    }
}
