package spotify.spring_spotify.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import spotify.spring_spotify.dto.request.RoleRequest;
import spotify.spring_spotify.dto.response.RoleResponse;
import spotify.spring_spotify.entity.Role;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-15T12:58:15+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class RoleMapperImpl implements RoleMapper {

    @Override
    public Role toRole(RoleRequest request) {
        if ( request == null ) {
            return null;
        }

        Role.RoleBuilder role = Role.builder();

        role.name( request.getName() );
        role.description( request.getDescription() );

        return role.build();
    }

    @Override
    public RoleResponse toRoleResponse(Role role) {
        if ( role == null ) {
            return null;
        }

        RoleResponse.RoleResponseBuilder roleResponse = RoleResponse.builder();

        roleResponse.name( role.getName() );
        roleResponse.description( role.getDescription() );

        return roleResponse.build();
    }
}
