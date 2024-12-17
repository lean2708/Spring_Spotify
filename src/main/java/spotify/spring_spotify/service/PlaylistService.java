package spotify.spring_spotify.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import spotify.spring_spotify.dto.request.PlaylistRequest;
import spotify.spring_spotify.dto.response.PageResponse;
import spotify.spring_spotify.dto.response.PlaylistResponse;
import spotify.spring_spotify.dto.response.SongResponse;
import spotify.spring_spotify.entity.Playlist;
import spotify.spring_spotify.entity.Song;
import spotify.spring_spotify.entity.User;
import spotify.spring_spotify.exception.ErrorCode;
import spotify.spring_spotify.exception.FileException;
import spotify.spring_spotify.exception.SpotifyException;
import spotify.spring_spotify.mapper.PlaylistMapper;
import spotify.spring_spotify.repository.PlaylistRepository;
import spotify.spring_spotify.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spotify.spring_spotify.repository.UserRepository;
import spotify.spring_spotify.specification.PlaylistSpecification;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;
    private final PlaylistMapper playlistMapper;
    private final FileService fileService;
    private final SongService songService;
    private final UserRepository userRepository;

    public PlaylistResponse create(PlaylistRequest request, MultipartFile multipartFile) throws FileException, IOException, SAXException {
        if(playlistRepository.existsByTitle(request.getTitle())){
            throw new SpotifyException(ErrorCode.PLAYLIST_EXISTED);
        }
        Playlist playlist = playlistMapper.toPlaylist(request);

        // Songs
        if(request.getSongs() != null && !request.getSongs().isEmpty()){
            List<Song> songList = songRepository
                    .findAllByNameIn(new ArrayList<>(request.getSongs()));
            playlist.setSongs(new HashSet<>(songList));
            playlist.setTotalTracks(songList.size());
            for(Song song : songList){
                playlist.setTotalHours(playlist.getTotalHours() + song.getDuration());
            }
        }else {
            playlist.setSongs(new HashSet<>());
        }

        playlist.setFollower(playlist.getFollower() + 1);

        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new SpotifyException(ErrorCode.USER_NOT_EXISTED));

        playlist.setCreator(user.getName());

        Set<Playlist> playlistSet = Set.of(playlist);
        user.setCreatedPlaylists(playlistSet);
        userRepository.save(user);

        // Image
        if (multipartFile != null && !multipartFile.isEmpty()) {
            List<String> allowedFileExtensions = new ArrayList<>
                    (Arrays.asList("jpg", "jpeg", "png", "gif", "bmp","svg"));
            playlist.setImageURL(fileService.uploadFile(multipartFile, allowedFileExtensions).getUrl());
        }

        return convertPlaylistResponse(playlistRepository.save(playlist));
    }

    public PlaylistResponse fetchById(long id){
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new SpotifyException(ErrorCode.PLAYLIST_NOT_EXISTED));

        playlist.setFollower(playlist.getFollower() + 1);
        playlist.setListener(playlist.getListener() + 1);

        return convertPlaylistResponse(playlistRepository.save(playlist));
    }

    public PageResponse<PlaylistResponse> fetchAllPlaylists(int pageNo,int pageSize, String titleSortOrder){
        pageNo = pageNo - 1;

        Sort sort = (titleSortOrder.equalsIgnoreCase("asc"))
                ? Sort.by(Sort.Order.asc("title"))
                : Sort.by(Sort.Order.desc("title"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Playlist> playlistPage = playlistRepository.findAll(pageable);
        if (playlistPage.isEmpty()) {
            throw new SpotifyException(ErrorCode.PLAYLIST_NOT_EXISTED);
        }

        List<PlaylistResponse> playlistResponses = convertListPlaylistResponse(playlistPage.getContent());

        return PageResponse.<PlaylistResponse>builder()
                .page(playlistPage.getNumber() + 1)  // Thêm 1 để bắt đầu từ trang 1
                .size(playlistPage.getSize())
                .totalPages(playlistPage.getTotalPages())
                .totalItems(playlistPage.getTotalElements())
                .items(playlistResponses)
                .build();
    }

    public PageResponse<PlaylistResponse> searchPlaylists(String name, int pageNo, int pageSize){
        pageNo = pageNo - 1;

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Specification<Playlist> spec = Specification.where(PlaylistSpecification.hasNameContainingIgnoreCase(name))
                .and(PlaylistSpecification.sortByNamePriority(name));

        Page<Playlist> playlistPage = playlistRepository.findAll(spec, pageable);

        if (playlistPage.isEmpty()) {
            throw new SpotifyException(ErrorCode.PLAYLIST_NOT_EXISTED);
        }

        // convert
        List<PlaylistResponse> playlistResponses = convertListPlaylistResponse(playlistPage.getContent());

        return PageResponse.<PlaylistResponse>builder()
                .page(playlistPage.getNumber() + 1)  // Thêm 1 để bắt đầu từ trang 1
                .size(playlistPage.getSize())
                .totalPages(playlistPage.getTotalPages())
                .totalItems(playlistPage.getTotalElements())
                .items(playlistResponses)
                .build();
    }


    public PlaylistResponse update(long id, PlaylistRequest request, MultipartFile multipartFile) throws FileException, IOException, SAXException {
        Playlist playlistDB = playlistRepository.findById(id)
                .orElseThrow(() -> new SpotifyException(ErrorCode.PLAYLIST_NOT_EXISTED));

        Playlist playlist = playlistMapper.update(playlistDB,request);

        // Songs
        if(request.getSongs() != null && !request.getSongs().isEmpty()){
            List<Song> songList = songRepository
                    .findAllByNameIn(new ArrayList<>(request.getSongs()));
            Set<Song> songSet = playlist.getSongs();
            songSet.addAll(songList);
            playlist.setSongs(songSet);
            playlist.setTotalTracks(songList.size());
            for(Song song : songList){
                playlist.setTotalHours(playlist.getTotalHours() + song.getDuration());
            }
        }else {
            playlist.setSongs(new HashSet<>());
        }

        playlist.setFollower(playlist.getFollower() + 1);

        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new SpotifyException(ErrorCode.USER_NOT_EXISTED));

        playlist.setCreator(user.getName());

        Set<Playlist> playlistSet = Set.of(playlist);
        user.setCreatedPlaylists(playlistSet);
        userRepository.save(user);

        // Image
        if (multipartFile != null && !multipartFile.isEmpty()) {
            List<String> allowedFileExtensions = new ArrayList<>
                    (Arrays.asList("jpg", "jpeg", "png", "gif", "bmp","svg"));
            playlist.setImageURL(fileService.uploadFile(multipartFile, allowedFileExtensions).getUrl());
        }

        return convertPlaylistResponse(playlistRepository.save(playlist));
    }

    public void delete(long id){
        Playlist playlistDB = playlistRepository.findById(id)
                .orElseThrow(() -> new SpotifyException(ErrorCode.PLAYLIST_NOT_EXISTED));
        playlistRepository.delete(playlistDB);
    }

    public void removeSongFromPlaylist(long playlistId, long songId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new SpotifyException(ErrorCode.PLAYLIST_NOT_EXISTED));

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new SpotifyException(ErrorCode.SONG_NOT_EXISTED));
        Set<Song> songSet = playlist.getSongs();
        if (songSet != null && songSet.contains(song)) {
            songSet.remove(song);
            playlistRepository.save(playlist);
        } else {
            throw new SpotifyException(ErrorCode.SONG_NOT_IN_PLAYLIST);
        }
    }
    public PageResponse<PlaylistResponse> fetchAllPlaylistSortedByViewer( int pageNo, int pageSize, String viewerSortOrder) {
        pageNo = pageNo - 1;

        Sort sort = (viewerSortOrder.equalsIgnoreCase("asc"))
                ? Sort.by(Sort.Order.asc("listener"))
                : Sort.by(Sort.Order.desc("listener"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Playlist> playlistPage = playlistRepository.findAll(pageable);
        if (playlistPage.isEmpty()) {
            throw new SpotifyException(ErrorCode.PLAYLIST_NOT_EXISTED);
        }

        List<PlaylistResponse> playlistResponses = convertListPlaylistResponse(playlistPage.getContent());

        return PageResponse.<PlaylistResponse>builder()
                .page(playlistPage.getNumber() + 1)  // Thêm 1 để bắt đầu từ trang 1
                .size(playlistPage.getSize())
                .totalPages(playlistPage.getTotalPages())
                .totalItems(playlistPage.getTotalElements())
                .items(playlistResponses)
                .build();
    }


    public PlaylistResponse convertPlaylistResponse(Playlist playlist) {
        PlaylistResponse response = playlistMapper.toPlaylistResponse(playlist);


        List<Song> songList = new ArrayList<>(playlist.getSongs());
        List<SongResponse> songResponseList = songService.convertListSongResponse(songList);

        response.setSongs(new HashSet<>(songResponseList));

        return response;
    }

    public List<PlaylistResponse> convertListPlaylistResponse(List<Playlist> playlistList){
        List<PlaylistResponse> playlistResponseList = new ArrayList<>();
        for (Playlist playlist : playlistList){
            PlaylistResponse response = convertPlaylistResponse(playlist);
            playlistResponseList.add(response);
        }
        return playlistResponseList;
    }
}
