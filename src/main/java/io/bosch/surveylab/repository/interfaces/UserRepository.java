package io.bosch.surveylab.repository.interfaces;

import io.bosch.surveylab.domain.Poll;
import io.bosch.surveylab.domain.PollOption;
import io.bosch.surveylab.domain.User;

import java.util.Collection;
import java.util.List;

public interface UserRepository {

    Long createUser(User user);

    User findById(Long id);

    User update(User user);

    void delete(Long id);

    List<User> findAll();

    List<Poll> getAllPollsById(Collection<Long> ids);

    List<PollOption> getAllPollOptionsById(Collection<Long> ids);

}
