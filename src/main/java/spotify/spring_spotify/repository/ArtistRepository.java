package spotify.spring_spotify.repository;

import spotify.spring_spotify.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist,Long> {
    boolean existsByName(String name);

    List<Artist> findAllByNameIn(List<String> listName);

}
