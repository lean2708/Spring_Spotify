package spotify.spring_spotify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import spotify.spring_spotify.dto.request.PermissionRequest;
import spotify.spring_spotify.dto.response.PermissionResponse;
import spotify.spring_spotify.entity.Permission;
import spotify.spring_spotify.exception.ErrorCode;
import spotify.spring_spotify.exception.SpotifyException;
import spotify.spring_spotify.mapper.PermissionMapper;
import spotify.spring_spotify.repository.PermissionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;
    @PreAuthorize("hasRole('ADMIN')")
    public PermissionResponse create(PermissionRequest request){
        if(permissionRepository.existsByName(request.getName())){
            throw new SpotifyException(ErrorCode.PERMISSION_EXISTED);
        }
        Permission permission = permissionMapper.toPermission(request);
        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<PermissionResponse> fetchAll(){
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(String name) {
        if(!permissionRepository.existsByName(name)){
            throw new SpotifyException(ErrorCode.PERMISSION_NOT_EXISTED);
        }
        permissionRepository.deleteById(name);
    }
}
