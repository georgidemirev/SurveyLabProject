package demirev.surveylab.service;

import demirev.surveylab.domain.Poll;
import demirev.surveylab.domain.PollOption;
import demirev.surveylab.dto.PollOptionDto;
import demirev.surveylab.mapper.PollOptionMapper;
import demirev.surveylab.mapper.PollOptionMapperImpl;
import demirev.surveylab.mapper.UserMapperImpl;
import demirev.surveylab.repository.PollOptionRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PollOptionServiceImplTest {

    private static final LocalDateTime EXPECTED_CREATED_ON = LocalDateTime.of(2020,
            1, 1, 1, 1);
    private static final String EXPECTED_TEXT_FOR_POLL_OPTION = "First poll option";

    private static PollOption pollOption;
    private Poll poll;

    @Mock
    private PollOptionRepository pollOptionRepository;

    @Spy
    private UserMapperImpl userMapper = new UserMapperImpl();

    @Spy
    private PollOptionMapper pollOptionMapper = new PollOptionMapperImpl(userMapper);

    @InjectMocks
    private PollOptionServiceImpl pollOptionService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        pollOption = setUpPollOption(poll, EXPECTED_TEXT_FOR_POLL_OPTION, 1L);
    }

    @Test
    public void save() {
        PollOptionDto pollOptionDto = pollOptionMapper.toDto(pollOption);

        when(pollOptionRepository.save(any())).thenReturn(pollOption);

        PollOptionDto expectedPollOptionDto = pollOptionService.save(pollOptionDto);

        Assert.assertEquals("poll option text not as expected", EXPECTED_TEXT_FOR_POLL_OPTION, expectedPollOptionDto.getText());
    }

    private PollOption setUpPollOption(Poll poll, String text, Long id) {
        PollOption pollOption = new PollOption();
        pollOption.setId(id);
        pollOption.setCreatedOn(EXPECTED_CREATED_ON);
        pollOption.setText(text);
        pollOption.setPoll(poll);
        return pollOption;
    }

}
