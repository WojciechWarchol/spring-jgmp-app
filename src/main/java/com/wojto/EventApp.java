package com.wojto;

import com.wojto.facade.BookingFacadeImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Configuration
@ComponentScan(basePackages = "com.wojto")
@PropertySource("classpath:application.properties")
public class EventApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventApp.class);

    private static ApplicationContext context;

    BookingFacadeImpl bookingFacade;

    public static void main(String[] args) throws ParseException {

        LOGGER.info("Initializing Application context from XML configuration");
        context = new AnnotationConfigApplicationContext(EventApp.class);
        LOGGER.info("Initialized");

        LOGGER.info("Creating BookingFacade and performing autowiring");
        BookingFacadeImpl bookingFacade = context.getBean(BookingFacadeImpl.class);
        LOGGER.info("Initialized BookingFacade with dependencies");
        System.out.println(bookingFacade.getEventById(1).getTitle());
        System.out.println(bookingFacade.getUserById(1).getName());
        System.out.println(bookingFacade.getEventsByTitle("IT", 1, 0));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        System.out.println(bookingFacade.getEventsForDay(dateFormat.parse("13-04-2023"), 1, 0));

        LOGGER.info("Ending Application");
    }

}
