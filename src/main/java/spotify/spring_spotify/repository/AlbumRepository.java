package spotify.spring_spotify.repository;

import spotify.spring_spotify.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album,Long> {
    boolean existsByName(String name);
    List<Album> findAllByNameIn(List<String> listName);
    Album findByName(String name);

}
