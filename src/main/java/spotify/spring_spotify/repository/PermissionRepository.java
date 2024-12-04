package spotify.spring_spotify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spotify.spring_spotify.entity.Permission;
import spotify.spring_spotify.entity.Role;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,String> {
    boolean existsByName(String name);
}
