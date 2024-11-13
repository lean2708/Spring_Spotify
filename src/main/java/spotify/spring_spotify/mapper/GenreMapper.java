package spotify.spring_spotify.mapper;

import spotify.spring_spotify.dto.basic.GenreBasic;
import spotify.spring_spotify.dto.request.GenreRequest;
import spotify.spring_spotify.dto.response.GenreResponse;
import spotify.spring_spotify.entity.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    @Mapping(target = "songs", ignore = true)
    Genre toGenre(GenreRequest request);
    @Mapping(target = "songs", ignore = true)
    GenreResponse toGenreResponse(Genre genre);
    GenreBasic toGenreBasic(Genre genre);

    @Mapping(target = "songs", ignore = true)
    Genre update(@MappingTarget Genre genre,GenreRequest request);
}
