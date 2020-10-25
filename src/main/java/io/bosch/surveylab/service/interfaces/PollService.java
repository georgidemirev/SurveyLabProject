package io.bosch.surveylab.service.interfaces;

import io.bosch.surveylab.dto.PollCreateDto;
import io.bosch.surveylab.dto.PollExtractDto;
import io.bosch.surveylab.dto.VoteDto;
import io.bosch.surveylab.rest.exceptions.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PollService {

    PollExtractDto createPoll(PollCreateDto pollCreateDto) throws ValidationException;

    Optional<PollExtractDto> findById(Long id);

    Page<PollExtractDto> findAllByUsername(String username, Pageable pageable) throws ValidationException;

    PollExtractDto vote(VoteDto voteDto) throws ValidationException;

}