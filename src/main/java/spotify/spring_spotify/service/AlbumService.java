package spotify.spring_spotify.service;

import spotify.spring_spotify.dto.basic.ArtistBasic;
import spotify.spring_spotify.dto.basic.GenreBasic;
import spotify.spring_spotify.dto.basic.SongBasic;
import spotify.spring_spotify.dto.request.AlbumRequest;
import spotify.spring_spotify.dto.response.AlbumResponse;
import spotify.spring_spotify.entity.Album;
import spotify.spring_spotify.entity.Artist;
import spotify.spring_spotify.entity.Genre;
import spotify.spring_spotify.entity.Song;
import spotify.spring_spotify.exception.SpotifyException;
import spotify.spring_spotify.mapper.AlbumMapper;
import spotify.spring_spotify.mapper.ArtistMapper;
import spotify.spring_spotify.mapper.GenreMapper;
import spotify.spring_spotify.mapper.SongMapper;
import spotify.spring_spotify.repository.AlbumRepository;
import spotify.spring_spotify.repository.ArtistRepository;
import spotify.spring_spotify.repository.GenreRepository;
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
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final GenreRepository genreRepository;
    private final SongRepository songRepository;
    private final SongMapper songMapper;
    private final AlbumMapper albumMapper;
    private final GenreMapper genreMapper;
    private final ArtistMapper artistMapper;
    public AlbumResponse create(AlbumRequest request){
       if(albumRepository.existsByName(request.getName())){
           throw new SpotifyException("Album đã tồn tại");
       }
       Album album = albumMapper.toAlbum(request);

       // Artist
        if (request.getArtists() != null && !request.getArtists().isEmpty()) {
            List<Artist> artistList = artistRepository
                    .findAllByNameIn(new ArrayList<>(request.getArtists()));
            album.setArtists(new HashSet<>(artistList));
        }else{
            album.setArtists(new HashSet<>());
        }

       // Genre
        if (request.getGenres() != null && !request.getGenres().isEmpty()) {
            List<Genre> genreList = genreRepository
                    .findAllByNameIn(new ArrayList<>(request.getGenres()));
            album.setGenres(new HashSet<>(genreList));
        }else{
            album.setGenres(new HashSet<>());
        }

        // Song
        if (request.getSongs() != null && !request.getSongs().isEmpty()) {
            List<Song> songList = songRepository
                    .findAllByNameIn(new ArrayList<>(request.getSongs()));
            if(songList != null){
                album.setSongs(new HashSet<>(songList));
                songList.forEach(song -> song.setAlbum(album));
            }
        }
        else{
            album.setSongs(new HashSet<>());
        }

       AlbumResponse response = albumMapper.toAlbumResponse(albumRepository.save(album));

        Set<ArtistBasic> artistBasicList = album.getArtists()
                .stream().map(artistMapper::toArtistBasic).collect(Collectors.toSet());
        response.setArtists(artistBasicList);

        Set<SongBasic> songBasicList = album.getSongs()
                .stream().map(songMapper::toSongBasic).collect(Collectors.toSet());
        response.setSongs(new HashSet<>(songBasicList));

        Set<GenreBasic> genreBasicSet = album.getGenres()
                .stream().map(genreMapper::toGenreBasic).collect(Collectors.toSet());
        response.setGenres(genreBasicSet);

        return response;
    }
    public AlbumResponse fetchById(long id){
        Album albumDB = albumRepository.findById(id).orElseThrow(() -> new SpotifyException("Album không tồn tại"));

        AlbumResponse response = albumMapper.toAlbumResponse(albumDB);

        Set<ArtistBasic> artistBasicSet = albumDB.getArtists().stream().map(artistMapper::toArtistBasic).collect(Collectors.toSet());
        response.setArtists(artistBasicSet);

        Set<SongBasic> songBasicSet = albumDB.getSongs().stream().map(songMapper::toSongBasic).collect(Collectors.toSet());
        response.setSongs(songBasicSet);

        return response;
    }

    public AlbumResponse update(long id, AlbumRequest request){
        Album albumDB = albumRepository.findById(id).orElseThrow(() -> new SpotifyException("Album không tồn tại"));
        Album album = albumMapper.update(albumDB,request);

        // Artist
        if (request.getArtists() != null && !request.getArtists().isEmpty()) {
            List<Artist> artistList = artistRepository
                    .findAllByNameIn(new ArrayList<>(request.getArtists()));
            album.setArtists(new HashSet<>(artistList));
        }else{
            album.setArtists(new HashSet<>());
        }

        // Genre
        if (request.getGenres() != null && !request.getGenres().isEmpty()) {
            List<Genre> genreList = genreRepository
                    .findAllByNameIn(new ArrayList<>(request.getGenres()));
            album.setGenres(new HashSet<>(genreList));
        }else{
            album.setGenres(new HashSet<>());
        }

        // Song
        if (request.getSongs() != null && !request.getSongs().isEmpty()) {
            List<Song> songList = songRepository
                    .findAllByNameIn(new ArrayList<>(request.getSongs()));
            if(songList != null){
                album.setSongs(new HashSet<>(songList));
                songList.forEach(song -> song.setAlbum(album));
            }
        }
        else{
            album.setSongs(new HashSet<>());
        }

        AlbumResponse response = albumMapper.toAlbumResponse(albumRepository.save(album));

        Set<ArtistBasic> artistBasicList = album.getArtists()
                .stream().map(artistMapper::toArtistBasic).collect(Collectors.toSet());
        response.setArtists(artistBasicList);

        Set<SongBasic> songBasicList = album.getSongs()
                .stream().map(songMapper::toSongBasic).collect(Collectors.toSet());
        response.setSongs(new HashSet<>(songBasicList));

        Set<GenreBasic> genreBasicSet = album.getGenres()
                .stream().map(genreMapper::toGenreBasic).collect(Collectors.toSet());
        response.setGenres(genreBasicSet);
        return response;
    }

    public void delete(long id){
        Album albumDB = albumRepository.findById(id)
                .orElseThrow(() -> new SpotifyException("Album không tồn tại"));
        if(albumDB.getSongs() != null){
            Set<Song> songSet = albumDB.getSongs();
            songRepository.deleteAll(songSet);
        }
        albumRepository.delete(albumDB);
    }
}
