package com.wojto.controller;

import com.wojto.facade.BookingFacade;
import com.wojto.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    @ResponseStatus(HttpStatus.OK)
    String getEventById(@RequestParam("eventId") long eventId, Model model) {
        LOGGER.debug("EventController.getEventById() method called");
        List<Event> eventList = new ArrayList<>();
        Event event = bookingFacade.getEventById(eventId);

        if (event != null) eventList.add(event);
        model.addAttribute("eventList", eventList);

        return "showEvents";
    }

    @GetMapping("/byTitle")
    @ResponseStatus(HttpStatus.OK)
    String getEventsByTitle(@RequestParam("title") String title, Model model) {
        LOGGER.debug("EventController.getEventsByTitle() method called");
        List<Event> eventList = new ArrayList<>();
        List<Event> foundEvents = bookingFacade.getEventsByTitle(title, 10, 0);

        if (foundEvents != null) eventList.addAll(foundEvents);
        model.addAttribute("eventList", eventList);

        return "showEvents";
    }

    @GetMapping("/forDay")
    @ResponseStatus(HttpStatus.OK)
    String getEventsForDay(@RequestParam("day") String day, Model model) throws ParseException {
        LOGGER.debug("EventController.getEventsForDay() method called");
        List<Event> eventList = new ArrayList<>();
        Date date = dateFormat.parse(day);
        List<Event> foundEvents = bookingFacade.getEventsForDay(date, 10, 0);

        if (foundEvents != null) eventList.addAll(foundEvents);
        model.addAttribute("eventList", eventList);

        return "showEvents";
    }

    @GetMapping("/createEventForm")
    String goToCreateEventForm() {
        LOGGER.debug("EventController.goToCreateEventForm() method called");
        return "createEvent";
    }

    @PostMapping("/createEvent")
    @ResponseStatus(HttpStatus.CREATED)
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

    @PutMapping("/updateEvent")
    @ResponseStatus(HttpStatus.CREATED)
    String updateEvent(@RequestParam("id") long id,
                       @RequestParam("title") String title,
                       @RequestParam("day") String day,
                       @RequestParam("ticketPrice") BigDecimal ticketPrice) throws ParseException {
        LOGGER.debug("EventController.updateEvent() method called");
        bookingFacade.updateEvent(new Event(id, title, dateFormat.parse(day), ticketPrice));
        return "index";
    }

    @DeleteMapping("/deleteEvent")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    String deleteEvent(@RequestParam("eventId") long eventId) {
        LOGGER.debug("EventController.deleteEvent() method called");
        boolean eventDeleted = bookingFacade.deleteEvent(eventId);
        return "index";
    }
}
