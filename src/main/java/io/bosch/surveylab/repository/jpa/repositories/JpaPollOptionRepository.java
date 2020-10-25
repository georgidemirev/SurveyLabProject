package io.bosch.surveylab.repository.jpa.repositories;

import io.bosch.surveylab.domain.Poll;
import io.bosch.surveylab.domain.PollOption;
import io.bosch.surveylab.repository.interfaces.PollOptionRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Profile("dev")
@Repository
public class JpaPollOptionRepository implements PollOptionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Long createPollOption(PollOption pollOption) {
        if (pollOption == null) {
            return 0L;
        }
        pollOption.setCreatedOn(LocalDateTime.now());
        entityManager.persist(pollOption);
        return pollOption.getId();
    }

    @Override
    //@Transactional
    public Long assignPoll(Long pollOptionId, Poll poll) {
        PollOption pollOption = entityManager.find(PollOption.class, pollOptionId);

        pollOption.setPoll(new Poll(poll));// should make a copy of the poll

        return pollOptionId;
    }

    @Override
    public PollOption findById(Long id) {
        return entityManager.find(PollOption.class, id);
    }

    @Override
    @Transactional
    public PollOption update(PollOption user) {
        PollOption pollOption = entityManager.find(PollOption.class, user.getId());

        pollOption.setText(user.getText());
        pollOption.setNumberOfVotes(user.getNumberOfVotes());
        pollOption.setCreatedOn(user.getCreatedOn());

        return user;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        entityManager.remove(entityManager.find(PollOption.class, id));
    }

    @Override
    public List<PollOption> findAll() {
        return entityManager.createQuery("SELECT e FROM PollOption e", PollOption.class).getResultList();
    }

    @Override
    public List<PollOption> findByIds(Collection<Long> ids) {
        return entityManager.createQuery("From PollOption where id in (:ids)", PollOption.class)
                .setParameter("ids", ids)
                .getResultList();
    }
}
