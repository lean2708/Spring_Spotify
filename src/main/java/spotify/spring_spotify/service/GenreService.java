package spotify.spring_spotify.service;

import org.springframework.security.access.prepost.PreAuthorize;
import spotify.spring_spotify.dto.basic.SongBasic;
import spotify.spring_spotify.dto.request.GenreRequest;
import spotify.spring_spotify.dto.response.GenreResponse;
import spotify.spring_spotify.entity.Genre;
import spotify.spring_spotify.entity.Song;
import spotify.spring_spotify.exception.ErrorCode;
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

    @PreAuthorize("hasRole('ADMIN')")
    public GenreResponse create(GenreRequest request){
        if(genreRepository.existsByName(request.getName())){
            throw new SpotifyException(ErrorCode.GENRE_EXISTED);
        }
        Genre genre = genreMapper.toGenre(request);

        return genreMapper.toGenreResponse(genreRepository.save(genre));
    }
    public GenreResponse fetchById(long id){
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new SpotifyException(ErrorCode.GENRE_NOT_EXISTED));

        return genreMapper.toGenreResponse(genre);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<GenreResponse> fetchAllGenre(){
        List<Genre> genres = genreRepository.findAll();

        List<GenreResponse> genreResponses = new ArrayList<>();
        for (Genre genre : genres){
            GenreResponse response = genreMapper.toGenreResponse(genre);
            genreResponses.add(response);
        }

        return genreResponses;
    }

    public GenreResponse update(long id, GenreRequest request){
        Genre genreDB = genreRepository.findById(id)
                .orElseThrow(() -> new SpotifyException(ErrorCode.GENRE_NOT_EXISTED));

        Genre genre = genreMapper.update(genreDB,request);

        return genreMapper.toGenreResponse(genreRepository.save(genre));
    }
    @PreAuthorize("hasRole('ADMIN')")

    public void delete(long id){
        Genre genreDB = genreRepository.findById(id)
                .orElseThrow(() -> new SpotifyException(ErrorCode.GENRE_NOT_EXISTED));

        genreRepository.delete(genreDB);
    }
}
