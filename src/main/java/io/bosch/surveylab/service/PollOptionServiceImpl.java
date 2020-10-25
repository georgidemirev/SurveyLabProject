package io.bosch.surveylab.service;

import io.bosch.surveylab.domain.Poll;
import io.bosch.surveylab.domain.PollOption;
import io.bosch.surveylab.repository.interfaces.PollOptionRepository;
import io.bosch.surveylab.service.interfaces.PollOptionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PollOptionServiceImpl implements PollOptionService {

    private PollOptionRepository pollOptionRepository;

    public PollOptionServiceImpl(PollOptionRepository pollOptionRepository) {
        this.pollOptionRepository = pollOptionRepository;
    }

    @Override
    public Long createPollOption(PollOption pollOption) {
        return pollOptionRepository.createPollOption(pollOption);
    }

    @Override
    public Long assignPoll(Long pollId, Poll poll) {
        return pollOptionRepository.assignPoll(pollId, poll);
    }

    @Override
    public PollOption findById(Long id) {
        return pollOptionRepository.findById(id);
    }

    @Override
    public PollOption update(PollOption user) {
        return pollOptionRepository.update(user);
    }

    @Override
    public void delete(Long id) {
        pollOptionRepository.delete(id);
    }

    @Override
    public List<PollOption> findAll() {
        return pollOptionRepository.findAll();
    }

}
