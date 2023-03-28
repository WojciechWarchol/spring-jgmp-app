package com.wojto.controller;

import com.wojto.facade.BookingFacade;
import com.wojto.model.Event;
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
@RequestMapping("/events")
public class EventController {

    private static Logger LOGGER = LoggerFactory.getLogger(EventController.class);

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    BookingFacade bookingFacade;

    @GetMapping("/byId")
    String getEventById(@RequestParam("eventId") long eventId, Model model) {
        LOGGER.debug("EventController.getEventById() method called");
        Event event = bookingFacade.getEventById(eventId);
        List<Event> eventList = Arrays.asList(event);
        model.addAttribute("eventList", eventList);
        return "showEvents";
    }

    @GetMapping("/byTitle")
    String getEventsByTitle(@RequestParam("title") String title, Model model) {
        LOGGER.debug("EventController.getEventsByTitle() method called");
        List<Event> eventList = bookingFacade.getEventsByTitle(title, 10, 0);
        model.addAttribute("eventList", eventList);
        return "showEvents";
    }

    @GetMapping("/forDay")
    String getEventsForDay(@RequestParam("day") String day, Model model) throws ParseException {
        LOGGER.debug("EventController.getEventsForDay() method called");
        Date date = dateFormat.parse(day);
        List<Event> eventList = bookingFacade.getEventsForDay(date, 10, 0);
        model.addAttribute("eventList", eventList);
        return "showEvents";
    }

    @GetMapping("/createEventForm")
    String goToCreateEventForm() {
        LOGGER.debug("EventController.goToCreateEventForm() method called");
        return "createEvent";
    }

    @PostMapping("/createEvent")
    String createEvent(@RequestParam("title") String title, @RequestParam("day") String day, @RequestParam("ticketPrice") BigDecimal ticketPrice) throws ParseException {
        LOGGER.debug("EventController.createEvent() method called");
        Event event = bookingFacade.createEvent(
                new Event(title, dateFormat.parse(day), ticketPrice));
        return "index";
    }

    @GetMapping("/updateEventForm")
    public String goToEditedEventForm(@RequestParam("eventId") Long eventId, Model model) {
        LOGGER.debug("EventController.goToEditedEventForm() method called");
        Event event = bookingFacade.getEventById(eventId);
        model.addAttribute("event", event);
        return "createEvent";
    }

    @PostMapping("/updateEvent")
    String updateEvent(@RequestParam("id") long id,
                       @RequestParam("title") String title,
                       @RequestParam("day") String day,
                       @RequestParam("ticketPrice") BigDecimal ticketPrice) throws ParseException {
        LOGGER.debug("EventController.updateEvent() method called");
        Event event = bookingFacade.getEventById(id);

        event.setTitle(title);
        event.setDate(dateFormat.parse(day));
        event.setTicketPrice(ticketPrice);

        bookingFacade.updateEvent(event);

        return "index";
    }

    @PostMapping("/deleteEvent")
    String deleteEvent(@RequestParam("eventId") long eventId) {
        LOGGER.debug("EventController.deleteEvent() method called");
        boolean eventDeleted = bookingFacade.deleteEvent(eventId);
        // TODO Probably attach a "successful delete to the model
        return "index";
    }
}
