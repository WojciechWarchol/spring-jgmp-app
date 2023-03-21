package com.wojto.facade;

import com.wojto.EventAppConfig;
import com.wojto.model.Event;
import com.wojto.model.Ticket;
import com.wojto.model.User;
import com.wojto.model.UserAccount;
import net.sf.ehcache.CacheManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookingFacadeTest {

    @Autowired
    private BookingFacadeImpl bookingFacade;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @BeforeEach
    void setUp() {
        ApplicationContext context = new AnnotationConfigApplicationContext(EventAppConfig.class);
        bookingFacade = context.getBean(BookingFacadeImpl.class);
    }

    @AfterEach
    void cleanup() {
        CacheManager.getInstance().getCache("eventCache").removeAll();
        CacheManager.getInstance().getCache("userCache").removeAll();
        CacheManager.getInstance().getCache("userAccountCache").removeAll();
    }

    @Test
    void getEventById() {
        Event event = bookingFacade.getEventById(1);
        assertEquals("Music Event", event.getTitle());
    }

    @Test
    void attemptToGetNonExistingEventById() {
        Event event = bookingFacade.getEventById(99);
        assertNull(event);
    }

    @Test
    void getManyEventsByTitle() {
        List<Event> eventList = bookingFacade.getEventsByTitle("Event", 3, 0);
        assertEquals(3, eventList.size());
    }

    @Test
    void getOneEventByTitle() {
        List<Event> eventList = bookingFacade.getEventsByTitle("Culinary Event", 2, 0);
        assertEquals("Culinary Event", eventList.get(0).getTitle());
        assertEquals(1, eventList.size());
    }

    @Test
    void getEventsByTitlePaginationTest() throws ParseException {
        Event event = new Event(1l, "Music Event", dateFormat.parse("01-01-2023"), BigDecimal.valueOf(50.00).setScale(2));
        List<Event> eventList = bookingFacade.getEventsByTitle("Event", 1, 0);
        Event eventFromList = eventList.get(0);

        assertEquals(1, eventList.size());
        assertEquals(event.getId(), eventFromList.getId());
        assertEquals(event.getTitle(), eventFromList.getTitle());
        assertEquals(event.getDate(), eventFromList.getDate());
        assertEquals(event.getTicketPrice(), eventFromList.getTicketPrice());
    }

    @Test
    void getEventsForDay() throws ParseException {
        List<Event> eventList = bookingFacade.getEventsForDay(dateFormat.parse("13-04-2023"), 2, 0);
        assertEquals(2, eventList.size());
    }

    @Test
    void getNoEventsForDayWithNone() throws ParseException {
        List<Event> eventList = bookingFacade.getEventsForDay(dateFormat.parse("30-03-2023"), 3, 0);
        assertTrue(eventList.isEmpty());
    }

    @Test
    void createEvent() throws ParseException {
        Event newEvent = new Event(4, "New Event", dateFormat.parse("15-05-2023"), BigDecimal.valueOf(10.00).setScale(2));
        bookingFacade.createEvent(newEvent);
        Event polledNewEvent = bookingFacade.getEventById(4);

        assertEquals(newEvent.getId(), polledNewEvent.getId());
        assertEquals(newEvent.getTitle(), polledNewEvent.getTitle());
        assertEquals(newEvent.getDate(), polledNewEvent.getDate());
        assertEquals(newEvent.getTicketPrice(), polledNewEvent.getTicketPrice());
    }

    @Test
    void updateEvent() throws ParseException {
        Event updatedEvent = new Event(1, "Updated Event", dateFormat.parse("06-06-2023"), BigDecimal.valueOf(33.00).setScale(2));
        bookingFacade.updateEvent(updatedEvent);
        Event polledUpdatedEvent = bookingFacade.getEventById(1);

        assertEquals(updatedEvent.getId(), polledUpdatedEvent.getId());
        assertEquals(updatedEvent.getTitle(), polledUpdatedEvent.getTitle());
        assertEquals(updatedEvent.getDate(), polledUpdatedEvent.getDate());
        assertEquals(updatedEvent.getTicketPrice(), polledUpdatedEvent.getTicketPrice());
    }

    @Test
    void deleteExistingEventAndReturnTrue() {
        boolean eventWasDeleted = bookingFacade.deleteEvent(1);
        Event event = bookingFacade.getEventById(1);

        assertTrue(eventWasDeleted);
        assertNull(event);
    }

    @Test
    void returnFalseWhenDeletingNonExistingEvent() {
        boolean eventWasDeleted = bookingFacade.deleteEvent(99);
        assertFalse(eventWasDeleted);
    }

    @Test
    void getUserById() {
        User user = bookingFacade.getUserById(2);
        assertEquals("Grzegorz Brzeczyszczykiewicz", user.getName());
    }

    @Test
    void getNullIfNoUserForId() {
        User user = bookingFacade.getUserById(99);
        assertNull(user);
    }

    @Test
    void getUserByEmail() {
        User user = bookingFacade.getUserByEmail("victor.wiedenski@gmail.com");
        assertEquals("victor.wiedenski@gmail.com", user.getEmail());
    }

    @Test
    void getUsersByName() {
        List<User> userList = bookingFacade.getUsersByName("Jan",3, 0);
        assertEquals(2, userList.size());
    }

    @Test
    void createUser() {
        User newUser = new User(7 , "Zbyszek Zbyszkowski", "zbychu@gmail.com");
        bookingFacade.createUser(newUser);
        User polledNewUser = bookingFacade.getUserByEmail("zbychu@gmail.com");

        assertEquals(newUser, polledNewUser);
    }

    @Test
    void updateUser() {
        User updatedUser = new User(1 , "Zbyszek Zbyszkowski", "zbychu@gmail.com");
        bookingFacade.updateUser(updatedUser);
        User polledNewUser = bookingFacade.getUserById(1);

        assertEquals(updatedUser, polledNewUser);
    }

    @Test
    void deleteUserAndReturnTrue() {
        boolean userDeleted = bookingFacade.deleteUser(2);
        User user = bookingFacade.getUserById(2);

        assertTrue(userDeleted);
        assertNull(user);
    }

    @Test
    void getFalseWhenDeletingNonExistingUser() {
        boolean userDeleted = bookingFacade.deleteUser(99);
        assertFalse(userDeleted);
    }

    @Test
    void bookTicket() {
        long userId = 1, eventId = 3;
        int place = 4;
        Ticket.Category category = Ticket.Category.BAR;
        Ticket ticket = bookingFacade.bookTicket(userId, eventId, place, category);

        assertEquals(userId, ticket.getUserId());
        assertEquals(eventId, ticket.getEventId());
        assertEquals(place, ticket.getPlace());
        assertEquals(category, ticket.getCategory());

        List<Ticket> ticketsForUser = bookingFacade.getBookedTickets(
                new User(1l, "Jozef Malolepszy", "jozef.malolepszy@gmail.com"),10,0);
        assertTrue(ticketsForUser.contains(ticket));
    }

    @Test
    void ThrowErrorWhenAttemptingToBookTicketForBookedPlace() {
        long userId = 2, eventId = 1;
        int place = 1;
        Ticket.Category category = Ticket.Category.STANDARD;

        assertThrows(IllegalStateException.class, () -> {
            Ticket ticket = bookingFacade.bookTicket(userId, eventId, place, category);
        }, "Illegal State Exception was expected");

//        BigDecimal userAccountBalanceBeforeTicketReservation = bookingFacade.userAccountService.getUserAccountByUserId(2).getFunds();
//        assertEquals(BigDecimal.valueOf(200.00).setScale(2), userAccountBalanceBeforeTicketReservation);
    }

    @Test
    void getBookedTicketsForUser() {
        User user = new User(1l, "Jozef Malolepszy", "jozef.malolepszy@gmail.com");
        List<Ticket> tickets = bookingFacade.getBookedTickets(user, 3, 0);
        assertEquals(2, tickets.size());
    }

    @Test
    void getBookedTicketsForEvent() throws ParseException {
        Event event = new Event(1l, "Music Event", dateFormat.parse("01-01-2023"), BigDecimal.valueOf(50.00));
        List<Ticket> tickets = bookingFacade.getBookedTickets(event, 10, 0);
        assertEquals(4, tickets.size());
    }

    @Test
    void getBookedTicketsForEventAndCheckPagination() throws ParseException {
        Event event = new Event(1l, "Music Event", dateFormat.parse("01-01-2023"), BigDecimal.valueOf(50.00));
        List<Ticket> tickets = bookingFacade.getBookedTickets(event, 2, 1);

        assertEquals(2, tickets.size());
        assertTrue(tickets.stream().anyMatch(t -> t.getId() == 7L));
        assertFalse(tickets.stream().anyMatch(t -> t.getId() == 1L));
    }

    @Test
    void cancelTicket() {
        User user = new User(4, "Jan Nowak", "j.nowak@gmail.com");
        BigDecimal userAccountAfterRefund = BigDecimal.valueOf(180.30).setScale(2);
        boolean ticketDeleted = bookingFacade.cancelTicket(7);
        List<Ticket> tickets = bookingFacade.getBookedTickets(user, 1, 0);
        UserAccount userAccount = bookingFacade.getUserAccountByUserId(user.getId());

        assertTrue(ticketDeleted);
        assertTrue(tickets.isEmpty());
        assertEquals(userAccountAfterRefund, userAccount.getFunds());
    }

    @Test
    void bookTicketForNewlyCreatedEventAndUser() throws Exception {
        Event event = new Event(4, "New Event", dateFormat.parse("07-07-2023"), BigDecimal.valueOf(10.00));
        bookingFacade.createEvent(event);

        User user = new User(7, "Newes Userus", "n.u@gmail.com");
        bookingFacade.createUser(user);
        bookingFacade.topUpUserAccount(7, BigDecimal.valueOf(100.00));

        Ticket ticket = bookingFacade.bookTicket(user.getId(), event.getId(), 1, Ticket.Category.PREMIUM);
        List<Ticket> ticketsByEvent = bookingFacade.getBookedTickets(event, 2, 0);
        List<Ticket> ticketsByUser = bookingFacade.getBookedTickets(user, 2, 0);

        assertNotNull(ticket);
        assertTrue(ticket.getId() != 0);
        assertEquals(1, ticketsByEvent.size());
        assertEquals(ticketsByEvent, ticketsByUser);

        UserAccount newUserAccount = bookingFacade.getUserAccountByUserId(7);
        assertEquals(BigDecimal.valueOf(90.00).setScale(2), newUserAccount.getFunds());
    }

    @Test
    void ThrowIllegalStateExceptionWhenAttemptingToBookATicketWithInsufficientFunds() {
        assertThrows(IllegalStateException.class, () -> {
            Ticket ticket = bookingFacade.bookTicket(6, 1, 10, Ticket.Category.STANDARD);
        }, "Illegal State Exception was expected");
    }

    @Test
    void cacheTest() throws ParseException {
        bookingFacade.getEventById(1);
        bookingFacade.getEventById(2);
        bookingFacade.getEventById(2);
        bookingFacade.getEventById(2);

        bookingFacade.getUserById(1);
        bookingFacade.getUserById(3);
        bookingFacade.getUserById(3);

        bookingFacade.getUserAccountById(2);
        bookingFacade.getUserAccountById(4);
        bookingFacade.getUserAccountById(4);
        bookingFacade.getUserAccountById(6);


        int eventCacheSize = CacheManager.ALL_CACHE_MANAGERS.get(0)
                .getCache("eventCache").getSize();
        int userCacheSize = CacheManager.ALL_CACHE_MANAGERS.get(0)
                .getCache("userCache").getSize();
        int userAccountCacheSize = CacheManager.ALL_CACHE_MANAGERS.get(0)
                .getCache("userAccountCache").getSize();


        assertTrue(eventCacheSize == 2);
        assertTrue(userCacheSize == 2);
        assertTrue(userAccountCacheSize == 3);
    }
}