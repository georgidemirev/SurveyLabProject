package io.bosch.surveylab.repository;

import io.bosch.surveylab.domain.Poll;
import io.bosch.surveylab.domain.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = "classpath:/deleteContent.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PollRepositoryImplTest {
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

    private PageRequest pageRequest;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PollRepository pollRepository;

    @Before
    public void setUp() {
        User user = new User();
        user.setFirstName(EXPECTED_FIRST_NAME);
        user.setLastName(EXPECTED_LAST_NAME);
        user.setUsername(EXPECTED_USERNAME);
        user.setPassword(EXPECTED_PASSWORD);
        user.setCreatedOn(EXPECTED_CREATED_ON);

        userRepository.save(user);

        Poll poll = new Poll();
        poll.setText(EXPECTED_TEXT);
        poll.setCompleted(EXPECTED_IS_COMPLETED);
        poll.setMultipleChoice(EXPECTED_IS_MULTIPLE_CHOICE);
        poll.setCreatedOn(EXPECTED_CREATED_ON);
        poll.setExpiryDate(EXPECTED_EXPIRY_DATE);
        poll.setUser(user);

        pollRepository.save(poll);

        pageRequest = PageRequest.of(0, 10);
    }

    @Test
    @Transactional
    public void findByUserUsername() {
        Page<Poll> foundPage = pollRepository.findByUserUsername(EXPECTED_USERNAME, pageRequest);
        Optional<Poll> expectedPoll = foundPage.get().findAny();
        Assert.assertTrue("poll doesnt exist", expectedPoll.isPresent());

        Assert.assertNotNull("id is null", expectedPoll.get().getId());
        Assert.assertEquals("Wrong text of poll", EXPECTED_TEXT, expectedPoll.get().getText());
        Assert.assertEquals("Wrong boolean is completed", EXPECTED_IS_COMPLETED, expectedPoll.get().isCompleted());
        Assert.assertEquals("Wrong boolean is multiple choice", EXPECTED_IS_MULTIPLE_CHOICE, expectedPoll.get().isMultipleChoice());
        Assert.assertEquals("Wrong expiry date", EXPECTED_EXPIRY_DATE, expectedPoll.get().getExpiryDate());
        Assert.assertEquals("Wrong created on time", EXPECTED_CREATED_ON, expectedPoll.get().getCreatedOn());

    }
}