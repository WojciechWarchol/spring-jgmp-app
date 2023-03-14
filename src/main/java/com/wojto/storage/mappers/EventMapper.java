package com.wojto.storage.mappers;

import com.wojto.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class EventMapper implements FieldSetMapper<Event> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventMapper.class);

    @Override
    public Event mapFieldSet(FieldSet fieldSet) throws BindException {
        Event event = new Event();
        event.setId(fieldSet.readLong("id"));
        event.setTitle(fieldSet.readString("title"));
        event.setDate(fieldSet.readDate("date"));
        LOGGER.debug("Created event: " + event);
        return event;
    }
}
