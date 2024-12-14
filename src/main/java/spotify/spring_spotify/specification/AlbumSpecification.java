package spotify.spring_spotify.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import spotify.spring_spotify.entity.Album;
import spotify.spring_spotify.entity.Artist;

public class AlbumSpecification {
    public static Specification<Album> hasNameContainingIgnoreCase(String name) {
        return new Specification<Album>() {
            @Override
            public Predicate toPredicate(Root<Album> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
            }
        };
    }

    public static Specification<Album> sortByNamePriority(String name) {
        return (root, query, criteriaBuilder) -> {
            Expression<Integer> position = criteriaBuilder.locate(criteriaBuilder.lower(root.get("name")), name.toLowerCase());

            Predicate containsKeyword = criteriaBuilder.greaterThan(position, 0);

            query.orderBy(criteriaBuilder.asc(position),
                    criteriaBuilder.desc(root.get("follower")) );


            return criteriaBuilder.and(containsKeyword);
        };
    }
}
