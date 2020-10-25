package io.bosch.surveylab.service;

import io.bosch.surveylab.domain.Poll;
import io.bosch.surveylab.domain.PollOption;
import io.bosch.surveylab.domain.User;
import io.bosch.surveylab.dto.PollDto;
import io.bosch.surveylab.dto.PollOptionDto;
import io.bosch.surveylab.mapper.PollMapper;
import io.bosch.surveylab.mapper.PollOptionMapper;
import io.bosch.surveylab.repository.interfaces.PollOptionRepository;
import io.bosch.surveylab.repository.interfaces.PollRepository;
import io.bosch.surveylab.service.interfaces.PollService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PollServiceImpl implements PollService {


    private PollRepository pollRepository;

    private PollOptionRepository pollOptionRepository;

    private PollMapper pollMapper;

    private PollOptionMapper pollOptionMapper;

    public PollServiceImpl(PollRepository pollRepository, PollOptionRepository pollOptionRepository,
                           PollMapper pollMapper, PollOptionMapper pollOptionMapper) {
        this.pollRepository = pollRepository;
        this.pollOptionRepository = pollOptionRepository;
        this.pollMapper = pollMapper;
        this.pollOptionMapper = pollOptionMapper;
    }

    @Override
    public Long createPoll(PollDto pollDto) {
        if (pollDto == null) {
            return 0L;
        }

        Poll poll = pollMapper.toEntity(pollDto);
        return pollRepository.createPoll(poll);
    }

    @Override
    public PollDto assignUser(Long pollId, User user) {
        Poll poll = pollRepository.findById(user.getId());
        poll.setUser(user);
        pollRepository.update(poll);
        return pollMapper.toDto(poll);
    }

    @Override
    @Transactional
    public PollDto addPollOption(Long pollId, PollOptionDto pollOptionDto) {
        Poll poll = pollRepository.findById(pollId);
        PollOption pollOption = null;

        if (pollOptionDto.getId() != null) {
            pollOption = pollOptionRepository.findById(pollOptionDto.getId());
        }

        if (pollOption == null) {
            pollOption = pollOptionMapper.toEntity(pollOptionDto);
            pollOption.setId(null);
            pollOptionRepository.createPollOption(pollOption);
        }

        poll.addPollOption(pollOption);
        pollRepository.update(poll);

        return pollMapper.toDto(poll);
    }

    @Override
    public PollDto findById(Long id) {
        return pollMapper.toDto(pollRepository.findById(id));
    }

    @Override
    @Transactional
    public PollDto updatePollById(PollDto pollDto) {
        Poll poll = pollRepository.findById(pollDto.getId());
        Poll pollFromPollDto = pollMapper.toEntity(pollDto);

        poll.setCreatedOn(pollFromPollDto.getCreatedOn());
        poll.setUser(pollFromPollDto.getUser());
        poll.setPollName(pollFromPollDto.getPollName());
        poll.setAllowMultipleAnswers(pollFromPollDto.isAllowMultipleAnswers());
        poll.setAnonymous(pollFromPollDto.isAnonymous());
        poll.setCompleted(pollFromPollDto.isCompleted());
        List<Long> ids = pollFromPollDto.getPollOptions().stream()
                .map(PollOption::getId)
                .collect(Collectors.toList());
        List<PollOption> pollOptions = pollOptionRepository.findByIds(ids);
        poll.getPollOptions().clear();
        pollOptions.forEach(poll::addPollOption);
        pollRepository.update(poll);

        return pollMapper.toDto(poll);
    }

    @Override
    public void deletePollById(Long id) {
        pollRepository.deletePollById(id);
    }

    @Override
    public List<PollDto> findAll() {
        List<PollDto> pollDtos = new ArrayList<>();
        for (Poll poll : pollRepository.findAll())
            pollDtos.add(pollMapper.toDto(poll));
        return pollDtos;
    }
}
