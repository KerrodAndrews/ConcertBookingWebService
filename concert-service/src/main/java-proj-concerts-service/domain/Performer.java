package proj.concert.service.domain;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import proj.concert.common.types.Genre;

@Entity
@Table(name = "PERFORMERS")
public class Performer {
    @Id
//    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(name = "IMAGE_NAME")
    private String imageName;
    @Enumerated(EnumType.STRING)
    private Genre genre;
    @Column(name="BLURB")
    @Lob private String blurb;
    @ManyToMany(mappedBy = "performers")
    private Set<Concert> concerts;

    public Performer(Long id, String name, String imageName, Genre genre, String blurb) {
        this.id = id;
        this.name = name;
        this.imageName = imageName;
        this.genre = genre;
        this.blurb = blurb;
    }

    public Performer(String name, String imageName, Genre genre, String blurb) {
        this(null, name, imageName, genre, blurb);
    }

    public Performer() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImageName() {
        return imageName;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    public Genre getGenre() {
        return genre;
    }
    public void setGenre(Genre genre) {
        this.genre = genre;
    }
    public Set<Concert> getConcerts() {
        return concerts;
    }
    public void setConcerts(Set<Concert> concerts) {
        this.concerts = concerts;
    }
    public String getBlurb() {return blurb;}
    public void setBlurb(String blurb) {this.blurb = blurb;}

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Performer, id: ");
        buffer.append(id);
        buffer.append(", name: ");
        buffer.append(name);
        buffer.append(", imageName: ");
        buffer.append(imageName);
        buffer.append(", genre: ");
        buffer.append(genre);
        return buffer.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Performer))
            return false;
        if (obj == this)
            return true;
        Performer rhs = (Performer) obj;
        return new EqualsBuilder().append(name, rhs.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(name).hashCode();
    }
}
