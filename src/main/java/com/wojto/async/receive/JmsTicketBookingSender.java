package com.wojto.async.receive;

import com.wojto.model.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsTicketBookingSender {

    public static final Logger LOGGER = LoggerFactory.getLogger(JmsTicketBookingSender.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    public void send(Ticket ticket) {
        LOGGER.info("Sending ticket to Artemis: " + ticket.toString());
        jmsTemplate.convertAndSend(ticket);
    }

}
