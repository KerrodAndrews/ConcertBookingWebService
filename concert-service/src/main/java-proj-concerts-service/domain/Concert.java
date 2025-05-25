package proj.concert.service.domain;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import proj.concert.common.jackson.*;


@Entity
@Table(name = "concerts")
public class Concert{

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "IMAGE_NAME")
    private String imageName;

    @Column(name = "BLURB")
    @Lob private String blurb;

    @ElementCollection
    @CollectionTable(name = "concert_dates", joinColumns = @JoinColumn(name = "concert_id"))
    @Column(name = "date")
    @JsonSerialize(contentUsing = LocalDateTimeSerializer.class)
    @JsonDeserialize(contentUsing = LocalDateTimeDeserializer.class)
    private Set<LocalDateTime> dates;

    @ManyToMany
    @JoinTable(
            name = "concert_performer",
            joinColumns = @JoinColumn(name = "concert_id"),
            inverseJoinColumns = @JoinColumn(name = "performer_id"))
    private List<Performer> performers;

    public Concert(Long id, String title, String imageName, String blurb) {
        this.id = id;
        this.title = title;
        this.imageName = imageName;
        this.blurb = blurb;
    }

    public Concert(String title, String imageName, String blurb) {
        this(null, title, imageName, blurb);
    }

    public Concert() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    public Set<LocalDateTime> getDates() {
        return dates;
    }
    public void setDates(Set<LocalDateTime> dates) {
        this.dates = dates;
    }
    public List<Performer> getPerformers() {
        return performers;
    }
    public void setPerformers(List<Performer> performers) {
        this.performers = performers;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Concert, id: ");
        buffer.append(id);
        buffer.append(", title: ");
        buffer.append(title);
        buffer.append(", imageName: ");
        buffer.append(imageName);
        buffer.append(", blurb: ");
        buffer.append(blurb);
        return buffer.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Concert)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        Concert rhs = (Concert) obj;
        return new EqualsBuilder().append(title, rhs.title).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(title).hashCode();
    }
}
