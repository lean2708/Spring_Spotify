package spotify.spring_spotify.repository;

import spotify.spring_spotify.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre,Long> {
    boolean existsByName(String name);
    List<Genre> findAllByNameIn(List<String> listName);
    Genre findByName(String name);

}
