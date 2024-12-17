package spotify.spring_spotify.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import spotify.spring_spotify.dto.basic.AlbumBasic;
import spotify.spring_spotify.dto.request.AlbumRequest;
import spotify.spring_spotify.dto.response.AlbumResponse;
import spotify.spring_spotify.entity.Album;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-16T21:10:03+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class AlbumMapperImpl implements AlbumMapper {

    @Override
    public Album toAlbum(AlbumRequest request) {
        if ( request == null ) {
            return null;
        }

        Album album = new Album();

        album.setName( request.getName() );
        album.setDescription( request.getDescription() );
        album.setFollower( request.getFollower() );

        return album;
    }

    @Override
    public AlbumResponse toAlbumResponse(Album album) {
        if ( album == null ) {
            return null;
        }

        AlbumResponse.AlbumResponseBuilder albumResponse = AlbumResponse.builder();

        albumResponse.id( album.getId() );
        albumResponse.name( album.getName() );
        albumResponse.description( album.getDescription() );
        albumResponse.totalTracks( album.getTotalTracks() );
        albumResponse.follower( album.getFollower() );
        albumResponse.imageURL( album.getImageURL() );
        albumResponse.totalHours( album.getTotalHours() );
        albumResponse.createdAt( album.getCreatedAt() );
        albumResponse.updatedAt( album.getUpdatedAt() );

        return albumResponse.build();
    }

    @Override
    public AlbumBasic toAlbumBasic(Album album) {
        if ( album == null ) {
            return null;
        }

        AlbumBasic.AlbumBasicBuilder albumBasic = AlbumBasic.builder();

        albumBasic.id( album.getId() );
        albumBasic.name( album.getName() );
        albumBasic.description( album.getDescription() );
        albumBasic.totalTracks( album.getTotalTracks() );
        albumBasic.follower( album.getFollower() );
        albumBasic.totalHours( album.getTotalHours() );

        return albumBasic.build();
    }

    @Override
    public Album update(Album album, AlbumRequest request) {
        if ( request == null ) {
            return album;
        }

        album.setName( request.getName() );
        album.setDescription( request.getDescription() );
        album.setFollower( request.getFollower() );

        return album;
    }
}
