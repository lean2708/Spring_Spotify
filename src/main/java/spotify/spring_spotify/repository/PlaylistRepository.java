package spotify.spring_spotify.repository;

import spotify.spring_spotify.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist,Long> {
    boolean existsByTitle(String title);
    List<Playlist> findAllByTitleIn(List<String> listTitle);

}
