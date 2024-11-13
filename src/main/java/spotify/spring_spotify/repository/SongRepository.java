package spotify.spring_spotify.repository;

import spotify.spring_spotify.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song,Long> {
    boolean existsByName(String name);

    List<Song> findAllByNameIn(List<String> listName);
}
