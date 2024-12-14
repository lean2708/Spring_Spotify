package spotify.spring_spotify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spotify.spring_spotify.entity.Role;
import spotify.spring_spotify.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {
    boolean existsByName(String name);
    List<Role> findAllByNameIn(List<String> listName);
    Optional<Role> findByName(String name);
}
