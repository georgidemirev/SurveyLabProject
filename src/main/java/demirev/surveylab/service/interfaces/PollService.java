package demirev.surveylab.service.interfaces;

import demirev.surveylab.dto.PollCreateDto;
import demirev.surveylab.dto.PollExtractDto;
import demirev.surveylab.dto.VoteDto;
import demirev.surveylab.rest.exceptions.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PollService {

    PollExtractDto createPoll(PollCreateDto pollCreateDto) throws ValidationException;

    Optional<PollExtractDto> findById(Long id);

    Page<PollExtractDto> findAllByUsername(String username, Pageable pageable) throws ValidationException;

    PollExtractDto vote(VoteDto voteDto) throws ValidationException;

}