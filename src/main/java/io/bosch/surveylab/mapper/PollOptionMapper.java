package io.bosch.surveylab.mapper;

import io.bosch.surveylab.domain.PollOption;
import io.bosch.surveylab.dto.PollOptionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PollOptionMapper {

    PollOptionDto toDto(PollOption pollOption);

    PollOption toEntity(PollOptionDto pollOptionDto);
}
