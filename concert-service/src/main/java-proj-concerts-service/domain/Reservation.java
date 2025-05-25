package proj.concert.service.domain;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class Reservation {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne(optional = false)
    private User user;
    @ManyToOne(optional = false)
    private Concert concert;
    private LocalDateTime date;
    @ManyToMany
    private Set<Seat> seats = new HashSet<>();
    @Version
    private int version;

    public Reservation() {
    }
    public Reservation(User user, Concert concert, LocalDateTime date, Set<Seat> seats) {
        this.user = user;
        this.concert = concert;
        this.date = date;
        this.seats = seats;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Concert getConcert() {
        return concert;
    }
    public void setConcert(Concert concert) {
        this.concert = concert;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public Set<Seat> getSeats() {
        return Collections.unmodifiableSet(seats);
    }
    public void setSeats(Set<Seat> seats) {
        this.seats = seats;
    }
    public int getVersion() {
        return version;
    }
    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Reservation)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        Reservation rhs = (Reservation) obj;
        return new EqualsBuilder().append(user, rhs.user).append(concert, rhs.concert).append(date, rhs.date).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(user).append(concert).append(date).hashCode();
    }
}
