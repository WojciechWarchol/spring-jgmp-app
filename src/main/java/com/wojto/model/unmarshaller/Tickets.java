package com.wojto.model.unmarshaller;

import com.wojto.model.Ticket;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "tickets")
public class Tickets {
    private List<Ticket> tickets;

    @XmlElement(name = "ticket")
    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
