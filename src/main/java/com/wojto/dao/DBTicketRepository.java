package com.wojto.dao;

import com.wojto.model.Event;
import com.wojto.model.Ticket;
import com.wojto.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DBTicketRepository extends JpaRepository<Ticket, Long>, TicketDao {

    /**
     * Methods from the original interface
     */
    @Override
    default Page<Ticket> getAllTickets(Pageable pageable) {
        return getAllTickets(pageable);
    }

    @Override
    default Ticket getTicketById(long ticketId) {
        return getTicketById(ticketId);
    }

    @Override
    default Page<Ticket> getBookedTickets(User user, Pageable pageable) {
        return findByUserId(user.getId(), pageable);
    }

    @Override
    default Page<Ticket> getBookedTickets(Event event, Pageable pageable) {
        return findByEventId(event.getId(), pageable);
    }

    @Override
    default List<Ticket> getBookedTickets(long eventId) {
        return findByEventId(eventId);
    }

    @Override
    default Ticket bookTicket(Ticket ticket) {
        return save(ticket);
    }

    @Override
    default boolean deleteTicket(long ticketId) {
        if (existsById(ticketId)) {
            deleteById(ticketId);
            return true;
        }
        return false;
    }

    /**
     * Methods from JpaRepository interface
     */

    List<Ticket> findAll();

    Page<Ticket> findAll(Pageable pageable);

    Ticket findById(long id);

    Page<Ticket> findByUserId(long userId, Pageable pageable);

    Page<Ticket> findByEventId(long eventId, Pageable pageable);

    List<Ticket> findByEventId(long eventId);

    Ticket save(Ticket ticket);

    void deleteById(long id);
}
