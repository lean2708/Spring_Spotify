package spotify.spring_spotify.mapper;

import spotify.spring_spotify.dto.basic.SongBasic;
import spotify.spring_spotify.dto.request.SongRequest;
import spotify.spring_spotify.dto.response.SongResponse;
import spotify.spring_spotify.entity.Song;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SongMapper {
    @Mapping(target = "album", ignore = true)
    @Mapping(target = "genre", ignore = true)
    @Mapping(target = "artists", ignore = true)
    Song toSong(SongRequest request);

    @Mapping(target = "album", ignore = true)
    @Mapping(target = "artists", ignore = true)
    SongResponse toSongResponse(Song song);

    SongBasic toSongBasic(Song song);

    @Mapping(target = "album", ignore = true)
    @Mapping(target = "genre", ignore = true)
    @Mapping(target = "artists", ignore = true)
    Song update(@MappingTarget Song song, SongRequest request);
}
