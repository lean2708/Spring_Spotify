package spotify.spring_spotify.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import spotify.spring_spotify.dto.basic.SongBasic;
import spotify.spring_spotify.dto.request.SongRequest;
import spotify.spring_spotify.dto.response.SongResponse;
import spotify.spring_spotify.entity.Song;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-17T12:54:18+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class SongMapperImpl implements SongMapper {

    @Override
    public Song toSong(SongRequest request) {
        if ( request == null ) {
            return null;
        }

        Song song = new Song();

        song.setName( request.getName() );
        song.setDescription( request.getDescription() );
        song.setDuration( request.getDuration() );
        song.setListener( request.getListener() );

        return song;
    }

    @Override
    public SongResponse toSongResponse(Song song) {
        if ( song == null ) {
            return null;
        }

        SongResponse.SongResponseBuilder songResponse = SongResponse.builder();

        songResponse.id( song.getId() );
        songResponse.name( song.getName() );
        songResponse.description( song.getDescription() );
        songResponse.duration( song.getDuration() );
        songResponse.listener( song.getListener() );
        songResponse.imageURL( song.getImageURL() );
        songResponse.fileSongURL( song.getFileSongURL() );
        songResponse.createdAt( song.getCreatedAt() );
        songResponse.updatedAt( song.getUpdatedAt() );
        songResponse.genre( song.getGenre() );

        return songResponse.build();
    }

    @Override
    public SongBasic toSongBasic(Song song) {
        if ( song == null ) {
            return null;
        }

        SongBasic.SongBasicBuilder songBasic = SongBasic.builder();

        songBasic.id( song.getId() );
        songBasic.name( song.getName() );
        songBasic.description( song.getDescription() );
        songBasic.duration( song.getDuration() );
        songBasic.listener( song.getListener() );
        songBasic.imageURL( song.getImageURL() );
        songBasic.fileSongURL( song.getFileSongURL() );
        songBasic.createdAt( song.getCreatedAt() );
        songBasic.updatedAt( song.getUpdatedAt() );

        return songBasic.build();
    }

    @Override
    public Song update(Song song, SongRequest request) {
        if ( request == null ) {
            return song;
        }

        song.setName( request.getName() );
        song.setDescription( request.getDescription() );
        song.setDuration( request.getDuration() );
        song.setListener( request.getListener() );

        return song;
    }
}
