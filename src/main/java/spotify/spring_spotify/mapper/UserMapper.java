package spotify.spring_spotify.mapper;

import spotify.spring_spotify.dto.request.RegisterRequest;
import spotify.spring_spotify.dto.request.UserRequest;
import spotify.spring_spotify.dto.response.UserResponse;
import spotify.spring_spotify.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdPlaylists", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toUser(UserRequest request);
    @Mapping(target = "createdPlaylists", ignore = true)
    @Mapping(target = "roles", ignore = true)
    UserResponse toUserResponse(User user);
    @Mapping(target = "createdPlaylists", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User updateUser(@MappingTarget User user, UserRequest request);

    @Mapping(target = "id", ignore = true)
    User toUserByRegister(RegisterRequest request);
}
