package io.bosch.surveylab.service.interfaces;

import io.bosch.surveylab.domain.Poll;
import io.bosch.surveylab.domain.PollOption;

import java.util.List;

public interface PollOptionService {

    Long createPollOption(PollOption pollOption);

    Long assignPoll(Long pollId, Poll poll);

    PollOption findById(Long id);

    PollOption update(PollOption pollOption);

    void delete(Long id);

    List<PollOption> findAll();

}
