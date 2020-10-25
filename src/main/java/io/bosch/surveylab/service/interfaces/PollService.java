package io.bosch.surveylab.service.interfaces;

import io.bosch.surveylab.domain.Poll;
import io.bosch.surveylab.domain.User;
import io.bosch.surveylab.dto.PollDto;
import io.bosch.surveylab.dto.PollOptionDto;

import java.util.List;

public interface PollService {

    Long createPoll(PollDto pollDto);

    PollDto assignUser(Long pollId, User user);

    PollDto addPollOption(Long pollId, PollOptionDto pollOptionDto);

    PollDto findById(Long id);

    PollDto updatePollById(PollDto pollDto);

    void deletePollById(Long id);

    List<PollDto> findAll();
}
