package com.wojto.facade;

import com.wojto.exception.TicketXmlUnmarshallingException;
import com.wojto.model.Event;
import com.wojto.model.Ticket;
import com.wojto.model.User;
import com.wojto.model.UserAccount;
import com.wojto.model.generator.TicketUtils;
import com.wojto.service.EventService;
import com.wojto.service.TicketService;
import com.wojto.service.UserAccountService;
import com.wojto.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
public class BookingFacadeImpl implements BookingFacade{

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingFacadeImpl.class);

    @Autowired
    EventService eventService;
    @Autowired
    UserService userService;
    @Autowired
    TicketService ticketService;
    @Autowired
    UserAccountService userAccountService;
    @Autowired
    PlatformTransactionManager transactionManager;

    public BookingFacadeImpl(EventService eventService, UserService userService, TicketService ticketService) {
        this.eventService = eventService;
        this.userService = userService;
        this.ticketService = ticketService;
    }

    @Override
    public Event getEventById(long eventId) {
        LOGGER.info("Calling EventService for event with id: " + eventId);
        return eventService.findEventById(eventId);
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        LOGGER.info(String.format("Calling EventService for events containing \"%s\" in title with page size/num: %d/%d", title, pageSize, pageNum));
        return eventService.findEventsByTitle(title, pageSize, pageNum);
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        LOGGER.info(String.format("Calling EventService for events on day \"%s\" in title with page size/num: %d/%d", day, pageSize, pageNum));
        return eventService.getEventsForDay(day, pageSize, pageNum);
    }

    @Override
    public Event createEvent(Event event) {
        LOGGER.info("Calling EventService to create event: " + event);
        return eventService.createEvent(event);
    }

    @Override
    public Event updateEvent(Event event) {
        LOGGER.info("Calling EventService to update event: " + event);
        return eventService.updateEvent(event);
    }

    @Override
    public boolean deleteEvent(long eventId) {
        LOGGER.info("Calling EventService to delete event with id,: " + eventId);
        return eventService.deleteEvent(eventId);
    }

    @Override
    public User getUserById(long userId) {
        LOGGER.info("Calling UserService for user with id: " + userId);
        return userService.getUserById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        LOGGER.info("Calling UserService for user with email: " + email);
        return userService.getUserByEmail(email);
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        LOGGER.info(String.format("Calling UserService for user with name containing \"%s\" with page size/num: %d/%d", name, pageSize, pageNum));
        return userService.getUsersByName(name, pageSize, pageNum);
    }

    @Override
    @Transactional
    public User createUser(User user) {
        LOGGER.info("Calling UserService to create user: " + user);
        user = userService.createUser(user);
        LOGGER.info("Calling UserAccountService to create account for user.");
        UserAccount userAccount = new UserAccount(user.getId(), user.getId());
        userAccountService.createUserAccount(userAccount);
        return user;
    }

    @Override
    public User updateUser(User user) {
        LOGGER.info("Calling UserService to update user: " + user);
        return userService.updateUser(user);
    }

    @Override
    public boolean deleteUser(long userId) {
        LOGGER.info("Calling UserService to delete user id: " + userId);
        return userService.deleteUser(userId);
    }

    @Override
    public Ticket bookTicket(Ticket ticket) {
        LOGGER.info(String.format("Calling bookTicket method with ticket: " + ticket.toString()));
        return bookTicket(ticket.getUserId(), ticket.getEventId(), ticket.getPlace(), ticket.getCategory());
    }

    @Override
    @Transactional
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
            LOGGER.info(
                    String.format("Calling Event Service, UserAccount Service and TicketService to book ticket with userId: %d, eventId: %d, place: %d, category: %s",
                            userId, eventId, place, category));
            LOGGER.info("Checking ticket price for event: " + eventId);
            Event event = eventService.findEventById(eventId);
            BigDecimal ticketPrice = event.getTicketPrice();
            LOGGER.info("Attempting to deduct payment for ticket: " + ticketPrice + " from user: " + userId);
            boolean fundDeductionSuccessful = userAccountService.deductFundsFromAccount(userId, ticketPrice);
            if (fundDeductionSuccessful) {
                LOGGER.info("Proceeding to book ticket after successfully deducting payment");
                Ticket bookedTicket = ticketService.bookTicket(userId, eventId, place, category);
                return bookedTicket;
            } else {
                LOGGER.error("Payment deduction failed. Performing rollback");
                throw new IllegalStateException("Payment deduction failed!");
            }
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        LOGGER.info(String.format("Calling TicketService for booked tickets for user: %s with page size/num: %d/%d", user, pageSize, pageNum));
        return ticketService.getBooketTickets(user, pageSize, pageNum);
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        LOGGER.info(String.format("Calling TicketService for booked tickets for event: %s with page size/num: %d/%d", event, pageSize, pageNum));
        return ticketService.getBooketTickets(event, pageSize, pageNum);
    }

    @Override
    @Transactional
    public boolean cancelTicket(long ticketId) {
        LOGGER.info("Calling TicketService to for ticket to cancel");
        Ticket ticket = ticketService.findTicketById(ticketId);
        LOGGER.info("Calling TicketService to cancel ticket with id: " + ticketId);
        ticketService.cancelTicket(ticketId);
        LOGGER.info("Calling EventService to check for amount to be refunded to user.");
        Event event = eventService.findEventById(ticket.getEventId());
        BigDecimal refundAmount = event.getTicketPrice();
        LOGGER.info("Calling UserAccount Service to refund ticket cost tu user.");
        userAccountService.topUpUserAccount(ticket.getUserId(), refundAmount);
        LOGGER.info("Ticket refund finished.");
        return true;
    }

    @Override
    public UserAccount getUserAccountById(long accountId) {
        LOGGER.info("Calling UserAccountService for user account with id: " + accountId);
        return userAccountService.getUserAccountById(accountId);
    }

    @Override
    public UserAccount getUserAccountByUserId(long userId) {
        LOGGER.info("Calling UserAccountService for user account for user id: " + userId);
        return userAccountService.getUserAccountByUserId(userId);
    }

    @Override
    public UserAccount topUpUserAccount(long userId, BigDecimal amount) {
        LOGGER.info("Calling UserAccountService to top up account of user: " + userId + " in the ammount of: " + amount);
        return userAccountService.topUpUserAccount(userId, amount);
    }

    public List<Ticket> bookTicketsFromMultipartFile(MultipartFile file) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        List<Ticket> ticketList = new ArrayList<>();
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                List<Ticket> unmarshalledTickets = null;
                try {
                    unmarshalledTickets = TicketUtils.createTicketListFromMultipartFile(file);
                    ticketList.addAll(unmarshalledTickets.stream()
                            .map(ticket -> {
                                long userId = ticket.getUserId();
                                long eventId = ticket.getEventId();
                                int place = ticket.getPlace();
                                Ticket.Category category = ticket.getCategory();
                                return bookTicket(userId, eventId, place, category);
                            })
                            .collect(Collectors.toList()));
                } catch (Exception e) {
                    String errorMessage = "Error during XML Unmarshalling";
                    LOGGER.error(errorMessage);
                    throw new TicketXmlUnmarshallingException(errorMessage);
                }

            }
        });

        return ticketList;
    }
}
