package io.bosch.surveylab.service;

import io.bosch.surveylab.domain.PollOption;
import io.bosch.surveylab.dto.PollOptionDto;
import io.bosch.surveylab.mapper.PollOptionMapper;
import io.bosch.surveylab.repository.PollOptionRepository;
import io.bosch.surveylab.service.interfaces.PollOptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class PollOptionServiceImpl implements PollOptionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PollOptionServiceImpl.class);

    private final PollOptionRepository pollOptionRepository;

    private final PollOptionMapper pollOptionMapper;

    public PollOptionServiceImpl(PollOptionRepository pollOptionRepository, PollOptionMapper pollOptionMapper) {
        this.pollOptionRepository = pollOptionRepository;
        this.pollOptionMapper = pollOptionMapper;
    }

    @Override
    @Transactional
    public PollOptionDto save(PollOptionDto pollOptionDto) {
        LOGGER.info("Invoked save method of poll option with poll option dto with id: {}, text: {}, number of votes: {}, users {}",
                pollOptionDto.getId(), pollOptionDto.getText(), pollOptionDto.getNumberOfVotes(), pollOptionDto.getUsers());

        PollOption pollOption = pollOptionMapper.toEntity(pollOptionDto);
        if (pollOption.getCreatedOn() == null) {
            pollOption.setCreatedOn(LocalDateTime.now());
        }
        return pollOptionMapper.toDto(pollOptionRepository.save(pollOption));
    }
}
