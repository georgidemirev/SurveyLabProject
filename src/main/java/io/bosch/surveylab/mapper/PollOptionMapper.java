package io.bosch.surveylab.mapper;

import io.bosch.surveylab.domain.PollOption;
import io.bosch.surveylab.dto.PollOptionDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = UserMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PollOptionMapper {

    PollOptionDto toDto(PollOption pollOption);

    @Mapping(target = "users", ignore = true)
    PollOption toEntity(PollOptionDto pollOptionDto);
}
