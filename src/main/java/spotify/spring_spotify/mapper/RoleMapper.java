package spotify.spring_spotify.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spotify.spring_spotify.dto.request.RoleRequest;
import spotify.spring_spotify.dto.response.RoleResponse;
import spotify.spring_spotify.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    @Mapping(target = "permissions", ignore = true)

    RoleResponse toRoleResponse(Role role);
}
