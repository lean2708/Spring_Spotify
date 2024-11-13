package spotify.spring_spotify.service;

import spotify.spring_spotify.dto.basic.SongBasic;
import spotify.spring_spotify.dto.request.PlaylistRequest;
import spotify.spring_spotify.dto.response.PlaylistResponse;
import spotify.spring_spotify.entity.Playlist;
import spotify.spring_spotify.entity.Song;
import spotify.spring_spotify.exception.SpotifyException;
import spotify.spring_spotify.mapper.PlaylistMapper;
import spotify.spring_spotify.mapper.SongMapper;
import spotify.spring_spotify.repository.PlaylistRepository;
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
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;
    private final SongMapper songMapper;
    private final PlaylistMapper playlistMapper;

    public PlaylistResponse create(PlaylistRequest request){
        if(playlistRepository.existsByTitle(request.getTitle())){
            throw new SpotifyException("Playlist đã tồn tại");
        }
        Playlist playlist = playlistMapper.toPlaylist(request);

        // Songs
        if(request.getSongs() != null && !request.getSongs().isEmpty()){
            List<Song> songList = songRepository
                    .findAllByNameIn(new ArrayList<>(request.getSongs()));
            playlist.setSongs(new HashSet<>(songList));
        }else {
            playlist.setSongs(new HashSet<>());
        }

        PlaylistResponse response = playlistMapper.toPlaylistResponse(playlistRepository.save(playlist));

        Set<SongBasic> songBasicList = playlist.getSongs().stream()
                .map(songMapper::toSongBasic).collect(Collectors.toSet());
        response.setSongs(songBasicList);

        return response;
    }

    public PlaylistResponse fetchById(long id){
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new SpotifyException("Playlist không tồn tại"));

        PlaylistResponse response = playlistMapper.toPlaylistResponse(playlist);

        List<SongBasic> songBasicList = playlist.getSongs()
                .stream().map(songMapper::toSongBasic).toList();
        response.setSongs(new HashSet<>(songBasicList));

        return response;
    }

    public PlaylistResponse update(long id, PlaylistRequest request){
        Playlist playlistDB = playlistRepository.findById(id)
                .orElseThrow(() -> new SpotifyException("Playlist không tồn tại"));

        Playlist playlist = playlistMapper.update(playlistDB,request);

        // Songs
        if(request.getSongs() != null && !request.getSongs().isEmpty()){
            List<Song> songList = songRepository
                    .findAllByNameIn(new ArrayList<>(request.getSongs()));
            playlist.setSongs(new HashSet<>(songList));
        }else {
            playlist.setSongs(new HashSet<>());
        }

        PlaylistResponse response = playlistMapper.toPlaylistResponse(playlistRepository.save(playlist));

        Set<SongBasic> songBasicList = playlist.getSongs().stream()
                .map(songMapper::toSongBasic).collect(Collectors.toSet());
        response.setSongs(songBasicList);

        return response;
    }

    public void delete(long id){
        Playlist playlistDB = playlistRepository.findById(id)
                .orElseThrow(() -> new SpotifyException("Playlist không tồn tại"));
        playlistRepository.delete(playlistDB);
    }
}
