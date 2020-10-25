package io.bosch.surveylab.repository.interfaces;

import io.bosch.surveylab.domain.Poll;
import io.bosch.surveylab.domain.PollOption;
import io.bosch.surveylab.domain.User;

import java.util.List;

public interface PollRepository {

    Long createPoll(Poll poll);

    Poll assignUser(Long pollId, User user);

    Long addPollOption(Long pollId, PollOption pollOption);

    Poll findById(Long id);

    Poll update(Poll poll);

    void deletePollById(Long id);

    List<Poll> findAll();
}
