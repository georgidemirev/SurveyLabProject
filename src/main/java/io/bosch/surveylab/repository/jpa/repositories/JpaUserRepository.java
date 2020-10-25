package io.bosch.surveylab.repository.jpa.repositories;


import io.bosch.surveylab.domain.Poll;
import io.bosch.surveylab.domain.PollOption;
import io.bosch.surveylab.domain.User;
import io.bosch.surveylab.repository.interfaces.UserRepository;
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
public class JpaUserRepository implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Long createUser(User user) {
        user.setCreatedOn(LocalDateTime.now());
        entityManager.persist(user);
        return user.getId();
    }

    @Override
    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    @Transactional
    public User update(User user) {
        return entityManager.merge(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        entityManager.remove(entityManager.find(User.class, id));
    }

    @Override
    public List<User> findAll() {
        return entityManager.createQuery("SELECT e FROM User e", User.class).getResultList();
    }

    @Override
    public List<Poll> getAllPollsById(Collection<Long> ids) {
        return entityManager.createQuery("From Poll where id in (:ids)", Poll.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public List<PollOption> getAllPollOptionsById(Collection<Long> ids) {
        return entityManager.createQuery("From PollOption where id in (:ids)", PollOption.class)
                .setParameter("ids", ids)
                .getResultList();
    }
}
