package spotify.spring_spotify.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import spotify.spring_spotify.entity.Album;
import spotify.spring_spotify.entity.Playlist;

public class PlaylistSpecification {
    public static Specification<Playlist> hasNameContainingIgnoreCase(String name) {
        return new Specification<Playlist>() {
            @Override
            public Predicate toPredicate(Root<Playlist> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + name.toLowerCase() + "%");
            }
        };
    }

    public static Specification<Playlist> sortByNamePriority(String name) {
        return (root, query, criteriaBuilder) -> {
            Expression<Integer> position = criteriaBuilder.locate(criteriaBuilder.lower(root.get("title")), name.toLowerCase());

            Predicate containsKeyword = criteriaBuilder.greaterThan(position, 0);

            query.orderBy(criteriaBuilder.asc(position),
                    criteriaBuilder.desc(root.get("follower")));

            return criteriaBuilder.and(containsKeyword);
        };
    }
}
