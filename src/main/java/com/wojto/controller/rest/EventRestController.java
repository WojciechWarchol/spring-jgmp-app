package com.wojto.controller.rest;

import com.wojto.facade.BookingFacade;
import com.wojto.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventRestController {

    private static Logger LOGGER = LoggerFactory.getLogger(EventRestController.class);

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    BookingFacade bookingFacade;

    @GetMapping("/byId")
    @ResponseStatus(HttpStatus.OK)
    List<Event> getEventById(@RequestParam("eventId") long eventId) {
        LOGGER.debug("EventRestController.getEventById() method called");
        List<Event> eventList = new ArrayList<>();
        Event event = bookingFacade.getEventById(eventId);

        if (event != null) eventList.add(event);

        return eventList;
    }

    @GetMapping("/byTitle")
    @ResponseStatus(HttpStatus.OK)
    List<Event> getEventsByTitle(@RequestParam("title") String title) {
        LOGGER.debug("EventRestController.getEventsByTitle() method called");
        List<Event> eventList = new ArrayList<>();
        List<Event> foundEvents = bookingFacade.getEventsByTitle(title, 10, 0);

        if (foundEvents != null) eventList.addAll(foundEvents);

        return eventList;
    }

    @GetMapping("/forDay")
    @ResponseStatus(HttpStatus.OK)
    List<Event> getEventsForDay(@RequestParam("day") String day) throws ParseException {
        LOGGER.debug("EventRestController.getEventsForDay() method called");
        List<Event> eventList = new ArrayList<>();
        Date date = dateFormat.parse(day);
        List<Event> foundEvents = bookingFacade.getEventsForDay(date, 10, 0);

        if (foundEvents != null) eventList.addAll(foundEvents);

        return eventList;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Event createEvent(@RequestBody Event event) {
        LOGGER.debug("EventRestController.createEvent() method called");
        Event createdEvent = bookingFacade.createEvent(event);
        return createdEvent;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    Event updateEvent(@RequestBody Event event) {
        LOGGER.debug("EventRestController.updateEvent() method called");
        Event updatedEvent = bookingFacade.updateEvent(event);
        return updatedEvent;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<Void> deleteEvent(@RequestParam("eventId") long eventId) {
        LOGGER.debug("EventRestController.deleteEvent() method called");
        boolean eventDeleted = bookingFacade.deleteEvent(eventId);
        if (eventDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
