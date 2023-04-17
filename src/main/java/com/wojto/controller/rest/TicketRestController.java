package com.wojto.controller.rest;

import com.itextpdf.text.DocumentException;
import com.wojto.facade.BookingFacade;
import com.wojto.model.Event;
import com.wojto.model.Ticket;
import com.wojto.model.User;
import com.wojto.model.generator.TicketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketRestController {

    private static Logger LOGGER = LoggerFactory.getLogger(TicketRestController.class);

    @Autowired
    BookingFacade bookingFacade;

    @GetMapping("/forUser")
    @ResponseStatus(HttpStatus.OK)
    List<Ticket> getTicketsForUser(@RequestParam("userId") long userId) {
        LOGGER.debug("TicketRestController.getTicketsForUser() method called");
        List<Ticket> ticketList = new ArrayList<>();
        User user = bookingFacade.getUserById(userId);
        List<Ticket> foundTickets = bookingFacade.getBookedTickets(user, 10, 0);

        if (foundTickets != null) ticketList.addAll(foundTickets);

        return ticketList;
    }

    @GetMapping(value = "/forUserInPdf", produces = "application/pdf")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> getPdfForUser(@RequestParam("userId") long userId) throws DocumentException {
        LOGGER.debug("TicketRestController.getPdfForUser() method called");
        List<Ticket> ticketList = new ArrayList<>();
        User user = bookingFacade.getUserById(userId);
        List<Ticket> foundTickets = bookingFacade.getBookedTickets(user, 10, 0);
        HttpHeaders headers = new HttpHeaders();

        if (foundTickets != null) {
            ticketList.addAll(foundTickets);
        } else {
            return new ResponseEntity<>(null, headers, HttpStatus.NO_CONTENT);
        }

        byte[] pdfBytes = TicketUtils.createPdfFromTicketList(ticketList);

        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("inline").filename("tickets.pdf").build());
        headers.setContentLength(pdfBytes.length);
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/forEvent")
    @ResponseStatus(HttpStatus.OK)
    List<Ticket> getTicketsForEvent(@RequestParam("eventId") long eventId) {
        LOGGER.debug("TicketRestController.getTicketsForEvent() method called");
        List<Ticket> ticketList = new ArrayList<>();
        Event event = bookingFacade.getEventById(eventId);
        List<Ticket> foundTickets = bookingFacade.getBookedTickets(event, 10, 0);

        if (foundTickets != null) ticketList.addAll(foundTickets);

        return ticketList;
    }


    @PostMapping("/bookTicket")
    @ResponseStatus(HttpStatus.CREATED)
    Ticket bookTicket(@RequestParam("userId") long userId,
                      @RequestParam("eventId") long eventId,
                      @RequestParam("place") int place,
                      @RequestParam("category") Ticket.Category category) {
        LOGGER.debug("TicketRestController.bookTicket() method called");
        Ticket bookedTicket = bookingFacade.bookTicket(userId, eventId, place, category);
        return bookedTicket;
    }

    @PostMapping("/bookTickets")
    @ResponseStatus(HttpStatus.CREATED)
    List<Ticket> bookTickets(@RequestParam("file") MultipartFile file) throws Exception {
        LOGGER.debug("TicketRestController.bookTickets() method called");

        List<Ticket> ticketList = bookingFacade.bookTicketsFromMultipartFile(file);

        return ticketList;
    }

    @DeleteMapping("/cancelTicket")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<Void> cancelTicket(@RequestParam("ticketId") long ticketId) {
        LOGGER.debug("TicketRestController.deleteTicket() method called");
        boolean ticketDeleted = bookingFacade.cancelTicket(ticketId);
        if (ticketDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
