package io.bosch.surveylab.repository;

import io.bosch.surveylab.domain.PollOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollOptionRepository extends JpaRepository<PollOption, Long> {
}