package spotify.spring_spotify.mapper;

import spotify.spring_spotify.dto.basic.AlbumBasic;
import spotify.spring_spotify.dto.request.AlbumRequest;
import spotify.spring_spotify.dto.response.AlbumResponse;
import spotify.spring_spotify.entity.Album;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AlbumMapper {
    @Mapping(target = "artists", ignore = true)
    @Mapping(target = "songs", ignore = true)
    Album toAlbum(AlbumRequest request);

    @Mapping(target = "artists", ignore = true)
    @Mapping(target = "songs", ignore = true)
    AlbumResponse toAlbumResponse(Album album);


    AlbumBasic toAlbumBasic(Album album);

    @Mapping(target = "artists", ignore = true)
    @Mapping(target = "songs", ignore = true)
    Album update(@MappingTarget Album  album, AlbumRequest request);
}
