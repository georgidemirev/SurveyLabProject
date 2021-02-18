package demirev.surveylab.service;

import demirev.surveylab.domain.Poll;
import demirev.surveylab.domain.PollOption;
import demirev.surveylab.domain.User;
import demirev.surveylab.dto.PollCreateDto;
import demirev.surveylab.dto.PollExtractDto;
import demirev.surveylab.dto.VoteDto;
import demirev.surveylab.mapper.PollMapper;
import demirev.surveylab.mapper.PollMapperImpl;
import demirev.surveylab.mapper.UserMapper;
import demirev.surveylab.mapper.UserMapperImpl;
import demirev.surveylab.repository.PollOptionRepository;
import demirev.surveylab.repository.PollRepository;
import demirev.surveylab.repository.UserRepository;
import demirev.surveylab.rest.exceptions.NotFoundException;
import demirev.surveylab.rest.exceptions.ValidationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PollServiceImplTest {
    private static final Long ID_1 = 1L;
    private static final Long ID_2 = 2L;
    private static final String EXPECTED_POLL_TEXT = "First poll text";
    private static final boolean EXPECTED_IS_COMPLETED = false;
    private static final boolean EXPECTED_IS_MULTIPLE_CHOICE = false;
    private static final LocalDateTime EXPECTED_CREATED_ON = LocalDateTime.of(2020,
            1, 1, 1, 1);
    private static final LocalDateTime EXPECTED_EXPIRY_DATE = EXPECTED_CREATED_ON.plusDays(10);
    private static final String EXPECTED_FIRST_NAME = "First Name 1";
    private static final String EXPECTED_LAST_NAME = "Second Name 1";
    private static final String EXPECTED_USERNAME = "Username 1";
    private static final String EXPECTED_PASSWORD = "Password 1";
    private static final String EXPECTED_POLL_OPTION_TEXT = "First poll option";

    private static VoteDto voteDto;
    private static PollOption pollOption1;
    private static PollOption pollOption2;
    private static List<PollOption> pollOptionList;
    Page<Poll> page;
    private Poll poll;
    private User user;
    @Mock
    private PollRepository pollRepository;

    @Mock
    private PollOptionRepository pollOptionRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    private UserMapper userMapper = new UserMapperImpl();

    @Spy
    private PollMapper pollMapper = new PollMapperImpl(userMapper);

    @InjectMocks
    private PollServiceImpl pollService;

    private PageRequest pageRequest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        poll = new Poll();
        poll.setId(1L);
        poll.setCreatedOn(EXPECTED_CREATED_ON);
        poll.setText(EXPECTED_POLL_TEXT);

        pollOption1 = setUpPollOption(poll, "first poll option", 1L);
        pollOption2 = setUpPollOption(poll, "second poll option", 2L);

        user = setUpUser();
        poll.setUser(user);

        pollOptionList = new ArrayList<>();
        pollOptionList.add(pollOption1);
        pollOptionList.add(pollOption2);

        voteDto = new VoteDto();
        voteDto.setPollId(ID_1);
        voteDto.setUserId(ID_1);
        voteDto.setPollOptionIds(Arrays.asList(ID_1, ID_2));

        pageRequest = PageRequest.of(0, 10);
        page = new PageImpl<Poll>(Collections.singletonList(poll));
    }

    @Test
    public void createPoll_ValidPollWithGivenExpiryDate() throws NotFoundException, ValidationException {
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));
        poll.setExpiryDate(EXPECTED_EXPIRY_DATE);
        when(pollRepository.save(any())).thenReturn(poll);
        poll.setPollOptions(pollOptionList);

        PollCreateDto pollCreateDto = pollMapper.toCreateDto(poll);
        pollCreateDto.setPollOptionStrings(Arrays.asList("first poll option", "second poll option"));

        PollExtractDto pollCreateDto1 = pollService.createPoll(pollCreateDto);

        Assert.assertEquals("Wrong text of poll", EXPECTED_POLL_TEXT, pollCreateDto1.getText());
        Assert.assertEquals("Wrong boolean is completed", EXPECTED_IS_COMPLETED, pollCreateDto1.isCompleted());
        Assert.assertEquals("Wrong boolean is multiple choice", EXPECTED_IS_MULTIPLE_CHOICE, pollCreateDto1.isMultipleChoice());
        Assert.assertEquals("Wrong expiry date", EXPECTED_EXPIRY_DATE, pollCreateDto1.getExpiryDate());

        Assert.assertEquals("Wrong poll option id in poll's poll option 1",
                ID_1, poll.getPollOptions().get(0).getId());
        Assert.assertEquals("Wrong poll option text in poll's poll option 1",
                "first poll option", poll.getPollOptions().get(0).getText());

        Assert.assertEquals("Wrong poll option id in poll's poll option 2",
                ID_2, poll.getPollOptions().get(1).getId());
        Assert.assertEquals("Wrong poll option text in poll's poll option 2",
                "second poll option", poll.getPollOptions().get(1).getText());

    }

    @Test
    public void createPoll_ValidPollWithoutGivenExpiryDate() throws NotFoundException, ValidationException {
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));
        when(pollRepository.save(any())).thenReturn(poll);

        poll.setPollOptions(pollOptionList);

        PollCreateDto pollCreateDto = pollMapper.toCreateDto(poll);
        pollCreateDto.setPollOptionStrings(Arrays.asList("first poll option", "second poll option"));

        PollExtractDto pollCreateDto1 = pollService.createPoll(pollCreateDto);

        Assert.assertEquals("Wrong text of poll", EXPECTED_POLL_TEXT, pollCreateDto1.getText());
        Assert.assertEquals("Wrong boolean is completed", EXPECTED_IS_COMPLETED, pollCreateDto1.isCompleted());
        Assert.assertEquals("Wrong boolean is multiple choice", EXPECTED_IS_MULTIPLE_CHOICE, pollCreateDto1.isMultipleChoice());
        Assert.assertEquals("Wrong expiry date", LocalDate.now().plusDays(10), pollCreateDto1.getExpiryDate().toLocalDate());

        Assert.assertEquals("Wrong poll option id in poll's poll option 1",
                ID_1, poll.getPollOptions().get(0).getId());
        Assert.assertEquals("Wrong poll option text in poll's poll option 1",
                "first poll option", poll.getPollOptions().get(0).getText());

        Assert.assertEquals("Wrong poll option id in poll's poll option 2",
                ID_2, poll.getPollOptions().get(1).getId());
        Assert.assertEquals("Wrong poll option text in poll's poll option 2",
                "second poll option", poll.getPollOptions().get(1).getText());

    }

    @Test(expected = ValidationException.class)
    public void createPoll_PollWithTwoPollOptionsWithSameNames() throws ValidationException, NotFoundException {
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));
        poll.setExpiryDate(EXPECTED_EXPIRY_DATE);

        PollCreateDto pollCreateDto = pollMapper.toCreateDto(poll);
        // here i set the second poll option's text to be same as the first poll option
        pollCreateDto.setPollOptionStrings(Arrays.asList("first poll option", "first poll option"));

        pollService.createPoll(pollCreateDto);
    }


    @Test(expected = ValidationException.class)
    public void createPoll_PollWithNonExistingUser() throws NotFoundException, ValidationException {
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        PollExtractDto pollCreateDto = pollService.createPoll(pollMapper.toCreateDto(poll));
    }

    @Test
    public void findById() throws NotFoundException {
        when(pollRepository.findById(any())).thenReturn(Optional.ofNullable(poll));

        Optional<PollExtractDto> pollCreateDto = pollService.findById(1L);
        Assert.assertTrue(pollCreateDto.isPresent());

        Assert.assertEquals("text is not as expected", EXPECTED_POLL_TEXT, pollCreateDto.get().getText());
    }

    @Test
    public void findById_WithNullPoll() throws NotFoundException {
        when(pollRepository.findById(any())).thenReturn(Optional.empty());

        Optional<PollExtractDto> pollCreateDto = pollService.findById(1L);
        Assert.assertFalse(pollCreateDto.isPresent());
    }

    @Test
    public void findAllByUsername() throws NotFoundException, ValidationException {
        when(pollRepository.findByUserUsername(any(), any())).thenReturn(page);
        when(userRepository.findByUsername(any())).thenReturn(Optional.ofNullable(user));

        Page<PollExtractDto> pollsList = pollService.findAllByUsername(EXPECTED_USERNAME, pageRequest);

        Assert.assertEquals("size of polls not equal to 1", 1, pollsList.getNumberOfElements());
    }

    @Test
    public void findAllByUsername_UserWithoutPolls() throws NotFoundException, ValidationException {
        when(pollRepository.findByUserUsername(any(), any())).thenReturn(new PageImpl<>(Collections.emptyList()));
        when(userRepository.findByUsername(any())).thenReturn(Optional.ofNullable(user));

        Page<PollExtractDto> pollsList = pollService.findAllByUsername(EXPECTED_USERNAME, pageRequest);

        Assert.assertEquals("size of polls not equal to 0", 0, pollsList.getNumberOfElements());
    }

    @Test(expected = ValidationException.class)
    public void findAllByUsername_NotExistingUsername_throwsValidationException() throws ValidationException {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        Page<PollExtractDto> pollsList = pollService.findAllByUsername(EXPECTED_USERNAME, pageRequest);
    }

    @Test
    public void vote_validVotingWithoutMultipleChoice() throws ValidationException {
        poll.setExpiryDate(LocalDateTime.now().plusDays(10));
        poll.setPollOptions(pollOptionList);
        voteDto.setPollOptionIds(Collections.singletonList(ID_1));

        when(userRepository.findById(ID_1)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findAllPollOptionsUserHasVotedFor(ID_1, ID_1)).thenReturn(Collections.emptyList());
        when(pollRepository.findById(ID_1)).thenReturn(Optional.ofNullable(poll));
        when(pollOptionRepository.findById(ID_1)).thenReturn(Optional.ofNullable(pollOption1));
        when(pollOptionRepository.findById(ID_2)).thenReturn(Optional.ofNullable(pollOption2));

        pollService.vote(voteDto);

        Assert.assertTrue("user does not contain poll option that he has voted for", user.getPollOptions().contains(pollOption1));
        Assert.assertTrue("poll option does not contain the user", pollOption1.getUsers().contains(user));
    }

    @Test
    public void vote_validVotingWithMultipleChoice() throws ValidationException {
        poll.setExpiryDate(LocalDateTime.now().plusDays(10));
        poll.setMultipleChoice(true);
        poll.setPollOptions(pollOptionList);

        when(userRepository.findById(ID_1)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findAllPollOptionsUserHasVotedFor(ID_1, ID_1)).thenReturn(Collections.emptyList());
        when(pollRepository.findById(ID_1)).thenReturn(Optional.ofNullable(poll));
        when(pollOptionRepository.findById(ID_1)).thenReturn(Optional.ofNullable(pollOption1));
        when(pollOptionRepository.findById(ID_2)).thenReturn(Optional.ofNullable(pollOption2));

        pollService.vote(voteDto);

        Assert.assertTrue("user does not contain poll option that he has voted for", user.getPollOptions().contains(pollOption1));
        Assert.assertTrue("poll option does not contain the user", pollOption1.getUsers().contains(user));
    }

    @Test(expected = ValidationException.class)
    public void vote_withNotExistingUser_throwValidationException() throws ValidationException {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        pollService.vote(voteDto);
    }

    @Test(expected = ValidationException.class)
    public void vote_withNullPollOptions_throwValidationException() throws ValidationException {
        voteDto.setPollOptionIds(null);

        pollService.vote(voteDto);
    }

    @Test(expected = ValidationException.class)
    public void vote_withEmptyPollOptions_throwValidationException() throws ValidationException {
        voteDto.setPollOptionIds(Collections.emptyList());

        pollService.vote(voteDto);
    }

    @Test(expected = ValidationException.class)
    public void vote_sameUserVoteTwice_throwValidationException() throws ValidationException {
        user = setUpUser();

        when(userRepository.findById(ID_1)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findAllPollOptionsUserHasVotedFor(ID_1, ID_1)).thenReturn(Collections.singletonList(pollOption1));

        pollService.vote(voteDto);
    }

    @Test(expected = ValidationException.class)
    public void vote_withExpiredPoll_throwValidationException() throws ValidationException {
        poll.setExpiryDate(poll.getCreatedOn().minusDays(10));

        when(userRepository.findById(ID_1)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findAllPollOptionsUserHasVotedFor(ID_1, ID_1)).thenReturn(Collections.emptyList());
        when(pollRepository.findById(ID_1)).thenReturn(Optional.ofNullable(poll));

        pollService.vote(voteDto);
    }

    @Test(expected = ValidationException.class)
    public void vote_withCompletedPoll_throwValidationException() throws ValidationException {
        poll.setCompleted(true);

        when(userRepository.findById(ID_1)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findAllPollOptionsUserHasVotedFor(ID_1, ID_1)).thenReturn(Collections.emptyList());
        when(pollRepository.findById(ID_1)).thenReturn(Optional.ofNullable(poll));

        pollService.vote(voteDto);
    }

    @Test(expected = ValidationException.class)
    public void vote_withNonExistingPoll_throwValidationException() throws ValidationException {
        when(userRepository.findById(ID_1)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findAllPollOptionsUserHasVotedFor(ID_1, ID_1)).thenReturn(Collections.emptyList());
        when(pollRepository.findById(ID_1)).thenReturn(Optional.empty());

        pollService.vote(voteDto);
    }

    @Test(expected = ValidationException.class)
    public void vote_withNonExistingPollOptionWithOneOption_throwValidationException() throws ValidationException {
        poll.setExpiryDate(LocalDateTime.now().plusDays(10));

        when(userRepository.findById(ID_1)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findAllPollOptionsUserHasVotedFor(ID_1, ID_1)).thenReturn(Collections.emptyList());
        when(pollRepository.findById(ID_1)).thenReturn(Optional.ofNullable(poll));

        voteDto.setPollOptionIds(Collections.singletonList(ID_1));
        when(pollOptionRepository.findById(ID_1)).thenReturn(Optional.empty());

        pollService.vote(voteDto);
    }

    @Test(expected = ValidationException.class)
    public void vote_withNonExistingPollOptionWithListOfOptions_throwValidationException() throws ValidationException {
        poll.setExpiryDate(LocalDateTime.now().plusDays(10));
        poll.setMultipleChoice(true);

        when(userRepository.findById(ID_1)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findAllPollOptionsUserHasVotedFor(ID_1, ID_1)).thenReturn(Collections.emptyList());
        when(pollRepository.findById(ID_1)).thenReturn(Optional.ofNullable(poll));

        when(pollOptionRepository.findById(ID_1)).thenReturn(Optional.empty());

        pollService.vote(voteDto);
    }

    @Test(expected = ValidationException.class)
    public void vote_forListOfOptionsWithoutMultipleChoiceEnabled_throwValidationException() throws ValidationException {
        poll.setExpiryDate(LocalDateTime.now().plusDays(10));

        when(userRepository.findById(ID_1)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findAllPollOptionsUserHasVotedFor(ID_1, ID_1)).thenReturn(Collections.emptyList());
        when(pollRepository.findById(ID_1)).thenReturn(Optional.ofNullable(poll));

        pollService.vote(voteDto);
    }

    @Test(expected = ValidationException.class)
    public void vote_forListOfOptions_MultipleChoiceDisabled_PollWithoutOptions_throwValidationException() throws ValidationException {
        poll.setExpiryDate(LocalDateTime.now().plusDays(10));
        voteDto.setPollOptionIds(Collections.singletonList(ID_1));
        poll.setPollOptions(Collections.emptyList());

        when(userRepository.findById(ID_1)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findAllPollOptionsUserHasVotedFor(ID_1, ID_1)).thenReturn(Collections.emptyList());
        when(pollRepository.findById(ID_1)).thenReturn(Optional.ofNullable(poll));
        when(pollOptionRepository.findById(ID_1)).thenReturn(Optional.ofNullable(pollOption1));

        pollService.vote(voteDto);
    }

    @Test(expected = ValidationException.class)
    public void vote_forListOfOptions_MultipleChoiceEnabled_PollWithoutOptions_throwValidationException() throws ValidationException {
        poll.setExpiryDate(LocalDateTime.now().plusDays(10));
        poll.setMultipleChoice(true);
        poll.setPollOptions(Collections.emptyList());

        when(userRepository.findById(ID_1)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findAllPollOptionsUserHasVotedFor(ID_1, ID_1)).thenReturn(Collections.emptyList());
        when(pollRepository.findById(ID_1)).thenReturn(Optional.ofNullable(poll));
        when(pollOptionRepository.findById(ID_1)).thenReturn(Optional.ofNullable(pollOption1));

        pollService.vote(voteDto);
    }

    @Test(expected = ValidationException.class)
    public void vote_forListOfOptions_UserHasAlreadyVoted_throwValidationException() throws ValidationException {
        poll.setExpiryDate(LocalDateTime.now().plusDays(10));
        poll.setMultipleChoice(true);
        poll.setPollOptions(pollOptionList);
        pollOption1.addUser(user);

        when(userRepository.findById(ID_1)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findAllPollOptionsUserHasVotedFor(ID_1, ID_1)).thenReturn(Collections.emptyList());
        when(pollRepository.findById(ID_1)).thenReturn(Optional.ofNullable(poll));
        when(pollOptionRepository.findById(ID_1)).thenReturn(Optional.ofNullable(pollOption1));

        pollService.vote(voteDto);
    }

    @Test(expected = ValidationException.class)
    public void vote_forOneOption_UserHasAlreadyVoted_throwValidationException() throws ValidationException {
        poll.setExpiryDate(LocalDateTime.now().plusDays(10));
        poll.setPollOptions(pollOptionList);
        voteDto.setPollOptionIds(Collections.singletonList(ID_1));
        pollOption1.addUser(user);

        when(userRepository.findById(ID_1)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findAllPollOptionsUserHasVotedFor(ID_1, ID_1)).thenReturn(Collections.emptyList());
        when(pollRepository.findById(ID_1)).thenReturn(Optional.ofNullable(poll));
        when(pollOptionRepository.findById(ID_1)).thenReturn(Optional.ofNullable(pollOption1));
        when(pollOptionRepository.findById(ID_2)).thenReturn(Optional.ofNullable(pollOption2));

        pollService.vote(voteDto);
    }

    private PollOption setUpPollOption(Poll poll, String text, Long id) {
        PollOption pollOption = new PollOption();
        pollOption.setId(id);
        pollOption.setCreatedOn(EXPECTED_CREATED_ON);
        pollOption.setText(text);
        pollOption.setPoll(poll);
        return pollOption;
    }

    private User setUpUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName(EXPECTED_FIRST_NAME);
        user.setLastName(EXPECTED_LAST_NAME);
        user.setUsername(EXPECTED_USERNAME);
        user.setPassword(EXPECTED_PASSWORD);
        user.setCreatedOn(EXPECTED_CREATED_ON);
        return user;
    }

}