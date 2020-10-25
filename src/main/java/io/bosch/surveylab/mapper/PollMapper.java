package io.bosch.surveylab.mapper;

import io.bosch.surveylab.domain.Poll;
import io.bosch.surveylab.dto.PollDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PollMapper {

    @Mapping(target = "userId", source = "poll.user.id")
    PollDto toDto(Poll poll);

    @Mapping(target = "user.id", source = "pollDto.userId")
    Poll toEntity(PollDto pollDto);
}


