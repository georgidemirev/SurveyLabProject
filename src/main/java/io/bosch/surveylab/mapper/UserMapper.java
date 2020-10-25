package io.bosch.surveylab.mapper;

import io.bosch.surveylab.domain.User;
import io.bosch.surveylab.dto.CreateUserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    CreateUserDto toDto(User user);

    User toEntity(CreateUserDto createUserDto);
}
