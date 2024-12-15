package spotify.spring_spotify.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import spotify.spring_spotify.dto.basic.PlaylistBasic;
import spotify.spring_spotify.dto.request.PlaylistRequest;
import spotify.spring_spotify.dto.response.PlaylistResponse;
import spotify.spring_spotify.entity.Playlist;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-15T22:32:36+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class PlaylistMapperImpl implements PlaylistMapper {

    @Override
    public Playlist toPlaylist(PlaylistRequest request) {
        if ( request == null ) {
            return null;
        }

        Playlist playlist = new Playlist();

        playlist.setTitle( request.getTitle() );
        playlist.setDescription( request.getDescription() );
        playlist.setFollower( request.getFollower() );
        playlist.setCreator( request.getCreator() );
        playlist.setListener( request.getListener() );

        return playlist;
    }

    @Override
    public PlaylistResponse toPlaylistResponse(Playlist playlist) {
        if ( playlist == null ) {
            return null;
        }

        PlaylistResponse.PlaylistResponseBuilder playlistResponse = PlaylistResponse.builder();

        playlistResponse.id( playlist.getId() );
        playlistResponse.title( playlist.getTitle() );
        playlistResponse.description( playlist.getDescription() );
        playlistResponse.totalTracks( playlist.getTotalTracks() );
        playlistResponse.follower( playlist.getFollower() );
        playlistResponse.creator( playlist.getCreator() );
        playlistResponse.imageURL( playlist.getImageURL() );
        playlistResponse.listener( playlist.getListener() );
        playlistResponse.totalHours( playlist.getTotalHours() );
        playlistResponse.createdAt( playlist.getCreatedAt() );
        playlistResponse.updatedAt( playlist.getUpdatedAt() );

        return playlistResponse.build();
    }

    @Override
    public PlaylistBasic toPlaylistBasic(Playlist playlist) {
        if ( playlist == null ) {
            return null;
        }

        PlaylistBasic.PlaylistBasicBuilder playlistBasic = PlaylistBasic.builder();

        playlistBasic.id( playlist.getId() );
        playlistBasic.title( playlist.getTitle() );
        playlistBasic.description( playlist.getDescription() );
        playlistBasic.follower( playlist.getFollower() );
        playlistBasic.creator( playlist.getCreator() );
        playlistBasic.imageURL( playlist.getImageURL() );
        playlistBasic.totalHours( playlist.getTotalHours() );

        return playlistBasic.build();
    }

    @Override
    public Playlist update(Playlist playlist, PlaylistRequest request) {
        if ( request == null ) {
            return playlist;
        }

        playlist.setTitle( request.getTitle() );
        playlist.setDescription( request.getDescription() );
        playlist.setFollower( request.getFollower() );
        playlist.setCreator( request.getCreator() );
        playlist.setListener( request.getListener() );

        return playlist;
    }
}
