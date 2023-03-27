package com.wojto.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private static Logger LOGGER = LoggerFactory.getLogger(TicketController.class);

    @Autowired
    BookingFacade bookingFacade;

    @Autowired
    PlatformTransactionManager transactionManager;

    @GetMapping("/forUser")
    String getTicketsForUser(@RequestParam("userId") long userId, Model model) {
        LOGGER.debug("TicketController.getTicketsForUser() method called");
        User user = bookingFacade.getUserById(userId);
        List<Ticket> ticketList = bookingFacade.getBookedTickets(user, 10, 0);
        model.addAttribute("ticketList", ticketList);
        return "showTickets";
    }

    @GetMapping(value = "/forUser", produces = "application/pdf")
    public ResponseEntity<byte[]> getPdfForUser(@RequestParam("userId") long userId) throws IOException, DocumentException {
        LOGGER.debug("TicketController.getPdfForUser() method called");
        User user = bookingFacade.getUserById(userId);
        List<Ticket> ticketList = bookingFacade.getBookedTickets(user, 10, 0);

        byte[] pdfBytes = TicketUtils.createPdfFromTicketList(ticketList);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("inline").filename("tickets.pdf").build());
        headers.setContentLength(pdfBytes.length);
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/forEvent")
    String getTicketsForEvent(@RequestParam("eventId") long eventId, Model model) {
        LOGGER.debug("TicketController.getTicketsForEvent() method called");
        Event event = bookingFacade.getEventById(eventId);
        List<Ticket> ticketList = bookingFacade.getBookedTickets(event, 10, 0);
        model.addAttribute("ticketList", ticketList);
        return "showTickets";
    }

    @GetMapping("/bookTicketForm")
    String goToBookTicketForm() {
        LOGGER.debug("TicketController.goToBookTicketForm() method called");
        return "bookTicket";
    }

    @PostMapping("/bookTicket")
    String bookTicket(@RequestParam("userId") long userId,
                      @RequestParam("eventId") long eventId,
                      @RequestParam("place") int place,
                      @RequestParam("category") Ticket.Category category) {
        LOGGER.debug("TicketController.bookTicket() method called");
        Ticket ticket = bookingFacade.bookTicket(userId, eventId, place, category);
        return "index";
    }

    @PostMapping("/cancelTicket")
    String cancelTicket(@RequestParam("ticketId") long ticketId) {
        LOGGER.debug("TicketController.deleteTicket() method called");
        boolean ticketDeleted = bookingFacade.cancelTicket(ticketId);
        // TODO Probably attach a "successful delete to the model
        return "index";
    }

    @PostMapping("/bookTickets")
    String bookTickets(@RequestParam("file") MultipartFile file, Model model) throws Exception {
        LOGGER.debug("TicketController.bookTickets() method called");
        List<Ticket> ticketList = new ArrayList<>();

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                List<Ticket> unmarshalledTickets = null;
                try {
                    unmarshalledTickets = TicketUtils.createTicketListFromMultipartFile(file);
                } catch (Exception e) {
                    LOGGER.error("Error during XML Unmarshalling");
                    throw new RuntimeException(e);
                }
                for (Ticket ticket : unmarshalledTickets) {
                    long userId = ticket.getUserId();
                    long eventId = ticket.getEventId();
                    int place = ticket.getPlace();
                    Ticket.Category category = ticket.getCategory();
                    Ticket bookedTicket = bookingFacade.bookTicket(userId, eventId, place, category);
                    ticketList.add(bookedTicket);
                }
            }
        });

        model.addAttribute("ticketList", ticketList);
        return "showTickets";
    }

}
