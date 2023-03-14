package com.wojto.model;

import javax.persistence.*;

@Entity
@Table(name="tickets")
public class Ticket {

    public enum Category {STANDARD, PREMIUM, BAR}

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private long eventId;
    @Column
    private long userId;
    @Column
    @Enumerated(EnumType.STRING)
    private Category category;
    @Column
    private int place;

    public Ticket() {
    }

    public Ticket(long id, long eventId, long userId, Category category, int place) {
        this.id = id;
        this.eventId = eventId;
        this.userId = userId;
        this.category = category;
        this.place = place;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ticket ticket = (Ticket) o;

        if (id != ticket.id) return false;
        if (eventId != ticket.eventId) return false;
        if (userId != ticket.userId) return false;
        if (place != ticket.place) return false;
        return category == ticket.category;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (eventId ^ (eventId >>> 32));
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + place;
        return result;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", eventId=" + eventId +
                ", userId=" + userId +
                ", category=" + category +
                ", place=" + place +
                '}';
    }
}
