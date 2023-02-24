package com.wojto;

import com.wojto.facade.BookingFacadeImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EventApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventApp.class);

    private static ApplicationContext context;

    public static void main(String[] args) throws ParseException {

        LOGGER.info("Initializing Application context from XML configuration");
        context = new ClassPathXmlApplicationContext("file:src/main/java/ApplicationContext.xml");
        LOGGER.info("Initialized");

        LOGGER.info("Creating BookingFacade and performing autowiring");
        BookingFacadeImpl bookingFacade = (BookingFacadeImpl) context.getBean("bookingFacade");
        LOGGER.info("Initialized BookingFacade with dependencies");
        System.out.println(bookingFacade.getEventById(1).getTitle());
        System.out.println(bookingFacade.getUserById(1).getName());
        System.out.println(bookingFacade.getEventsByTitle("IT", 1, 0));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        System.out.println(bookingFacade.getEventsForDay(dateFormat.parse("13-04-2023"), 1, 0));

        LOGGER.info("Ending Application");
    }

}
