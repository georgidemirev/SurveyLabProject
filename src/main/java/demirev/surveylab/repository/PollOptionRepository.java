package demirev.surveylab.repository;

import demirev.surveylab.domain.PollOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollOptionRepository extends JpaRepository<PollOption, Long> {
}