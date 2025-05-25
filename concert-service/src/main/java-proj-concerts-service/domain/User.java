package proj.concert.service.domain;

import javax.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Version
    private int version;

    public User() {
    }
    public User(Long id, String username, String password, int version) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.version = version;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getVersion() {
        return version;
    }
    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        User rhs = (User) obj;
        return new EqualsBuilder().append(id, rhs.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).hashCode();
    }
}
