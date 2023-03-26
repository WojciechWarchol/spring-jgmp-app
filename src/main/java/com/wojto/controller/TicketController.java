package com.wojto.controller;

import com.itextpdf.text.DocumentException;
import com.wojto.facade.BookingFacade;
import com.wojto.model.Event;
import com.wojto.model.Ticket;
import com.wojto.model.User;
import com.wojto.model.generator.TicketPdfGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
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

    @GetMapping(value = "/forUser", produces = "application/pdf")
    public ResponseEntity<byte[]> getPdfForUser(@RequestParam("userId") long userId) throws IOException, DocumentException {
        LOGGER.info("TicketController.getPdfForUser() method called");
        User user = bookingFacade.getUserById(userId);
        List<Ticket> ticketList = bookingFacade.getBookedTickets(user, 10, 0);

        byte[] pdfBytes = TicketPdfGenerator.generatePdf(ticketList);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("inline").filename("tickets.pdf").build());
        headers.setContentLength(pdfBytes.length);
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
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
