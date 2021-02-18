package demirev.surveylab.repository;

import demirev.surveylab.domain.Poll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<Poll, Long> {

    Page<Poll> findByUserUsername(String username, Pageable pageable);
}