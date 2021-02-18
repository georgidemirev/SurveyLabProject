package demirev.surveylab.mapper;

import demirev.surveylab.domain.User;
import demirev.surveylab.dto.UserCreateDto;
import demirev.surveylab.dto.UserExtractDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toCreateEntity(UserCreateDto createUserDto);

    @Mapping(target = "pollExtractDtos", source = "user.polls")
    @Mapping(target = "pollOptionDtos", source = "user.pollOptions")
    UserExtractDto toExtractDto(User user);

    default Set<String> listUserToListString(Set<User> users) {
        return users.stream()
                .map(User::getUsername)
                .collect(Collectors.toSet());
    }

    default String listUserToListString(User user) {
        return user.getUsername();
    }
}
