package spotify.spring_spotify.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spotify.spring_spotify.dto.request.PermissionRequest;
import spotify.spring_spotify.dto.request.RoleRequest;
import spotify.spring_spotify.dto.response.PermissionResponse;
import spotify.spring_spotify.dto.response.RoleResponse;
import spotify.spring_spotify.entity.Permission;
import spotify.spring_spotify.entity.Role;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
