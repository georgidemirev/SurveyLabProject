package demirev.surveylab.mapper;

import demirev.surveylab.domain.Poll;
import demirev.surveylab.dto.PollCreateDto;
import demirev.surveylab.dto.PollExtractDto;
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
