package proj.concert.service.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.io.Serializable;
import java.util.Set;

@Entity
@IdClass(Seat.SeatId.class)
@Table(name = "SEATS")
public class Seat{
	@Id
	@Column(name = "LABEL")
	private String label;
	@Id
	@Column(name = "DATE")
	private LocalDateTime date;
	@Column(name = "IS_BOOKED", nullable = false)
	private boolean isBooked;
	@Column(name = "COST")
	private BigDecimal cost;
	public Seat(String label, boolean isBooked, LocalDateTime date, BigDecimal cost) {
		this.label = label;
		this.isBooked = isBooked;
		this.date = date;
		this.cost = cost;
	}	
	
	public Seat() {}

	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	public boolean isBooked() {
		return isBooked;
	}
	public void setBooked(boolean booked) {
		isBooked = booked;
	}
	public BigDecimal getCost() {
		return cost;
	}
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Seat)) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		Seat seat = (Seat) obj;
		return new EqualsBuilder().append(label, seat.label).append(date, seat.date).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(label).append(date).hashCode();
	}

	public static class SeatId implements Serializable {
		private String label;
		private LocalDateTime date;
		public SeatId() {}
		public SeatId(String label, LocalDateTime date) {
			this.label = label;
			this.date = date;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public LocalDateTime getDate() {
			return date;
		}
		public void setDate(LocalDateTime date) {
			this.date = date;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof SeatId)) {
				return false;
			}
			if (obj == this) {
				return true;
			}
			SeatId seatId = (SeatId) obj;
			return new EqualsBuilder().append(label, seatId.label).append(date, seatId.date).isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 37).append(label).append(date).hashCode();
		}
	}
}
