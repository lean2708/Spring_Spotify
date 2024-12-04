package spotify.spring_spotify.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import spotify.spring_spotify.dto.request.RoleRequest;
import spotify.spring_spotify.dto.response.PermissionResponse;
import spotify.spring_spotify.dto.response.RoleResponse;
import spotify.spring_spotify.entity.Permission;
import spotify.spring_spotify.entity.Role;
import spotify.spring_spotify.exception.ErrorCode;
import spotify.spring_spotify.exception.SpotifyException;
import spotify.spring_spotify.mapper.PermissionMapper;
import spotify.spring_spotify.mapper.RoleMapper;
import spotify.spring_spotify.repository.PermissionRepository;
import spotify.spring_spotify.repository.RoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public RoleResponse create(RoleRequest request){
        if(roleRepository.existsByName(request.getName())){
            throw new SpotifyException(ErrorCode.ROLE_EXISTED);
        }
        Role role = roleMapper.toRole(request);
        System.out.println(request.getPermissions());
        if (request.getPermissions() != null && !request.getPermissions().isEmpty()){
            List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
            role.setPermissions(new HashSet<>(permissions));
        }else {
            role.setPermissions(new HashSet<>());
        }

        RoleResponse response = roleMapper.toRoleResponse(roleRepository.save(role));

        Set<PermissionResponse> permissionResponse = role.getPermissions().stream()
                .map(permissionMapper::toPermissionResponse).collect(Collectors.toSet());
        response.setPermissions(permissionResponse);
        return response;
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<RoleResponse> fetchAll(){
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(String name) {
        if(!roleRepository.existsByName(name)){
            throw new SpotifyException(ErrorCode.ROLE_NOT_EXISTED);
        }
        roleRepository.deleteById(name);
    }
}
