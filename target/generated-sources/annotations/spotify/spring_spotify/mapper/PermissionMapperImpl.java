package spotify.spring_spotify.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import spotify.spring_spotify.dto.request.PermissionRequest;
import spotify.spring_spotify.dto.response.PermissionResponse;
import spotify.spring_spotify.entity.Permission;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-15T12:58:14+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class PermissionMapperImpl implements PermissionMapper {

    @Override
    public Permission toPermission(PermissionRequest request) {
        if ( request == null ) {
            return null;
        }

        Permission.PermissionBuilder permission = Permission.builder();

        permission.name( request.getName() );
        permission.description( request.getDescription() );

        return permission.build();
    }

    @Override
    public PermissionResponse toPermissionResponse(Permission permission) {
        if ( permission == null ) {
            return null;
        }

        PermissionResponse.PermissionResponseBuilder permissionResponse = PermissionResponse.builder();

        permissionResponse.name( permission.getName() );
        permissionResponse.description( permission.getDescription() );

        return permissionResponse.build();
    }
}
