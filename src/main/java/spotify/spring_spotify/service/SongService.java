package spotify.spring_spotify.service;

import spotify.spring_spotify.dto.basic.ArtistBasic;
import spotify.spring_spotify.dto.request.SongRequest;
import spotify.spring_spotify.dto.response.SongResponse;
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

import java.util.*;

@RequiredArgsConstructor
@Service
public class SongService {
    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final GenreRepository genreRepository;
    private final ArtistMapper artistMapper;
    private final GenreMapper genreMapper;
    private final AlbumMapper albumMapper;
    private final SongMapper songMapper;


    public SongResponse create(SongRequest request){
        if(songRepository.existsByName(request.getName())){
            throw new SpotifyException("Song đã tồn tại");
        }
       Song song = songMapper.toSong(request);

        Set<Song> songSet = new HashSet<>();
        songSet.add(song);

        // Album
        if(request.getAlbum() != null && !request.getAlbum().isEmpty()){
           Album album = albumRepository.findByName(request.getAlbum());
           if(album != null){
               song.setAlbum(album);
               album.setSongs(songSet);
           }
        }

        // Artist
        if(request.getArtists() != null && !request.getArtists().isEmpty()){
            List<Artist> artistList = artistRepository
                    .findAllByNameIn(new ArrayList<>(request.getArtists()));
                song.setArtists(new HashSet<>(artistList));
        }else{
            song.setArtists(new HashSet<>());
        }

        // Genre
        if(request.getGenre() != null && !request.getGenre().isEmpty()){
            Genre genre = genreRepository.findByName(request.getGenre());
            if(genre != null){
                song.setGenre(genre);
                genre.setSongs(songSet);
            }
        }

       SongResponse response = songMapper.toSongResponse(songRepository.save(song));

        response.setAlbum(Optional.ofNullable(song.getAlbum())
                .map(albumMapper::toAlbumBasic).orElse(null));

        List<ArtistBasic> artistBasicList = song.getArtists()
                .stream().map(artistMapper::toArtistBasic).toList();
        response.setArtists(new HashSet<>(artistBasicList));

        response.setGenre(Optional.ofNullable(song.getGenre())
                .map(genreMapper::toGenreBasic).orElse(null));

        return  response;
    }

    public SongResponse fetchById(long id){
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new SpotifyException("Song không tồn tại"));
        SongResponse response = songMapper.toSongResponse(song);

        response.setAlbum(albumMapper.toAlbumBasic(song.getAlbum()));

        Set<ArtistBasic> artistBasicSet = new HashSet<>();
        for(Artist artist : song.getArtists()){
            artistBasicSet.add(artistMapper.toArtistBasic(artist));
        }
        response.setArtists(new HashSet<>(artistBasicSet));

        return response;
    }

    public SongResponse update(long id, SongRequest request){
        Song songDB = songRepository.findById(id)
                .orElseThrow(() -> new SpotifyException("Song không tồn tại"));
        Song song  = songMapper.update(songDB,request);

        Set<Song> songSet = new HashSet<>();
        songSet.add(song);
        // Album
        if(request.getAlbum() != null && !request.getAlbum().isEmpty()){
            Album album = albumRepository.findByName(request.getAlbum());
            if(album != null){
                song.setAlbum(album);
                album.setSongs(songSet);
            }
        }

        // Artist
        if(request.getArtists() != null && !request.getArtists().isEmpty()){
            List<Artist> artistList = artistRepository
                    .findAllByNameIn(new ArrayList<>(request.getArtists()));
            song.setArtists(new HashSet<>(artistList));
        }else{
            song.setArtists(new HashSet<>());
        }

        // Genre
        if(request.getGenre() != null && !request.getGenre().isEmpty()){
            Genre genre = genreRepository.findByName(request.getGenre());
            if(genre != null){
                song.setGenre(genre);
                genre.setSongs(songSet);
            }
        }

        SongResponse response = songMapper.toSongResponse(songRepository.save(song));

        response.setAlbum(Optional.ofNullable(song.getAlbum())
                .map(albumMapper::toAlbumBasic).orElse(null));

        List<ArtistBasic> artistBasicList = song.getArtists()
                .stream().map(artistMapper::toArtistBasic).toList();
        response.setArtists(new HashSet<>(artistBasicList));

        response.setGenre(Optional.ofNullable(song.getGenre())
                .map(genreMapper::toGenreBasic).orElse(null));

        return  response;
    }

    public void delete(long id){
        Song songDB = songRepository.findById(id)
                .orElseThrow(() -> new SpotifyException("Song không tồn tại"));

        songRepository.delete(songDB);
    }
}
