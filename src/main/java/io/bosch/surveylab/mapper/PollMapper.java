package io.bosch.surveylab.mapper;

import io.bosch.surveylab.domain.Poll;
import io.bosch.surveylab.dto.PollCreateDto;
import io.bosch.surveylab.dto.PollExtractDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = UserMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PollMapper {

    @Mapping(target = "userId", source = "poll.user.id")
    PollCreateDto toCreateDto(Poll poll);

    @Mapping(target = "user.id", source = "pollCreateDto.userId")
    Poll toCreateEntity(PollCreateDto pollCreateDto);

    @Mapping(target = "pollOptionDtos", source = "poll.pollOptions")
    @Mapping(target = "user", source = "poll.user.username")
    PollExtractDto toExtractDto(Poll poll);
}
