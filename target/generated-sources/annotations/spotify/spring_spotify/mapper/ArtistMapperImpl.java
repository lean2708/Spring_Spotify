package spotify.spring_spotify.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import spotify.spring_spotify.dto.basic.ArtistBasic;
import spotify.spring_spotify.dto.request.ArtistRequest;
import spotify.spring_spotify.dto.response.ArtistResponse;
import spotify.spring_spotify.entity.Artist;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-15T20:38:21+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class ArtistMapperImpl implements ArtistMapper {

    @Override
    public Artist toArtist(ArtistRequest request) {
        if ( request == null ) {
            return null;
        }

        Artist artist = new Artist();

        artist.setName( request.getName() );
        artist.setFollower( request.getFollower() );

        return artist;
    }

    @Override
    public ArtistResponse toArtistResponse(Artist artist) {
        if ( artist == null ) {
            return null;
        }

        ArtistResponse.ArtistResponseBuilder artistResponse = ArtistResponse.builder();

        artistResponse.id( artist.getId() );
        artistResponse.name( artist.getName() );
        artistResponse.follower( artist.getFollower() );
        artistResponse.imageURL( artist.getImageURL() );

        return artistResponse.build();
    }

    @Override
    public ArtistBasic toArtistBasic(Artist artist) {
        if ( artist == null ) {
            return null;
        }

        ArtistBasic.ArtistBasicBuilder artistBasic = ArtistBasic.builder();

        artistBasic.id( artist.getId() );
        artistBasic.name( artist.getName() );
        artistBasic.follower( artist.getFollower() );

        return artistBasic.build();
    }

    @Override
    public Artist update(Artist artist, ArtistRequest request) {
        if ( request == null ) {
            return artist;
        }

        artist.setName( request.getName() );
        artist.setFollower( request.getFollower() );

        return artist;
    }
}
