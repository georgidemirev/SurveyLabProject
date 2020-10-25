package io.bosch.surveylab.repository;

import io.bosch.surveylab.domain.Poll;
import io.bosch.surveylab.domain.PollOption;
import io.bosch.surveylab.domain.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = "classpath:/deleteContent.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserRepositoryImplTest {
    private static final String EXPECTED_TEXT = "First poll text";
    private static final boolean EXPECTED_IS_COMPLETED = false;
    private static final boolean EXPECTED_IS_MULTIPLE_CHOICE = false;
    private static final LocalDateTime EXPECTED_CREATED_ON = LocalDateTime.of(2020,
            1, 1, 1, 1);
    private static final LocalDateTime EXPECTED_EXPIRY_DATE = EXPECTED_CREATED_ON.plusDays(10);
    private static final String EXPECTED_FIRST_NAME = "First Name 1";
    private static final String EXPECTED_LAST_NAME = "Second Name 1";
    private static final String EXPECTED_USERNAME = "Username 1";
    private static final String EXPECTED_PASSWORD = "Password 1";

    private User user;
    private Poll poll;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollOptionRepository pollOptionRepository;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName(EXPECTED_FIRST_NAME);
        user.setLastName(EXPECTED_LAST_NAME);
        user.setUsername(EXPECTED_USERNAME);
        user.setPassword(EXPECTED_PASSWORD);
        user.setCreatedOn(EXPECTED_CREATED_ON);

        userRepository.save(user);

        poll = new Poll();
        poll.setText(EXPECTED_TEXT);
        poll.setCompleted(EXPECTED_IS_COMPLETED);
        poll.setMultipleChoice(EXPECTED_IS_MULTIPLE_CHOICE);
        poll.setCreatedOn(EXPECTED_CREATED_ON);
        poll.setExpiryDate(EXPECTED_EXPIRY_DATE);
        poll.setUser(user);

        pollRepository.save(poll);
    }

    @Test
    @Transactional
    public void findAllPollOptionsUserHasVotedFor_WithoutVotes() {
        List<PollOption> list = userRepository.findAllPollOptionsUserHasVotedFor(user.getId(), poll.getId());

        Assert.assertEquals("size of list is not equal ot 0", 0L, list.size());
    }

    @Test
    @Transactional
    public void findAllPollOptionsUserHasVotedFor_WithOneVote() {
        PollOption pollOption = new PollOption();
        pollOption.setText(EXPECTED_TEXT);
        pollOption.setCreatedOn(EXPECTED_CREATED_ON);
        pollOption.addUser(user);
        poll.addPollOption(pollOption);

        List<PollOption> list = userRepository.findAllPollOptionsUserHasVotedFor(user.getId(), poll.getId());

        Assert.assertEquals("size of list is not equal ot 1", 1L, list.size());
    }

    @Test
    @Transactional
    public void findByUsername() {
        Optional<User> expectedUser = userRepository.findByUsername(EXPECTED_USERNAME);
        Assert.assertTrue("user doesnt exist", expectedUser.isPresent());

        Assert.assertNotNull("id is null", expectedUser.get().getId());
        Assert.assertEquals("Wrong text of poll", EXPECTED_FIRST_NAME, expectedUser.get().getFirstName());
        Assert.assertEquals("Wrong boolean is completed", EXPECTED_LAST_NAME, expectedUser.get().getLastName());
        Assert.assertEquals("Wrong boolean is multiple choice", EXPECTED_USERNAME, expectedUser.get().getUsername());
        Assert.assertEquals("Wrong expiry date", EXPECTED_PASSWORD, expectedUser.get().getPassword());
        Assert.assertEquals("Wrong created on time", EXPECTED_CREATED_ON, expectedUser.get().getCreatedOn());
    }
}
