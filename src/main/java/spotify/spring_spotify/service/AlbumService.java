package spotify.spring_spotify.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import spotify.spring_spotify.dto.basic.ArtistBasic;
import spotify.spring_spotify.dto.basic.SongBasic;
import spotify.spring_spotify.dto.request.AlbumRequest;
import spotify.spring_spotify.dto.response.AlbumResponse;
import spotify.spring_spotify.dto.response.ArtistResponse;
import spotify.spring_spotify.dto.response.PageResponse;
import spotify.spring_spotify.entity.Album;
import spotify.spring_spotify.entity.Artist;
import spotify.spring_spotify.entity.Song;
import spotify.spring_spotify.exception.ErrorCode;
import spotify.spring_spotify.exception.FileException;
import spotify.spring_spotify.exception.SpotifyException;
import spotify.spring_spotify.mapper.AlbumMapper;
import spotify.spring_spotify.mapper.ArtistMapper;
import spotify.spring_spotify.mapper.SongMapper;
import spotify.spring_spotify.repository.AlbumRepository;
import spotify.spring_spotify.repository.ArtistRepository;
import spotify.spring_spotify.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spotify.spring_spotify.specification.AlbumSpecification;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@RequiredArgsConstructor
@Service
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final SongRepository songRepository;
    private final SongMapper songMapper;
    private final AlbumMapper albumMapper;
    private final ArtistMapper artistMapper;
    private final FileService fileService;

    @PreAuthorize("hasRole('ADMIN')")
    public AlbumResponse create(AlbumRequest request, MultipartFile multipartFile) throws FileException, IOException {
       if(albumRepository.existsByName(request.getName())){
           throw new SpotifyException(ErrorCode.ALBUM_EXISTED);
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

        // Song
        if (request.getSongs() != null && !request.getSongs().isEmpty()) {
            List<Song> songList = songRepository
                    .findAllByNameIn(new ArrayList<>(request.getSongs()));
            if(songList != null){
                album.setSongs(new HashSet<>(songList));
                songList.forEach(song -> song.setAlbum(album));
                album.setTotalTracks(songList.size());
                for(Song song : songList){
                    album.setTotalHours(album.getTotalHours() + song.getDuration());
                }
            }
        }
        else{
            album.setSongs(new HashSet<>());
        }
        album.setFollower(album.getFollower() + 1);

        // Image
        if (multipartFile != null && !multipartFile.isEmpty()) {
            List<String> allowedFileExtensions = new ArrayList<>
                    (Arrays.asList("jpg", "jpeg", "png", "gif", "bmp","svg"));
            album.setImageURL(fileService.uploadFile(multipartFile, allowedFileExtensions).getUrl());
        }

       return convertAlbumResponse(albumRepository.save(album));
    }
    public AlbumResponse fetchById(long id){
        Album album = albumRepository.findById(id).orElseThrow(() -> new SpotifyException(ErrorCode.ALBUM_NOT_EXISTED));

        album.setFollower(album.getFollower() + 1);

        return convertAlbumResponse(albumRepository.save(album));
    }


    public PageResponse<AlbumResponse> fetchAllAlbums(int pageNo,int pageSize, String nameSortOrder){
        pageNo = pageNo - 1;

        Sort sort = (nameSortOrder.equalsIgnoreCase("asc"))
                ? Sort.by(Sort.Order.asc("name"))
                : Sort.by(Sort.Order.desc("name"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Album> albumPage = albumRepository.findAll(pageable);
        if (albumPage.isEmpty()) {
            throw new SpotifyException(ErrorCode.ALBUM_NOT_FOUND);
        }

        List<AlbumResponse> responses =  convertListAlbumResponse(albumPage.getContent());

        return PageResponse.<AlbumResponse>builder()
                .page(albumPage.getNumber() + 1)  // Thêm 1 để bắt đầu từ trang 1
                .size(albumPage.getSize())
                .totalPages(albumPage.getTotalPages())
                .totalItems(albumPage.getTotalElements())
                .items(responses)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public AlbumResponse update(long id, AlbumRequest request, MultipartFile multipartFile) throws FileException, IOException, SAXException {
        Album albumDB = albumRepository.findById(id).orElseThrow(() -> new SpotifyException(ErrorCode.ALBUM_NOT_EXISTED));
        Album album = albumMapper.update(albumDB,request);

        // Artist
        if (request.getArtists() != null && !request.getArtists().isEmpty()) {
            List<Artist> artistList = artistRepository
                    .findAllByNameIn(new ArrayList<>(request.getArtists()));
            album.setArtists(new HashSet<>(artistList));
        }else{
            album.setArtists(new HashSet<>());
        }

        // Song
        if (request.getSongs() != null && !request.getSongs().isEmpty()) {
            List<Song> songList = songRepository
                    .findAllByNameIn(new ArrayList<>(request.getSongs()));
            Set<Song> albumSongs = album.getSongs();
            albumSongs.addAll(songList);
                album.setSongs(albumSongs);
                songList.forEach(song -> song.setAlbum(album));
            album.setTotalTracks(songList.size());
            for(Song song : songList){
                album.setTotalHours(album.getTotalHours() + song.getDuration());
            }
        }
        else{
            album.setSongs(new HashSet<>());
        }

        album.setFollower(album.getFollower() + 1);

        // Image
        if (multipartFile != null && !multipartFile.isEmpty()) {
            List<String> allowedFileExtensions = new ArrayList<>
                    (Arrays.asList("jpg", "jpeg", "png", "gif", "bmp","svg"));
            album.setImageURL(fileService.uploadFile(multipartFile, allowedFileExtensions).getUrl());
        }

        return convertAlbumResponse(albumRepository.save(album));
    }

    public PageResponse<AlbumResponse> searchAlbums(String name, int pageNo, int pageSize){
        pageNo = pageNo - 1;

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Specification<Album> spec = Specification.where(AlbumSpecification.hasNameContainingIgnoreCase(name))
                .and(AlbumSpecification.sortByNamePriority(name));

        Page<Album> albumPage = albumRepository.findAll(spec, pageable);

        if (albumPage.isEmpty()) {
            throw new SpotifyException(ErrorCode.ALBUM_NOT_EXISTED);
        }

        // convert
        List<AlbumResponse> albumResponse = convertListAlbumResponse(albumPage.getContent());

        return PageResponse.<AlbumResponse>builder()
                .page(albumPage.getNumber() + 1)  // Thêm 1 để bắt đầu từ trang 1
                .size(albumPage.getSize())
                .totalPages(albumPage.getTotalPages())
                .totalItems(albumPage.getTotalElements())
                .items(albumResponse)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(long id){
        Album albumDB = albumRepository.findById(id)
                .orElseThrow(() -> new SpotifyException(ErrorCode.ALBUM_NOT_EXISTED));
        if(albumDB.getSongs() != null){
            Set<Song> songSet = albumDB.getSongs();
            songRepository.deleteAll(songSet);
        }
        albumRepository.delete(albumDB);
    }

    public void deleteSongFromAlbum(long albumId, long songId) {
        Album albumDB = albumRepository.findById(albumId)
                .orElseThrow(() -> new SpotifyException(ErrorCode.ALBUM_NOT_EXISTED));

        Song songToDelete = songRepository.findById(songId)
                .orElseThrow(() -> new SpotifyException(ErrorCode.SONG_NOT_EXISTED));

        Set<Song> songSet = albumDB.getSongs();
        if (songSet != null && songSet.contains(songToDelete)) {
            songSet.remove(songToDelete);
            albumDB.setSongs(songSet);

            albumRepository.save(albumDB);

        } else {
            throw new SpotifyException(ErrorCode.SONG_NOT_IN_ALBUM);
        }
    }

    public AlbumResponse convertAlbumResponse(Album album) {
        AlbumResponse response = albumMapper.toAlbumResponse(album);

        Set<ArtistBasic> artistBasicList = album.getArtists()
                .stream().map(artistMapper::toArtistBasic).collect(Collectors.toSet());
        response.setArtists(artistBasicList);

        Set<SongBasic> songBasicList = album.getSongs()
                .stream().map(songMapper::toSongBasic).collect(Collectors.toSet());
        response.setSongs(songBasicList);

        return response;
    }

    public List<AlbumResponse> convertListAlbumResponse(List<Album> albumList){
        List<AlbumResponse> albumResponseList = new ArrayList<>();
        for(Album album : albumList){
            AlbumResponse response = convertAlbumResponse(album);
            albumResponseList.add(response);
        }
        return albumResponseList;
    }
}
