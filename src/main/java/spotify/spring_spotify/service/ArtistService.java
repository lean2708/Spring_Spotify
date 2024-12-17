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
import spotify.spring_spotify.dto.basic.AlbumBasic;
import spotify.spring_spotify.dto.basic.SongBasic;
import spotify.spring_spotify.dto.request.ArtistRequest;
import spotify.spring_spotify.dto.response.ArtistResponse;
import spotify.spring_spotify.dto.response.PageResponse;
import spotify.spring_spotify.dto.response.SongResponse;
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
import spotify.spring_spotify.specification.ArtistSpecification;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final SongRepository songRepository;
    private final SongMapper songMapper;
    private final AlbumMapper albumMapper;
    private final ArtistMapper artistMapper;
    private final FileService fileService;
    @PreAuthorize("hasRole('ADMIN')")
    public ArtistResponse create(ArtistRequest request, MultipartFile multipartFile) throws FileException, IOException, SAXException {
       if(artistRepository.existsByName(request.getName())){
           throw new SpotifyException(ErrorCode.ARTIST_EXISTED);
       }
       Artist artist = artistMapper.toArtist(request);

       // Album
       if(request.getAlbums() != null && !request.getAlbums().isEmpty()){
           List<Album> albumList = albumRepository
                   .findAllByNameIn(new ArrayList<>(request.getAlbums()));
           artist.setAlbums(new HashSet<>(albumList));
           albumList.forEach(album -> album.getArtists().add(artist));
       }
       else{
           artist.setAlbums(new HashSet<>());
       }

       // Song
        if(request.getSongs() != null && !request.getSongs().isEmpty()){
            List<Song> songList = songRepository
                    .findAllByNameIn(new ArrayList<>(request.getSongs()));
            artist.setSongs(new HashSet<>(songList));
            songList.forEach(song -> song.getArtists().add(artist));
        }
        else {
            artist.setSongs(new HashSet<>());
        }

        artist.setFollower(artist.getFollower() + 1);

        // Image
        if (multipartFile != null && !multipartFile.isEmpty()) {
            List<String> allowedFileExtensions = new ArrayList<>
                    (Arrays.asList("jpg", "jpeg", "png", "gif", "bmp","svg"));
            artist.setImageURL(fileService.uploadFile(multipartFile, allowedFileExtensions).getUrl());
        }

       return convertArtistToResponse(artistRepository.save(artist));
    }
    public ArtistResponse fetchById(long id){
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new SpotifyException(ErrorCode.ARTIST_NOT_EXISTED));

        artist.setFollower(artist.getFollower() + 1);

        return convertArtistToResponse(artistRepository.save(artist));
    }


    public PageResponse<ArtistResponse> fetchAllAritsts(int pageNo,int pageSize, String nameSortOrder){
        pageNo = pageNo - 1;

        Sort sort = (nameSortOrder.equalsIgnoreCase("asc"))
                ? Sort.by(Sort.Order.asc("name"))
                : Sort.by(Sort.Order.desc("name"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Artist> artistPage = artistRepository.findAll(pageable);
        if (artistPage.isEmpty()) {
            throw new SpotifyException(ErrorCode.ARTIST_NOT_FOUND);
        }

        List<ArtistResponse> responses =  convertListArtistToResponse(artistPage.getContent());

        return PageResponse.<ArtistResponse>builder()
                .page(artistPage.getNumber() + 1)  // Thêm 1 để bắt đầu từ trang 1
                .size(artistPage.getSize())
                .totalPages(artistPage.getTotalPages())
                .totalItems(artistPage.getTotalElements())
                .items(responses)
                .build();
    }

    public PageResponse<ArtistResponse> searchArtists(String name, int pageNo, int pageSize){
        pageNo = pageNo - 1;

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Specification<Artist> spec = Specification.where(ArtistSpecification.hasNameContainingIgnoreCase(name))
                .and(ArtistSpecification.sortByNamePriority(name));

        Page<Artist> artistPage = artistRepository.findAll(spec, pageable);
        if (artistPage.isEmpty()) {
            throw new SpotifyException(ErrorCode.ARTIST_NOT_EXISTED);
        }

        // convert
        List<ArtistResponse> artistResponses = convertListArtistToResponse(artistPage.getContent());

        return PageResponse.<ArtistResponse>builder()
                .page(artistPage.getNumber() + 1)  // Thêm 1 để bắt đầu từ trang 1
                .size(artistPage.getSize())
                .totalPages(artistPage.getTotalPages())
                .totalItems(artistPage.getTotalElements())
                .items(artistResponses)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ArtistResponse update(long id, ArtistRequest request, MultipartFile multipartFile) throws FileException, IOException, SAXException {
        Artist artistDB = artistRepository.findById(id)
                .orElseThrow(() -> new SpotifyException(ErrorCode.ARTIST_NOT_EXISTED));
        Artist artist = artistMapper.update(artistDB,request);

        Set<Artist> artistSet = new HashSet<>();
        artistSet.add(artist);

        // Album
        if(request.getAlbums() != null && !request.getAlbums().isEmpty()){
            List<Album> albumList = albumRepository
                    .findAllByNameIn(new ArrayList<>(request.getAlbums()));
            artist.setAlbums(new HashSet<>(albumList));
            albumList.forEach(album -> album.getArtists().add(artist));
        }
        else{
            artist.setAlbums(new HashSet<>());
        }

        // Song
        if(request.getSongs() != null && !request.getSongs().isEmpty()){
            List<Song> songList = songRepository
                    .findAllByNameIn(new ArrayList<>(request.getSongs()));
            artist.setSongs(new HashSet<>(songList));
            songList.forEach(song -> song.getArtists().add(artist));
        }
        else {
            artist.setSongs(new HashSet<>());
        }

        artist.setFollower(artist.getFollower() + 1);

        // Image
        if (multipartFile != null && !multipartFile.isEmpty()) {
            List<String> allowedFileExtensions = new ArrayList<>
                    (Arrays.asList("jpg", "jpeg", "png", "gif", "bmp","svg"));
            artist.setImageURL(fileService.uploadFile(multipartFile, allowedFileExtensions).getUrl());
        }

        return convertArtistToResponse(artistRepository.save(artist));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(long id){
        Artist artistDB = artistRepository.findById(id)
                .orElseThrow(() -> new SpotifyException(ErrorCode.ARTIST_NOT_EXISTED));

        artistDB.getAlbums().forEach(album -> album.getArtists().remove(artistDB));

        artistDB.getSongs().forEach(song -> song.getArtists().remove(artistDB));

        artistRepository.delete(artistDB);
    }

    public List<ArtistResponse> convertListArtistToResponse(List<Artist> artistList) {
        List<ArtistResponse> artistResponses = new ArrayList<>();
        for (Artist artist : artistList) {
            ArtistResponse response = convertArtistToResponse(artist);
            artistResponses.add(response);
        }
        return artistResponses;
    }


    public ArtistResponse convertArtistToResponse(Artist artist) {
        ArtistResponse response = artistMapper.toArtistResponse(artist);

        Set<AlbumBasic> albumBasicList = artist.getAlbums()
                .stream().map(albumMapper::toAlbumBasic).collect(Collectors.toSet());
        response.setAlbums(albumBasicList);

        Set<SongBasic> songBasicList = artist.getSongs()
                .stream().map(songMapper::toSongBasic).collect(Collectors.toSet());
        response.setSongs(songBasicList);

        return response;
    }

}
