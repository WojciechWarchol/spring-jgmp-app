package com.wojto.dao;

import com.wojto.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository("DBEventRepository")
public interface DBEventRepository extends JpaRepository<Event, Long>, EventDao {

    /**
     * Methods from the original interface
     */
    @Override
    default Page<Event> getAllEvents(Pageable pageable) {
        return findAll(pageable);
    }

    @Override
    default Event getEventById(long id) {
        return findById(id);
    }

    @Override
    default Page<Event> getEventsByTitle(String title, Pageable pageable) {
        return findByTitleContaining(title, pageable);
    }

    @Override
    default Page<Event> getEventsForDay(Date day, Pageable pageable) {
        return findByDate(day, pageable);
    }

    @Override
    default Event createEvent(Event event) {
        return save(event);
    }

    @Override
    default Event updateEvent(Event event) {
        return save(event);
    }

    @Override
    default boolean deleteEvent(long eventId) {
        if(existsById(eventId)) {
            deleteById(eventId);
            return true;
        }
        return false;
    }

    /**
     * Methods from JpaRepository interface
     */


    List<Event> findAll();

    Page<Event> findAll(Pageable pageable);

    Event findById(long id);

    Page<Event> findByTitleContaining(String title, Pageable pageable);

    Page<Event> findByDate(Date date, Pageable pageable);

    <S extends Event> S save(S entity);
}
