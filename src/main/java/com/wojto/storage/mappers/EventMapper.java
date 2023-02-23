package com.wojto.storage.mappers;

import com.wojto.model.Event;
import com.wojto.model.EventImpl;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class EventMapper implements FieldSetMapper<Event> {

    @Override
    public Event mapFieldSet(FieldSet fieldSet) throws BindException {
        Event event = new EventImpl();
        event.setId(fieldSet.readLong("id"));
        event.setTitle(fieldSet.readString("title"));
        event.setDate(fieldSet.readDate("date"));
        return event;
    }
}
