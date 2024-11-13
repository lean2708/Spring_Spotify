package spotify.spring_spotify.service;

import spotify.spring_spotify.dto.basic.SongBasic;
import spotify.spring_spotify.dto.request.GenreRequest;
import spotify.spring_spotify.dto.response.GenreResponse;
import spotify.spring_spotify.entity.Genre;
import spotify.spring_spotify.entity.Song;
import spotify.spring_spotify.exception.SpotifyException;
import spotify.spring_spotify.mapper.GenreMapper;
import spotify.spring_spotify.mapper.SongMapper;
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
public class GenreService {
    private final GenreMapper genreMapper;
    private final SongRepository songRepository;
    private final SongMapper songMapper;
    private final GenreRepository genreRepository;

    public GenreResponse create(GenreRequest request){
        if(genreRepository.existsByName(request.getName())){
            throw new SpotifyException("Genre đã tồn tại");
        }
        Genre genre = genreMapper.toGenre(request);

        if (request.getSongs() != null && !request.getSongs().isEmpty()) {
            List<Song> songList = songRepository
                    .findAllByNameIn(new ArrayList<>(request.getSongs()));
            if(songList != null){
                genre.setSongs(new HashSet<>(songList));
                songList.forEach(song -> song.setGenre(genre));
            }
        }else {
            genre.setSongs(new HashSet<>());
        }

        GenreResponse response = genreMapper.toGenreResponse(genreRepository.save(genre));

        Set<SongBasic> songBasicList = genre.getSongs()
                .stream().map(songMapper::toSongBasic).collect(Collectors.toSet());
        response.setSongs(songBasicList);

        return response;
    }
    public GenreResponse fetchById(long id){
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new SpotifyException("Genre không tồn tại"));

        GenreResponse response = genreMapper.toGenreResponse(genre);
        List<SongBasic> songBasicList = genre.getSongs()
                .stream().map(songMapper::toSongBasic).toList();
        response.setSongs(new HashSet<>(songBasicList));

        return response;
    }

    public GenreResponse update(long id, GenreRequest request){
        Genre genreDB = genreRepository.findById(id)
                .orElseThrow(() -> new SpotifyException("Genre không tồn tại"));

        Genre genre = genreMapper.update(genreDB,request);

        if (request.getSongs() != null && !request.getSongs().isEmpty()) {
            List<Song> songList = songRepository
                    .findAllByNameIn(new ArrayList<>(request.getSongs()));
            if(songList != null){
                genre.setSongs(new HashSet<>(songList));
                songList.forEach(song -> song.setGenre(genre));
            }
        }else {
            genre.setSongs(new HashSet<>());
        }

        GenreResponse response = genreMapper.toGenreResponse(genreRepository.save(genre));

        Set<SongBasic> songBasicList = genre.getSongs()
                .stream().map(songMapper::toSongBasic).collect(Collectors.toSet());
        response.setSongs(songBasicList);

        return response;
    }

    public void delete(long id){
        Genre genreDB = genreRepository.findById(id)
                .orElseThrow(() -> new SpotifyException("Genre không tồn tại"));

        genreRepository.delete(genreDB);
    }
}
