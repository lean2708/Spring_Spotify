package spotify.spring_spotify.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import spotify.spring_spotify.dto.request.RegisterRequest;
import spotify.spring_spotify.dto.request.UserRequest;
import spotify.spring_spotify.dto.response.UserResponse;
import spotify.spring_spotify.entity.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-16T21:23:24+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(UserRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.name( request.getName() );
        user.email( request.getEmail() );
        user.password( request.getPassword() );
        user.dob( request.getDob() );

        return user.build();
    }

    @Override
    public UserResponse toUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.id( user.getId() );
        userResponse.name( user.getName() );
        userResponse.email( user.getEmail() );
        userResponse.imageURL( user.getImageURL() );
        userResponse.dob( user.getDob() );
        List<Long> list = user.getSavedPlaylistId();
        if ( list != null ) {
            userResponse.savedPlaylistId( new ArrayList<Long>( list ) );
        }
        userResponse.createdAt( user.getCreatedAt() );
        userResponse.updatedAt( user.getUpdatedAt() );
        userResponse.premiumStatus( user.isPremiumStatus() );
        userResponse.premiumExpiryDate( user.getPremiumExpiryDate() );

        return userResponse.build();
    }

    @Override
    public User toUserByRegister(RegisterRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.name( request.getName() );
        user.email( request.getEmail() );
        user.password( request.getPassword() );
        user.dob( request.getDob() );

        return user.build();
    }
}
