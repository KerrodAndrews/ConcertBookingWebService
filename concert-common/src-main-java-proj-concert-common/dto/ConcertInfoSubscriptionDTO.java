package proj.concert.common.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import proj.concert.common.jackson.LocalDateTimeDeserializer;
import proj.concert.common.jackson.LocalDateTimeSerializer;

/**
 * Represents a subscription request to be notified when a particular concert / date's booking ratio goes over
 * a certain amount.
 *
 * concertId          the id of the concert
 * date               the date of the particular performance
 * percentageBooked   the threshold at which a notification is requested
 */
public class ConcertInfoSubscriptionDTO {

    @JsonProperty("concertId")
    private long concertId;
    @JsonProperty("date")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime date;
    @JsonProperty("percentageBooked")
    private int percentageBooked;

    public ConcertInfoSubscriptionDTO() {
    }

    public ConcertInfoSubscriptionDTO(long concertId, LocalDateTime date, int percentageBooked) {
        this.concertId = concertId;
        this.date = date;
        this.percentageBooked = percentageBooked;
    }

    public long getConcertId() {
        return concertId;
    }

    public void setConcertId(long concertId) {
        this.concertId = concertId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getPercentageBooked() {
        return percentageBooked;
    }

    public void setPercentageBooked(int percentageBooked) {
        this.percentageBooked = percentageBooked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ConcertInfoSubscriptionDTO that = (ConcertInfoSubscriptionDTO) o;

        return new EqualsBuilder()
                .append(concertId, that.concertId)
                .append(percentageBooked, that.percentageBooked)
                .append(date, that.date)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(concertId)
                .append(date)
                .append(percentageBooked)
                .toHashCode();
    }
}
