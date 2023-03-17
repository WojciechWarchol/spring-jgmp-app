package com.wojto.storage.postprocessor;

import com.wojto.dao.InMemoryDao;
import com.wojto.dao.InMemoryEventDao;
import com.wojto.dao.InMemoryTicketDao;
import com.wojto.dao.InMemoryUserDao;
import com.wojto.model.Event;
import com.wojto.model.Ticket;
import com.wojto.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class InMemoryDaoPostProcessor implements BeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryDaoPostProcessor.class);

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof InMemoryDao) {
            if (bean instanceof InMemoryEventDao) {
                LOGGER.info("Post Processor for InMemoryEventDao initialized. Filling in memory DB.");
                InMemoryEventDao dao = (InMemoryEventDao) bean;
                FlatFileItemReader<Event> reader = createReaderForInMemoryDao(dao);
                reader.open(new ExecutionContext());
                boolean linesToRead = true;
                while (linesToRead) {
                    Event event;
                    try {
                        event = reader.read();
                    } catch (Exception e) {
                        reader.close();
                        throw new RuntimeException(e);
                    }
                    if (event == null) {
                        linesToRead = false;
                    } else {
                        dao.createEvent(event);
                    }
                }
                reader.close();
                LOGGER.debug("Finished initializing data for events.");
            } else if (bean instanceof InMemoryUserDao) {
                LOGGER.info("Post Processor for InMemoryUserDao initialized. Filling in memory DB.");
                InMemoryUserDao dao = (InMemoryUserDao) bean;
                FlatFileItemReader<User> reader = createReaderForInMemoryDao(dao);
                reader.open(new ExecutionContext());
                boolean linesToRead = true;
                while (linesToRead) {
                    User user;
                    try {
                        user = reader.read();
                    } catch (Exception e) {
                        reader.close();
                        throw new RuntimeException(e);
                    }
                    if (user == null) {
                        linesToRead = false;
                    } else {
                        dao.createUser(user);
                    }
                }
                reader.close();
                LOGGER.debug("Finished initializing data for users.");
            } else if (bean instanceof InMemoryTicketDao) {
                LOGGER.info("Post Processor for InMemoryTicketDao initialized. Filling in memory DB.");
                InMemoryTicketDao dao = (InMemoryTicketDao) bean;
                FlatFileItemReader<Ticket> reader = createReaderForInMemoryDao(dao);
                reader.open(new ExecutionContext());
                boolean linesToRead = true;
                while (linesToRead) {
                    Ticket ticket;
                    try {
                        ticket = reader.read();
                    } catch (Exception e) {
                        reader.close();
                        throw new RuntimeException(e);
                    }
                    if (ticket == null) {
                        linesToRead = false;
                    } else {
                        dao.bookTicket(ticket);
                    }
                }
                reader.close();
                LOGGER.debug("Finished initializing data for tickets.");
            }
        }
        return bean;
    }

    private <C> FlatFileItemReader<C> createReaderForInMemoryDao(InMemoryDao inMemoryDao) {
        LOGGER.debug("Creating FlatFileReader for " + inMemoryDao.getFileName());
        FlatFileItemReader<C> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource(inMemoryDao.getFileName()));
        reader.setLinesToSkip(1);

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(inMemoryDao.getParameterNames());

        DefaultLineMapper<C> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(inMemoryDao.getMapperForObjects());

        reader.setLineMapper(lineMapper);
        LOGGER.debug("Finished creation of FlatFileReader for " + inMemoryDao.getFileName());

        return reader;
    }
}
