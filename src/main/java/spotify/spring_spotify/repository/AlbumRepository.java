package spotify.spring_spotify.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import spotify.spring_spotify.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spotify.spring_spotify.entity.Artist;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album,Long>, JpaSpecificationExecutor<Album> {
    boolean existsByName(String name);
    List<Album> findAllByNameIn(List<String> listName);
    Album findByName(String name);

}
