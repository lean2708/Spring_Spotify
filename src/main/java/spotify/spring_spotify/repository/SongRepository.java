package spotify.spring_spotify.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import spotify.spring_spotify.entity.Artist;
import spotify.spring_spotify.entity.Genre;
import spotify.spring_spotify.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song,Long>, JpaSpecificationExecutor<Song> {
    boolean existsByName(String name);

    List<Song> findAllByNameIn(List<String> listName);
    List<Song> findAllByGenre(Genre genre);
}
