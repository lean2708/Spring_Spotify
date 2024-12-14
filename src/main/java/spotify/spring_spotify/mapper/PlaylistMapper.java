package spotify.spring_spotify.mapper;

import spotify.spring_spotify.dto.basic.PlaylistBasic;
import spotify.spring_spotify.dto.request.PlaylistRequest;
import spotify.spring_spotify.dto.response.PlaylistResponse;
import spotify.spring_spotify.entity.Playlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PlaylistMapper {
    @Mapping(target = "songs", ignore = true)
    Playlist toPlaylist(PlaylistRequest request);
    @Mapping(target = "songs", ignore = true)
    PlaylistResponse toPlaylistResponse(Playlist playlist);

    PlaylistBasic toPlaylistBasic(Playlist playlist);
    @Mapping(target = "songs", ignore = true)
    Playlist update(@MappingTarget Playlist playlist, PlaylistRequest request);
}
