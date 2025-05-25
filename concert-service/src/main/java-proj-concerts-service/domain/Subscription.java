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
public class Subscription {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(optional = false)
    private User user;
    @ManyToOne(optional = false)
    private Concert concert;
    private LocalDateTime date;
    private int percentageThreshold;
    @Version
    private int version;

    public Subscription() {
    }
    public Subscription(User user, Concert concert, LocalDateTime date, int percentageThreshold) {
        this.user = user;
        this.concert = concert;
        this.date = date;
        this.percentageThreshold = percentageThreshold;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
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
    public int getPercentageThreshold() {
        return percentageThreshold;
    }
    public void setPercentageThreshold(int percentageThreshold) {
        this.percentageThreshold = percentageThreshold;
    }
    public int getVersion() {
        return version;
    }
    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Subscription)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        Subscription rhs = (Subscription) obj;
        return new EqualsBuilder().append(user, rhs.user).append(concert, rhs.concert).append(date, rhs.date).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(user).append(concert).append(date).hashCode();
    }
}
