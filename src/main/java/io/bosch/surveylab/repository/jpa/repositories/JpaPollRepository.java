package io.bosch.surveylab.repository.jpa.repositories;

import io.bosch.surveylab.domain.Poll;
import io.bosch.surveylab.domain.PollOption;
import io.bosch.surveylab.domain.User;
import io.bosch.surveylab.repository.interfaces.PollRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;


@Repository
@Profile("dev")
public class JpaPollRepository implements PollRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Long createPoll(Poll poll) {
        poll.setCreatedOn(LocalDateTime.now());
        entityManager.persist(poll);
        return poll.getId();
    }

    @Override
    @Transactional
    public Poll assignUser(Long pollId, User user) {
        Poll poll = entityManager.find(Poll.class, pollId);

        poll.setUser(new User(user));

        return poll;
    }

    @Override
    @Transactional
    public Long addPollOption(Long pollId, PollOption pollOption) {
        Poll poll = entityManager.find(Poll.class, pollId);

        poll.getPollOptions().add(new PollOption(pollOption));

        return pollId;
    }

    @Override
    public Poll findById(Long id) {
        return entityManager.find(Poll.class, id);
    }

    @Transactional
    public Poll update(Poll poll) {
        return entityManager.merge(poll);

    }

    @Override
    @Transactional
    public void deletePollById(Long id) {
        entityManager.remove(entityManager.find(Poll.class, id));
    }

    @Override
    public List<Poll> findAll() {
        return entityManager.createQuery("SELECT p from Poll p", Poll.class).getResultList();
    }
}
