package io.bosch.surveylab.repository;

import io.bosch.surveylab.domain.User;
import io.bosch.surveylab.repository.interfaces.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
public class JpaUserRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @Before
    public void setUp() throws Exception {
        user1 = new User();
        user1.setFirstName("g1");
        user1.setLastName("v1");
        user1.setEmail("m1");
        user2 = new User();
        user1.setFirstName("g2");
        user1.setLastName("v2");
        user1.setEmail("m2");

        user1.setCreatedOn(LocalDateTime.now());
        user2.setCreatedOn(LocalDateTime.now());
    }

    @Test
    @Transactional
    public void createValidUser() {
        Long createdUserId = userRepository.createUser(user1);

        User createdUser = entityManager.find(User.class, createdUserId);

        Assert.assertEquals(createdUserId, createdUser.getId());
        Assert.assertEquals(user1.getFirstName(), createdUser.getFirstName());
        Assert.assertEquals(user1.getLastName(), createdUser.getLastName());
        Assert.assertEquals(user1.getPolls(), createdUser.getPolls());
        Assert.assertEquals(user1.getPollOptions(), createdUser.getPollOptions());
        Assert.assertEquals(user1.getCreatedOn(), createdUser.getCreatedOn());
    }

    @Test
    @Transactional
    public void createNullUser() {
        Long createdUserId = userRepository.createUser(null);
        Long expectedId = 0L;

        Assert.assertEquals(expectedId, createdUserId);

        Assert.assertNull(entityManager.find(User.class, createdUserId));
    }

    @Test
    @Transactional
    public void findAll() {

        entityManager.persist(user1);
        entityManager.persist(user2);

        List<User> from_user = userRepository.findAll();
        Assert.assertEquals(2, from_user.size());
    }
}