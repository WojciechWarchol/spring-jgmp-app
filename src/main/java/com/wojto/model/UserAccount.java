package com.wojto.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "user_accounts")
public class UserAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private long userId;
    @Column
    private BigDecimal funds;

    public UserAccount() {
    }

    public UserAccount(long id, long userId) {
        this.id = id;
        this.userId = userId;
        this.funds = BigDecimal.ZERO.setScale(2);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public BigDecimal getFunds() {
        return funds.setScale(2);
    }

    public void setFunds(BigDecimal funds) {
        if (funds.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("User funds cannot be less than 0!");
        }
        this.funds = funds;
    }

    public BigDecimal topUp(BigDecimal amount) {
        return funds = funds.add(amount);
    }

    public boolean deductFunds(BigDecimal amount) {
        if (funds.compareTo(amount) >= 0) {
            funds = funds.subtract(amount);
            return true;
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAccount that = (UserAccount) o;

        if (id != that.id) return false;
        if (userId != that.userId) return false;
        return funds.equals(that.funds);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + funds.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "id=" + id +
                ", userId=" + userId +
                ", funds=" + funds +
                '}';
    }
}
