package com.wojto.storage.mappers;

import com.wojto.model.Ticket;
import com.wojto.model.TicketImpl;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class TicketMapper implements FieldSetMapper<Ticket> {

    @Override
    public Ticket mapFieldSet(FieldSet fieldSet) throws BindException {
        Ticket ticket = new TicketImpl();
        ticket.setId(fieldSet.readLong("id"));
        ticket.setEventId(fieldSet.readLong("eventId"));
        ticket.setUserId(fieldSet.readLong("userId"));
        ticket.setCategory(Ticket.Category.valueOf(fieldSet.readString("category")));
        return ticket;
    }
}
