package spotify.spring_spotify.service;

import spotify.spring_spotify.dto.basic.AlbumBasic;
import spotify.spring_spotify.dto.basic.SongBasic;
import spotify.spring_spotify.dto.request.ArtistRequest;
import spotify.spring_spotify.dto.response.ArtistResponse;
import spotify.spring_spotify.entity.Album;
import spotify.spring_spotify.entity.Artist;
import spotify.spring_spotify.entity.Song;
import spotify.spring_spotify.exception.SpotifyException;
import spotify.spring_spotify.mapper.AlbumMapper;
import spotify.spring_spotify.mapper.ArtistMapper;
import spotify.spring_spotify.mapper.SongMapper;
import spotify.spring_spotify.repository.AlbumRepository;
import spotify.spring_spotify.repository.ArtistRepository;
import spotify.spring_spotify.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final SongRepository songRepository;
    private final SongMapper songMapper;
    private final AlbumMapper albumMapper;
    private final ArtistMapper artistMapper;
    public ArtistResponse create(ArtistRequest request){
       if(artistRepository.existsByName(request.getName())){
           throw new SpotifyException("Artist đã tồn tại");
       }
       Artist artist = artistMapper.toArtist(request);

       // Album
       if(request.getAlbums() != null && !request.getAlbums().isEmpty()){
           List<Album> albumList = albumRepository
                   .findAllByNameIn(new ArrayList<>(request.getAlbums()));
           artist.setAlbums(new HashSet<>(albumList));
       }
       else{
           artist.setAlbums(new HashSet<>());
       }

       // Song
        if(request.getSongs() != null && !request.getSongs().isEmpty()){
            List<Song> songList = songRepository
                    .findAllByNameIn(new ArrayList<>(request.getSongs()));
            artist.setSongs(new HashSet<>(songList));
        }
        else {
            artist.setSongs(new HashSet<>());
        }

       ArtistResponse response = artistMapper.toArtistResponse(artistRepository.save(artist));

       Set<AlbumBasic> albumBasicList = artist.getAlbums()
               .stream().map(albumMapper::toAlbumBasic).collect(Collectors.toSet());
       response.setAlbums(albumBasicList);

        Set<SongBasic> songBasicList = artist.getSongs()
                .stream().map(songMapper::toSongBasic).collect(Collectors.toSet());
        response.setSongs(songBasicList);

        return response;
    }
    public ArtistResponse fetchById(long id){
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new SpotifyException("Artist không tồn tại"));

        ArtistResponse response = artistMapper.toArtistResponse(artist);

        List<AlbumBasic> albumBasicList = artist.getAlbums().stream().map(albumMapper::toAlbumBasic).toList();
        response.setAlbums(new HashSet<>(albumBasicList));

        List<SongBasic> songBasicList = artist.getSongs().stream().map(songMapper::toSongBasic).toList();
        response.setSongs(new HashSet<>(songBasicList));
        return response;
    }

    public ArtistResponse update(long id, ArtistRequest request){
        Artist artistDB = artistRepository.findById(id)
                .orElseThrow(() -> new SpotifyException("Artist không tồn tại"));
        Artist artist = artistMapper.update(artistDB,request);

        // Album
        if(request.getAlbums() != null && !request.getAlbums().isEmpty()){
            List<Album> albumList = albumRepository
                    .findAllByNameIn(new ArrayList<>(request.getAlbums()));
            artist.setAlbums(new HashSet<>(albumList));
        }
        else{
            artist.setAlbums(new HashSet<>());
        }

        // Song
        if(request.getSongs() != null && !request.getSongs().isEmpty()){
            List<Song> songList = songRepository
                    .findAllByNameIn(new ArrayList<>(request.getSongs()));
            artist.setSongs(new HashSet<>(songList));
        }
        else {
            artist.setSongs(new HashSet<>());
        }

        ArtistResponse response = artistMapper.toArtistResponse(artistRepository.save(artist));

        Set<AlbumBasic> albumBasicList = artist.getAlbums()
                .stream().map(albumMapper::toAlbumBasic).collect(Collectors.toSet());
        response.setAlbums(albumBasicList);

        Set<SongBasic> songBasicList = artist.getSongs()
                .stream().map(songMapper::toSongBasic).collect(Collectors.toSet());
        response.setSongs(songBasicList);

        return response;
    }

    public void delete(long id){
        Artist artistDB = artistRepository.findById(id)
                .orElseThrow(() -> new SpotifyException("Artist không tồn tại"));

        artistDB.getAlbums().forEach(album -> album.getArtists().remove(artistDB));

        artistDB.getSongs().forEach(song -> song.getArtists().remove(artistDB));

        artistRepository.delete(artistDB);
    }
}
