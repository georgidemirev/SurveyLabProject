package io.bosch.surveylab.service;

import io.bosch.surveylab.domain.Poll;
import io.bosch.surveylab.domain.PollOption;
import io.bosch.surveylab.domain.User;
import io.bosch.surveylab.dto.CreateUserDto;
import io.bosch.surveylab.dto.PollDto;
import io.bosch.surveylab.dto.PollOptionDto;
import io.bosch.surveylab.mapper.PollMapper;
import io.bosch.surveylab.mapper.PollOptionMapper;
import io.bosch.surveylab.mapper.UserMapper;
import io.bosch.surveylab.repository.interfaces.PollOptionRepository;
import io.bosch.surveylab.repository.interfaces.PollRepository;
import io.bosch.surveylab.repository.interfaces.UserRepository;
import io.bosch.surveylab.service.interfaces.UserService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private PollRepository pollRepository;

    private PollOptionRepository pollOptionRepository;

    private PollMapper pollMapper;

    private UserMapper userMapper;

    private PollOptionMapper pollOptionMapper;

    public UserServiceImpl(UserRepository userRepository, PollRepository pollRepository,
                           PollOptionRepository pollOptionRepository, PollMapper pollMapper,
                           UserMapper userMapper, PollOptionMapper pollOptionMapper) {
        this.userRepository = userRepository;
        this.pollRepository = pollRepository;
        this.pollOptionRepository = pollOptionRepository;
        this.pollMapper = pollMapper;
        this.userMapper = userMapper;
        this.pollOptionMapper = pollOptionMapper;
    }

    @Override
    public Long createUser(CreateUserDto createUserDto) {
        if (createUserDto == null) {
            return -1L;
        }

        User user = userMapper.toEntity(createUserDto);

        return userRepository.createUser(user);
    }

    @Transactional
    public CreateUserDto addPoll(Long userId, PollDto pollDto) {
        User user = userRepository.findById(userId);
        Poll poll = null;

        if (pollDto.getId() != null) {
            poll = pollRepository.findById(pollDto.getId());
        }

        if (poll == null) {
            poll = pollMapper.toEntity(pollDto);
            poll.setId(null);
            pollRepository.createPoll(poll);
        }

        user.addPoll(poll);
        userRepository.update(user);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public CreateUserDto vote(Long userId, PollOptionDto pollOptionDto) {
        User user = userRepository.findById(userId);
        PollOption pollOption = null;

        if (pollOptionDto.getId() != null) {
            pollOption = pollOptionRepository.findById(pollOptionDto.getId());
        }

        // is this necessary ?
        // if i vote for smth it should be created already !
        if (pollOption == null) {
            pollOption = pollOptionMapper.toEntity(pollOptionDto);
            pollOption.setId(null);
            pollOptionRepository.createPollOption(pollOption);
        }

        pollOption.setNumberOfVotes(pollOption.getNumberOfVotes() + 1);

        user.addPollOption(pollOption);
        userRepository.update(user);
        return userMapper.toDto(user);
    }

    @Override
    public CreateUserDto findById(Long id) {
        User user = userRepository.findById(id);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public CreateUserDto update(CreateUserDto createUserDto) {
        if (createUserDto.getId() == null) {
            return null;
        }
        User user = userRepository.findById(createUserDto.getId());
        user.setCreatedOn(createUserDto.getCreatedOn());
        user.setEmail(createUserDto.getEmail());
        user.setLastName(createUserDto.getLastName());
        user.setFirstName(createUserDto.getFirstName());

        List<Long> dtoPollOptionList = createUserDto.getPollOptions().stream()
                .map(PollOptionDto::getId)
                .collect(Collectors.toList());
        user.getPollOptions().clear();
        List<PollOption> pollOptionList = userRepository.getAllPollOptionsById(dtoPollOptionList);
        pollOptionList.forEach(user::addPollOption);

        List<Long> dtoPollList = createUserDto.getPolls().stream()
                .map(PollDto::getId)
                .collect(Collectors.toList());
        user.getPolls().clear();
        List<Poll> pollList = userRepository.getAllPollsById(dtoPollList);
        pollList.forEach(user::addPoll);

        userRepository.update(user);
        return userMapper.toDto(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }

    @Override
    public List<CreateUserDto> findAll() {
        List<CreateUserDto> dtos = new ArrayList<>();
        for (User i : userRepository.findAll()) {
            dtos.add(userMapper.toDto(i));
        }
        return dtos;
    }
}
