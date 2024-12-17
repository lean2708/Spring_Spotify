package spotify.spring_spotify.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import spotify.spring_spotify.dto.request.GenreRequest;
import spotify.spring_spotify.dto.response.GenreResponse;
import spotify.spring_spotify.entity.Genre;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-16T21:10:03+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class GenreMapperImpl implements GenreMapper {

    @Override
    public Genre toGenre(GenreRequest request) {
        if ( request == null ) {
            return null;
        }

        Genre genre = new Genre();

        genre.setKey( request.getKey() );
        genre.setName( request.getName() );

        return genre;
    }

    @Override
    public GenreResponse toGenreResponse(Genre genre) {
        if ( genre == null ) {
            return null;
        }

        GenreResponse.GenreResponseBuilder genreResponse = GenreResponse.builder();

        genreResponse.id( genre.getId() );
        genreResponse.key( genre.getKey() );
        genreResponse.name( genre.getName() );

        return genreResponse.build();
    }

    @Override
    public Genre update(Genre genre, GenreRequest request) {
        if ( request == null ) {
            return genre;
        }

        genre.setKey( request.getKey() );
        genre.setName( request.getName() );

        return genre;
    }
}
