package spotify.spring_spotify.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import spotify.spring_spotify.entity.Artist;
import spotify.spring_spotify.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist,Long>, JpaSpecificationExecutor<Playlist> {
    boolean existsByTitle(String title);
    Optional<List<Playlist>> findAllByTitleIn(List<String> listTitle);
    Optional<List<Playlist>> findAllByIdIn(List<Long> listId);

}
