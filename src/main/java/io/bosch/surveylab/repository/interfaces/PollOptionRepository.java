package io.bosch.surveylab.repository.interfaces;

import io.bosch.surveylab.domain.Poll;
import io.bosch.surveylab.domain.PollOption;

import java.util.Collection;
import java.util.List;

public interface PollOptionRepository {

    Long createPollOption(PollOption pollOption);

    Long assignPoll(Long pollOptionId, Poll poll);

    PollOption findById(Long id);

    PollOption update(PollOption pollOption);

    void delete(Long id);

    List<PollOption> findAll();

    List<PollOption> findByIds(Collection<Long> ids);
}
