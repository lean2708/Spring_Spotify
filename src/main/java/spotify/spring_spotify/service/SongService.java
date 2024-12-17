package spotify.spring_spotify.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import spotify.spring_spotify.dto.basic.ArtistBasic;
import spotify.spring_spotify.dto.request.SongRequest;
import spotify.spring_spotify.dto.response.PageResponse;
import spotify.spring_spotify.dto.response.SongResponse;
import spotify.spring_spotify.entity.*;
import spotify.spring_spotify.exception.ErrorCode;
import spotify.spring_spotify.exception.FileException;
import spotify.spring_spotify.exception.SpotifyException;
import spotify.spring_spotify.mapper.AlbumMapper;
import spotify.spring_spotify.mapper.ArtistMapper;
import spotify.spring_spotify.mapper.SongMapper;
import spotify.spring_spotify.repository.AlbumRepository;
import spotify.spring_spotify.repository.ArtistRepository;
import spotify.spring_spotify.repository.GenreRepository;
import spotify.spring_spotify.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spotify.spring_spotify.specification.SongSpecification;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class SongService {
    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final GenreRepository genreRepository;
    private final ArtistMapper artistMapper;
    private final FileService fileService;
    private final AlbumMapper albumMapper;
    private final SongMapper songMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public SongResponse create(SongRequest request, MultipartFile image, MultipartFile fileSong) throws FileException, IOException,  SAXException {
        if(songRepository.existsByName(request.getName())){
            throw new SpotifyException(ErrorCode.SONG_EXISTED);
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
               album.setTotalTracks(album.getTotalTracks() + 1);
               album.setTotalHours(album.getTotalHours() + song.getDuration());
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
            song.setGenre(genre);
        }

        song.setListener(song.getListener() + 1);

        // Image
        if (image != null && !image.isEmpty()) {
            List<String> allowedFileImageExtensions = new ArrayList<>
                    (Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "svg"));
            song.setImageURL(fileService.uploadFile(image, allowedFileImageExtensions).getUrl());
        }

        // File song
        if (fileSong != null && !fileSong.isEmpty()) {
            List<String> allowedFileSongExtensions = new ArrayList<>
                    (Arrays.asList("mp3", "wav", "aac", "flac", "ogg", "avi", "mov", "mkv", "flv", "wmv", "m4a"));
            song.setFileSongURL(fileService.uploadFile(fileSong, allowedFileSongExtensions).getUrl());
        }

       return convertSongResponse(songRepository.save(song));
    }

    public SongResponse fetchById(long id){
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new SpotifyException(ErrorCode.SONG_NOT_EXISTED));

        song.setListener(song.getListener() + 1);

        return convertSongResponse(songRepository.save(song));
    }

    public PageResponse<SongResponse> fetchAllSongs(int pageNo,int pageSize, String nameSortOrder){
        pageNo = pageNo - 1;

        Sort sort = (nameSortOrder.equalsIgnoreCase("asc"))
                ? Sort.by(Sort.Order.asc("name"))
                : Sort.by(Sort.Order.desc("name"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Song> songPage = songRepository.findAll(pageable);
        if (songPage.isEmpty()) {
            throw new SpotifyException(ErrorCode.SONG_NOT_FOUND);
        }

        List<SongResponse> responses = convertListSongResponse(songPage.getContent());

        return PageResponse.<SongResponse>builder()
                .page(songPage.getNumber() + 1)  // Thêm 1 để bắt đầu từ trang 1
                .size(songPage.getSize())
                .totalPages(songPage.getTotalPages())
                .totalItems(songPage.getTotalElements())
                .items(responses)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public SongResponse update(long id, SongRequest request,
                               MultipartFile image, MultipartFile fileSong) throws FileException, IOException,  SAXException {
        Song songDB = songRepository.findById(id)
                .orElseThrow(() -> new SpotifyException(ErrorCode.SONG_NOT_EXISTED));

        if(songDB.getAlbum() != null) {
            songDB.getAlbum().setTotalHours(songDB.getAlbum().getTotalHours() - songDB.getDuration());
        }

        Song song = songMapper.update(songDB, request);
        Set<Song> songSet = new HashSet<>();
        songSet.add(song);
        // Album
        if(request.getAlbum() != null && !request.getAlbum().isEmpty()){
            Album album = albumRepository.findByName(request.getAlbum());
            if(album != null){
                song.setAlbum(album);
                album.setSongs(songSet);
                album.setTotalHours(album.getTotalHours() + song.getDuration()/3600);
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
            song.setGenre(genre);
        }

        song.setListener(song.getListener() + 1);

        // Image
        if (image != null && !image.isEmpty()) {
            List<String> allowedFileImageExtensions = new ArrayList<>
                    (Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "svg"));
            song.setImageURL(fileService.uploadFile(image, allowedFileImageExtensions).getUrl());
        }

        // File song
         if (fileSong != null && !fileSong.isEmpty()) {
             List<String> allowedFileSongExtensions = new ArrayList<>
                     (Arrays.asList("mp3", "wav", "aac", "flac", "ogg", "avi", "mov", "mkv", "flv", "wmv"));
             song.setFileSongURL(fileService.uploadFile(fileSong, allowedFileSongExtensions).getUrl());
         }

        return songMapper.toSongResponse(songRepository.save(song));
    }

    public PageResponse<SongResponse> searchSongs(String name, int pageNo, int pageSize){
        pageNo = pageNo - 1;

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Specification<Song> spec = Specification.where(SongSpecification.hasNameContainingIgnoreCase(name))
                .and(SongSpecification.sortByNamePriority(name));

        Page<Song> songPage = songRepository.findAll(spec, pageable);

        if (songPage.isEmpty()) {
            throw new SpotifyException(ErrorCode.SONG_NOT_EXISTED);
        }

        // convert
        List<SongResponse> playlistResponses = convertListSongResponse(songPage.getContent());

        return PageResponse.<SongResponse>builder()
                .page(songPage.getNumber() + 1)  // Thêm 1 để bắt đầu từ trang 1
                .size(songPage.getSize())
                .totalPages(songPage.getTotalPages())
                .totalItems(songPage.getTotalElements())
                .items(playlistResponses)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(long id){
        Song songDB = songRepository.findById(id)
                .orElseThrow(() -> new SpotifyException(ErrorCode.SONG_NOT_EXISTED));

        songRepository.delete(songDB);
    }

    public PageResponse<SongResponse> fetchAllSongSortedByViewer( int pageNo, int pageSize, String viewerSortOrder) {
        pageNo = pageNo - 1;

        Sort sort = (viewerSortOrder.equalsIgnoreCase("asc"))
                ? Sort.by(Sort.Order.asc("listener"))
                : Sort.by(Sort.Order.desc("listener"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Song> songPage = songRepository.findAll(pageable);

        if (songPage.isEmpty()) {
            throw new SpotifyException(ErrorCode.SONG_NOT_EXISTED);
        }

        List<SongResponse> playlistResponses = convertListSongResponse(songPage.getContent());

        return PageResponse.<SongResponse>builder()
                .page(songPage.getNumber() + 1)  // Thêm 1 để bắt đầu từ trang 1
                .size(songPage.getSize())
                .totalPages(songPage.getTotalPages())
                .totalItems(songPage.getTotalElements())
                .items(playlistResponses)
                .build();
    }

    public List<SongResponse> fetchSongsByGenre(long genreId) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new SpotifyException(ErrorCode.GENRE_NOT_EXISTED));
        List<Song> songList = songRepository.findAllByGenre(genre);
        if (songList.isEmpty()) {
            throw new SpotifyException(ErrorCode.SONG_NOT_FOUND_FOR_GENRE);
        }

        return convertListSongResponse(songList);
    }

    public SongResponse convertSongResponse(Song song){
        SongResponse response = songMapper.toSongResponse(song);

        response.setAlbum(Optional.ofNullable(song.getAlbum())
                .map(albumMapper::toAlbumBasic).orElse(null));

        List<ArtistBasic> artistBasicList = song.getArtists()
                .stream().map(artistMapper::toArtistBasic).toList();
        response.setArtists(new HashSet<>(artistBasicList));

        return response;
    }

    public List<SongResponse> convertListSongResponse(List<Song> songList){
        List<SongResponse> songResponseList = new ArrayList<>();
        for(Song song : songList){
            SongResponse response = convertSongResponse(song);
            songResponseList.add(response);
        }
        return songResponseList;
    }
}
