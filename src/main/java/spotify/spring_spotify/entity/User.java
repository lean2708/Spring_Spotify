package spotify.spring_spotify.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     long id;
     String name;
     String email;
     @JsonIgnore
     String password;
    @JsonFormat(pattern = "dd/MM/yyyy")
     LocalDate dob;
     String gender;
     String language;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate updatedAt;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_playlist",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "playlist_id")
    )
    Set<Playlist> playlists;

    @PrePersist
    public void hanldeBeforeCreate(){
        this.createdAt = LocalDate.now();
    }

    @PreUpdate
    public void hanldeBeforeUpdate(){
        this.updatedAt = LocalDate.now();
    }
}
