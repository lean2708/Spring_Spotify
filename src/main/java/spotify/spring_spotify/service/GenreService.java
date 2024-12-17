package spotify.spring_spotify.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import spotify.spring_spotify.dto.basic.SongBasic;
import spotify.spring_spotify.dto.request.GenreRequest;
import spotify.spring_spotify.dto.response.GenreResponse;
import spotify.spring_spotify.dto.response.PageResponse;
import spotify.spring_spotify.dto.response.PlaylistResponse;
import spotify.spring_spotify.entity.Genre;
import spotify.spring_spotify.entity.Playlist;
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

    public PageResponse<GenreResponse> fetchAllGenres(int pageNo,int pageSize, String nameSortOrder){
        pageNo = pageNo - 1;

        Sort sort = (nameSortOrder.equalsIgnoreCase("asc"))
                ? Sort.by(Sort.Order.asc("name"))
                : Sort.by(Sort.Order.desc("name"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Genre> genrePage = genreRepository.findAll(pageable);
        if (genrePage.isEmpty()) {
            throw new SpotifyException(ErrorCode.GENRE_NOT_FOUND);
        }

        List<GenreResponse> genreResponses = new ArrayList<>();
        for (Genre genre : genrePage.getContent()){
            GenreResponse response = genreMapper.toGenreResponse(genre);
            genreResponses.add(response);
        }

        return PageResponse.<GenreResponse>builder()
                .page(genrePage.getNumber() + 1)  // Thêm 1 để bắt đầu từ trang 1
                .size(genrePage.getSize())
                .totalPages(genrePage.getTotalPages())
                .totalItems(genrePage.getTotalElements())
                .items(genreResponses)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
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
