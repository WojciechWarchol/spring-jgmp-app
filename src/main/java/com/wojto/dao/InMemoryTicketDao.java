package com.wojto.dao;

import com.wojto.model.Event;
import com.wojto.model.Ticket;
import com.wojto.model.User;
import com.wojto.storage.TicketInMemoryStorage;
import com.wojto.storage.mappers.TicketMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class InMemoryTicketDao implements TicketDao, InMemoryDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryTicketDao.class);

    @Autowired
    TicketInMemoryStorage ticketInMemoryStorage;

    @Value("${dao.inmemory.ticket.contentfile}")
    private String fileName;
    private static final String[] PARAM_NAMES = new String[] { "id", "eventId", "userId", "category", "place" };
    private static final Class<Ticket> SUPPORTED_CLASS_TYPE = Ticket.class;

    @Override
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String[] getParameterNames() {
        return PARAM_NAMES;
    }

    @Override
    public Class<Ticket> getClassType() {
        return SUPPORTED_CLASS_TYPE;
    }

    @Override
    public FieldSetMapper getMapperForObjects() {
        return new TicketMapper();
    }

    @Override
    public Page<Ticket> getAllTickets(Pageable pageable) {
        LOGGER.info("Calling in memory storage to retrieve all tickets.");
        List<Ticket> allTicketsList = ticketInMemoryStorage.getAllTickets();
        LOGGER.info("Retrieved list of all tickets: " + allTicketsList );
        Page<Ticket> page = convertListToPage(pageable, allTicketsList);
        return page;
    }

    @Override
    public Ticket getTicketById(long ticketId) {
        LOGGER.info("Calling in memory storage for ticket with id: " + ticketId);
        Ticket ticket = ticketInMemoryStorage.getTicketById(ticketId);
        LOGGER.info("Retrieved ticket from in memory storage: " + ticket);
        return ticket;
    }

    @Override
    public Page<Ticket> getBookedTickets(User user, Pageable pageable) {
        LOGGER.info("Calling in memory storage for tickets booked by user: " + user);
        List<Ticket> ticketsFoundForUser = ticketInMemoryStorage.getBooketTickets(user, pageable);
        LOGGER.info("Retrieved tickets from in memory storage: " + ticketsFoundForUser);
        Page<Ticket> page = convertListToPage(pageable, ticketsFoundForUser);
        return page;
    }

    @Override
    public Page<Ticket> getBookedTickets(Event event, Pageable pageable) {
        LOGGER.info("Calling in memory storage for tickets booked for event: " + event);
        List<Ticket> ticketsFoundForEvent = ticketInMemoryStorage.getBooketTickets(event, pageable);
        LOGGER.info("Retrieved tickets from in memory storage: " + ticketsFoundForEvent);
        Page<Ticket> page = convertListToPage(pageable, ticketsFoundForEvent);
        return page;
    }

    @Override
    public List<Ticket> getBookedTickets(long eventId) {
        LOGGER.info("Calling in memory storage for tickets booked for event with id: " + eventId);
        List<Ticket> ticketList = ticketInMemoryStorage.getAllTickets()
                .stream()
                .filter(t -> t.getEventId() == eventId)
                .collect(Collectors.toList());
        LOGGER.info("Retrieved tickets from in memory storage: " + ticketList);
        return ticketList;
    }

    @Override
    public Ticket bookTicket(Ticket ticket) {
        LOGGER.info("Booking ticket, adding to in memory storage: " + ticket);
        return ticketInMemoryStorage.addTicket(ticket);
    }

    @Override
    public boolean deleteTicket(long ticketId) {
        LOGGER.info("Deleting ticket from in memory storage with id: " + ticketId);
        return ticketInMemoryStorage.deleteTicket(ticketId);
    }

    public long getNewTicketId() {
        LOGGER.info("Calling in memorty storage for new ticket id.");
        return ticketInMemoryStorage.getNewTicketId();
    }

    private Page<Ticket> convertListToPage(Pageable pageable, List<Ticket> listOfTickets) {
        LOGGER.debug("Converting ticket List to Page");
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), listOfTickets.size());
        Page<Ticket> page = new PageImpl<Ticket>(listOfTickets.subList(start, end), pageable, listOfTickets.size());
        LOGGER.debug("Created page of ticket: " + page);
        return page;
    }

    public TicketInMemoryStorage getTicketInMemoryStorage() {
        return ticketInMemoryStorage;
    }

    public void setTicketInMemoryStorage(TicketInMemoryStorage ticketInMemoryStorage) {
        this.ticketInMemoryStorage = ticketInMemoryStorage;
    }
}
