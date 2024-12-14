package spotify.spring_spotify.mapper;

import spotify.spring_spotify.dto.request.GenreRequest;
import spotify.spring_spotify.dto.response.GenreResponse;
import spotify.spring_spotify.entity.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    Genre toGenre(GenreRequest request);

    GenreResponse toGenreResponse(Genre genre);

    Genre update(@MappingTarget Genre genre,GenreRequest request);
}
