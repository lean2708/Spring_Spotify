package spotify.spring_spotify.mapper;

import spotify.spring_spotify.dto.basic.ArtistBasic;
import spotify.spring_spotify.dto.request.ArtistRequest;
import spotify.spring_spotify.dto.response.ArtistResponse;
import spotify.spring_spotify.entity.Artist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ArtistMapper {
    @Mapping(target = "songs", ignore = true)
    @Mapping(target = "albums", ignore = true)
    Artist toArtist(ArtistRequest request);

    @Mapping(target = "songs", ignore = true)
    @Mapping(target = "albums", ignore = true)
    ArtistResponse toArtistResponse(Artist artist);

    ArtistBasic toArtistBasic(Artist artist);

    @Mapping(target = "songs", ignore = true)
    @Mapping(target = "albums", ignore = true)
    Artist update(@MappingTarget Artist artist, ArtistRequest request);
}
