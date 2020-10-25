package io.bosch.surveylab.repository;

import io.bosch.surveylab.domain.Poll;
import io.bosch.surveylab.repository.interfaces.PollRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Sql(scripts = "classpath:/createDBscript.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:/truncateTables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PollRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PollRepository pollRepository;

    @Test
    @Transactional
    public void createTest() {
        Poll poll = new Poll();
        poll.setPollName("test");
        Long poll1 = pollRepository.createPoll(poll);
        Poll poll2 = entityManager.find(Poll.class, poll1);
        Assert.assertEquals(poll1, poll2.getId());
    }

    @Test
    @Transactional
    public void findAllTest() {
        Poll poll1 = new Poll();
        poll1.setCreatedOn(LocalDateTime.now());
        Poll poll2 = new Poll();
        poll2.setCreatedOn(LocalDateTime.now());
        entityManager.persist(poll1);
        entityManager.persist(poll2);

        List<Poll> from_poll = pollRepository.findAll();
        Assert.assertEquals(2, from_poll.size());
    }
}
