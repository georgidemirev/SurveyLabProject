package io.bosch.surveylab.repository;

import io.bosch.surveylab.domain.PollOption;
import io.bosch.surveylab.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("SELECT po FROM PollOption po" +
            " join po.users u" +
            " where u.id = :userId" +
            " and po.poll.id = :pollId")
    List<PollOption> findAllPollOptionsUserHasVotedFor(@Param("userId") Long userId, @Param("pollId") Long pollId);
}