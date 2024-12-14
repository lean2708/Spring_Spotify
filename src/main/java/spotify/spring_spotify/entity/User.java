package spotify.spring_spotify.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Data
@Builder
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
     String imageURL;
    @JsonFormat(pattern = "dd/MM/yyyy")
     LocalDate dob;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate updatedAt;

    @ManyToMany
    Set<Role> roles;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_playlist",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "playlist_id")
    )
    Set<Playlist> createdPlaylists;

    @ElementCollection
    @Column(name = "playlist_id")
    List<Long> savedPlaylistId;

    boolean premiumStatus;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate premiumExpiryDate;

    @PrePersist
    public void hanldeBeforeCreate(){
        this.createdAt = LocalDate.now();
    }

    @PreUpdate
    public void hanldeBeforeUpdate(){
        this.updatedAt = LocalDate.now();
    }
}
