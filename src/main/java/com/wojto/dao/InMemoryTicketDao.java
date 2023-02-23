package com.wojto.dao;

import com.wojto.model.Event;
import com.wojto.model.Ticket;
import com.wojto.model.User;
import com.wojto.storage.TicketInMemoryStorage;
import com.wojto.storage.mappers.TicketMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;


public class InMemoryTicketDao implements TicketDao, InMemoryDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryTicketDao.class);

    TicketInMemoryStorage ticketInMemoryStorage;

    private static String fileName;
    private static final String[] PARAM_NAMES = new String[] { "id", "eventId", "userId", "category", "place" };
    private static final Class<Ticket> SUPPORTED_CLASS_TYPE = Ticket.class;

    @Override
    public String getFileName() {
        return fileName;
    }

    public static void setFileName(String fileName) {
        InMemoryTicketDao.fileName = fileName;
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
        List<Ticket> allTickets = ticketInMemoryStorage.getAllTickets();
        Page<Ticket> page = convertListToPage(pageable, allTickets);
        return page;
    }

    @Override
    public Ticket getTicketById(long ticketId) {
        return ticketInMemoryStorage.getTicketById(ticketId);
    }

    @Override
    public Page<Ticket> getBookedTickets(User user, Pageable pageable) {
        List<Ticket> ticketsFoundForUser = ticketInMemoryStorage.getBooketTickets(user, pageable);
        Page<Ticket> page = convertListToPage(pageable, ticketsFoundForUser);
        return page;
    }

    @Override
    public Page<Ticket> getBookedTickets(Event event, Pageable pageable) {
        List<Ticket> ticketsFoundForEvent = ticketInMemoryStorage.getBooketTickets(event, pageable);
        Page<Ticket> page = convertListToPage(pageable, ticketsFoundForEvent);
        return page;
    }

    @Override
    public List<Ticket> getBookedTickets(long eventId) {
        return ticketInMemoryStorage.getAllTickets()
                .stream()
                .filter(t -> t.getEventId() == eventId)
                .collect(Collectors.toList());
    }

    @Override
    public Ticket bookTicket(Ticket ticket) {
        return ticketInMemoryStorage.addTicket(ticket);
    }

    @Override
    public boolean deleteTicket(long ticketId) {
        return ticketInMemoryStorage.deleteTicket(ticketId);
    }

    public long getNewTicketId() {
        return ticketInMemoryStorage.getNewTicketId();
    }

    private Page<Ticket> convertListToPage(Pageable pageable, List<Ticket> listOfTickets) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), listOfTickets.size());
        Page<Ticket> page = new PageImpl<Ticket>(listOfTickets.subList(start, end), pageable, listOfTickets.size());
        return page;
    }

    public TicketInMemoryStorage getTicketInMemoryStorage() {
        return ticketInMemoryStorage;
    }

    public void setTicketInMemoryStorage(TicketInMemoryStorage ticketInMemoryStorage) {
        this.ticketInMemoryStorage = ticketInMemoryStorage;
    }
}
