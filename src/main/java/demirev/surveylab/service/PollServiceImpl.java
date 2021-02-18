package demirev.surveylab.service;

import demirev.surveylab.domain.Poll;
import demirev.surveylab.domain.PollOption;
import demirev.surveylab.domain.User;
import demirev.surveylab.dto.PollCreateDto;
import demirev.surveylab.dto.PollExtractDto;
import demirev.surveylab.dto.VoteDto;
import demirev.surveylab.mapper.PollMapper;
import demirev.surveylab.repository.PollOptionRepository;
import demirev.surveylab.repository.PollRepository;
import demirev.surveylab.repository.UserRepository;
import demirev.surveylab.rest.exceptions.ValidationException;
import demirev.surveylab.service.interfaces.PollService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PollServiceImpl implements PollService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PollServiceImpl.class);

    private final PollRepository pollRepository;

    private final PollOptionRepository pollOptionRepository;

    private final UserRepository userRepository;

    private final PollMapper pollMapper;


    public PollServiceImpl(PollRepository pollRepository, PollOptionRepository pollOptionRepository,
                           UserRepository userRepository, PollMapper pollMapper) {
        this.pollRepository = pollRepository;
        this.pollOptionRepository = pollOptionRepository;
        this.userRepository = userRepository;
        this.pollMapper = pollMapper;
    }

    /**
     * Creating poll method with text, isCompleted and isMultipleChoice flags(which are optional to set),
     * expiryDate(default 10 days after creation), userId (owner of poll) and list of strings for every pollOption
     * to be added
     * <p>
     * PollOption strings must be at least two
     * <p>
     * PollOptions are created as part of the poll creation process
     *
     * @param pollCreateDto text, completed and multiple choice flag, user id, list of poll option texts
     * @return {@link PollExtractDto}
     * @throws ValidationException when:
     *                             user id is not valid
     *                             list of texts has same strings inside
     */
    @Override
    @Transactional
    public PollExtractDto createPoll(PollCreateDto pollCreateDto) throws ValidationException {
        LOGGER.info("Invoked createPoll method with poll text: {}, expiry date: {}, user id: {}, poll option texts: {} ",
                pollCreateDto.getText(), pollCreateDto.getExpiryDate(), pollCreateDto.getUserId(), pollCreateDto.getPollOptionStrings());

        User user = userRepository.findById(pollCreateDto.getUserId())
                .orElseThrow(() -> new ValidationException(MessageFormat.format("user with id: {0} does not exist", pollCreateDto.getUserId())));

        //Creating the poll and adding the poll to the database
        Poll poll = pollMapper.toCreateEntity(pollCreateDto);
        poll.setCreatedOn(LocalDateTime.now());
        setExpiryDateIfAny(poll);
        pollRepository.save(poll);

        Set<String> pollOptionsTexts = new HashSet<>();
        for (String s : pollCreateDto.getPollOptionStrings()) {
            if (!pollOptionsTexts.contains(s)) {
                pollOptionsTexts.add(s);
                PollOption newPollOption = setPollOption(poll, s);
                pollOptionRepository.save(newPollOption);
                poll.addPollOption(newPollOption);
            }
        }

        validateSizeOfPollOptions(pollOptionsTexts);

        user.addPoll(poll);

        return pollMapper.toExtractDto(poll);
    }

    private void validateSizeOfPollOptions(Set<String> pollOptionsTexts) throws ValidationException {
        if (pollOptionsTexts.size() < 2) {
            throw new ValidationException("All the given unique poll option texts are less than 2");
        }
    }

    private void setExpiryDateIfAny(Poll poll) {
        if (poll.getExpiryDate() == null) {
            poll.setExpiryDate(poll.getCreatedOn().plusDays(10));
        }
    }

    @Override
    @Transactional
    public Optional<PollExtractDto> findById(Long id) {
        LOGGER.info("Invoked findById method with id: {}", id);

        Optional<Poll> poll = pollRepository.findById(id);

        return poll.map(pollMapper::toExtractDto);
    }

    /**
     * Taking username of User and returning all the poll that this User has created
     *
     * @param username username of User
     * @return Page<PollExtractDto> {@link PollExtractDto}
     */
    @Override
    @Transactional
    public Page<PollExtractDto> findAllByUsername(String username, Pageable pageable) throws ValidationException {
        LOGGER.info("Invoked findAllByUsername method with username: {}", username);

        if (!userRepository.findByUsername(username).isPresent()) {
            throw new ValidationException(MessageFormat.format("User with username: {0} does not exist", username));
        }

        Page<Poll> page = pollRepository.findByUserUsername(username, pageable);

        return page.map(pollMapper::toExtractDto);
    }

    /**
     * Vote method is taking a list of ids of poll options, user id and poll id
     * Is is validating the data and mapping the user to the poll options of the poll
     * and incrementing the votes of every poll option given in the list
     *
     * @param voteDto contains pollId, userId and list of pollOptionId, all of them are Long
     * @return {@link PollExtractDto}
     * @throws ValidationException when:
     *                             user id is not valid
     *                             poll id is not valid
     *                             user has already voted in this poll
     *                             poll options are more than 1 and poll is not multiple choice
     *                             poll option ids are not existing or are not valid
     *                             user has already voted for poll option
     */
    @Override
    @Transactional
    public PollExtractDto vote(VoteDto voteDto) throws ValidationException {
        LOGGER.info("Invoked vote method with vote dto with user id: {}, poll id: {}, poll options id's: {}",
                voteDto.getUserId(), voteDto.getPollId(), voteDto.getPollOptionIds());

        validateEmptyPollOptionsList(voteDto.getPollOptionIds());

        User user = userRepository.findById(voteDto.getUserId())
                .orElseThrow(() -> new ValidationException(MessageFormat.format("user with id: {0} does not exist", voteDto.getUserId())));
        validateVotingInPollFromUser(user, voteDto.getPollId());

        Poll poll = pollRepository.findById(voteDto.getPollId())
                .orElseThrow(() -> new ValidationException(MessageFormat.format("poll with id: {0} does not exist", voteDto.getPollId())));
        validatePoll(poll);

        validateMultipleChoiceVoting(voteDto, poll);

        for (Long i : voteDto.getPollOptionIds()) {
            PollOption pollOption = pollOptionRepository.findById(i)
                    .orElseThrow(() -> new ValidationException(MessageFormat.format("poll option with id: {0} not found", i)));

            validatePollOption(user, pollOption, poll);
            validateUserCanVoteForThisPollOption(user, pollOption);

            pollOption.setNumberOfVotes(pollOption.getNumberOfVotes() + 1);

            pollOption.addUser(user);
        }
        return pollMapper.toExtractDto(poll);
    }

    private void validateMultipleChoiceVoting(VoteDto voteDto, Poll poll) throws ValidationException {
        if (voteDto.getPollOptionIds().size() != 1 && !poll.isMultipleChoice()) {
            throw new ValidationException(MessageFormat.format("can not vote for more than one poll option" +
                    ", poll with id: {0} is not multiple choice", poll.getId()));
        }
    }

    private void validateVotingInPollFromUser(User user, Long pollId) throws ValidationException {
        if (hasUserVoted(user.getId(), pollId)) {
            throw new ValidationException(MessageFormat.format("user with id: {0} " +
                    "has already voted in poll with id: {1}", user.getId(), pollId));
        }
    }

    private void validatePollOption(User user, PollOption pollOption, Poll poll) throws ValidationException {
        if (!poll.getPollOptions().contains(pollOption)) {
            throw new ValidationException(MessageFormat.format("poll with id: {0} " +
                    "has no such option", poll.getId()));
        }
        validateUserCanVoteForThisPollOption(user, pollOption);
    }

    private void validateUserCanVoteForThisPollOption(User user, PollOption pollOption) throws ValidationException {
        if (pollOption.getUsers().contains(user)) {
            throw new ValidationException(MessageFormat.format("user with id: {0} already" +
                            " has already voted in poll with id: {1}"
                    , user.getId(), pollOption.getId()));
        }
    }

    private void validatePoll(Poll poll) throws ValidationException {
        if (poll.isCompleted()) {
            throw new ValidationException(MessageFormat.format("poll with id: {0} is completed" +
                    ", voting is impossible", poll.getId()));
        } else if (poll.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ValidationException(MessageFormat.format("poll with id: {0} has expired" +
                    ", voting is impossible", poll.getId()));
        }
    }

    private void validateEmptyPollOptionsList(List<Long> pollOptionIds) throws ValidationException {
        if (pollOptionIds == null || pollOptionIds.isEmpty()) {
            throw new ValidationException("empty poll option list given");
        }
    }

    private PollOption setPollOption(Poll poll, String s) {
        PollOption newPollOption = new PollOption();
        newPollOption.setPoll(poll);
        newPollOption.setCreatedOn(LocalDateTime.now());
        newPollOption.setText(s);

        return newPollOption;
    }

    private boolean hasUserVoted(Long userId, Long pollId) {
        return !userRepository.findAllPollOptionsUserHasVotedFor(userId, pollId).isEmpty();
    }
}
