package com.wojto.storage.mappers;

import com.wojto.model.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class TicketMapper implements FieldSetMapper<Ticket> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketMapper.class);

    @Override
    public Ticket mapFieldSet(FieldSet fieldSet) throws BindException {
        Ticket ticket = new Ticket();
        ticket.setId(fieldSet.readLong("id"));
        ticket.setEventId(fieldSet.readLong("eventId"));
        ticket.setUserId(fieldSet.readLong("userId"));
        ticket.setCategory(Ticket.Category.valueOf(fieldSet.readString("category")));
        ticket.setPlace(fieldSet.readInt("place"));
        LOGGER.debug("Created ticket: " + ticket);
        return ticket;
    }
}
