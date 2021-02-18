package demirev.surveylab.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import demirev.surveylab.dto.PollCreateDto;
import demirev.surveylab.dto.PollExtractDto;
import demirev.surveylab.dto.VoteDto;
import demirev.surveylab.mapper.PollMapper;
import demirev.surveylab.rest.exceptions.ValidationException;
import demirev.surveylab.service.interfaces.PollService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class PollControllerTest {

    private static final Long ID_1 = 1L;
    private static final Long ID_2 = 2L;
    private static final String EXPECTED_TEXT = "First poll text";
    private static final LocalDateTime EXPECTED_CREATED_ON = LocalDateTime.of(2020,
            1, 1, 1, 1);
    private static final LocalDateTime EXPECTED_EXPIRY_DATE = EXPECTED_CREATED_ON.plusDays(10);

    private VoteDto voteDto;

    private PollCreateDto pollCreateDto;
    private PollExtractDto pollExtractDto;

    private Page<PollExtractDto> page;
    private PageRequest pageRequest;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PollMapper pollMapper;

    @MockBean
    private PollService pollService;

    @Before
    public void setUp() {
        pollCreateDto = new PollCreateDto();
        pollCreateDto.setText(EXPECTED_TEXT);
        pollCreateDto.setExpiryDate(EXPECTED_EXPIRY_DATE);

        pollExtractDto = new PollExtractDto();
        pollExtractDto.setText(EXPECTED_TEXT);
        pollExtractDto.setExpiryDate(EXPECTED_EXPIRY_DATE);

        voteDto = new VoteDto();
        voteDto.setPollId(ID_1);
        voteDto.setUserId(ID_1);
        voteDto.setPollOptionIds(Arrays.asList(ID_1, ID_2));

        pageRequest = PageRequest.of(0, 10);
        page = new PageImpl<PollExtractDto>(Collections.singletonList(pollExtractDto));
    }

    @Test
    public void save_WithValidDto() throws Exception {
        pollCreateDto.setUserId(ID_1);
        pollCreateDto.setPollOptionStrings(Arrays.asList("first", "second"));

        when(pollService.createPoll(any())).thenReturn(pollExtractDto);

        String pollDtoString = objectMapper.writeValueAsString(pollCreateDto);
        String expectedPollDtoString = objectMapper.writeValueAsString(pollExtractDto);

        mockMvc.perform(post("/api/polls").contentType(MediaType.APPLICATION_JSON).content(pollDtoString))
                .andExpect(status().isCreated())
                .andExpect(content().string(expectedPollDtoString));
    }

    @Test
    public void save_withoutTwoPollOptions_expectClientError() throws Exception {
        pollCreateDto.setUserId(ID_1);

        when(pollService.createPoll(any())).thenReturn(pollExtractDto);

        String pollDtoString = objectMapper.writeValueAsString(pollCreateDto);

        mockMvc.perform(post("/api/polls").contentType(MediaType.APPLICATION_JSON).content(pollDtoString))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void save_WithNullPollDto_expectBadRequest() throws Exception {
        String pollDtoString = objectMapper.writeValueAsString(null);

        mockMvc.perform(post("/api/polls").contentType(MediaType.APPLICATION_JSON).content(pollDtoString))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findById() throws Exception {
        when(pollService.findById(ID_1)).thenReturn(java.util.Optional.ofNullable(pollExtractDto));

        String expectedPollDtoString = objectMapper.writeValueAsString(pollExtractDto);

        mockMvc.perform(get("/api/polls/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedPollDtoString));
    }

    @Test
    public void findById_WithInvalidId() throws Exception {
        when(pollService.findById(ID_1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/polls/1"))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isNotFound());
    }

    @Test
    public void findAllByUsername() throws Exception {
        when(pollService.findAllByUsername("username", pageRequest))
                .thenReturn(page);

        pollCreateDto.setUserId(ID_1);
        pollCreateDto.setPollOptionStrings(Arrays.asList("first", "second"));

        String expectedPollDtoString = objectMapper.writeValueAsString(page);

        mockMvc.perform(get("/api/polls/by-username?page=0&size=10")
                .contentType(MediaType.APPLICATION_JSON).param("username", "username"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedPollDtoString));
    }

    @Test
    public void findAllByUsername_WithInvalidUsername_throwValidationException() throws Exception {
        when(pollService.findAllByUsername("username", pageRequest))
                .thenThrow(new ValidationException(""));

        mockMvc.perform(get("/api/polls/by-username?page=0&size=10")
                .contentType(MediaType.APPLICATION_JSON).param("username", "username"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void vote() throws Exception {
        when(pollService.vote(any())).thenReturn(pollExtractDto);

        String voteDto1 = objectMapper.writeValueAsString(voteDto);
        String expectedPollExtractDto = objectMapper.writeValueAsString(pollExtractDto);

        mockMvc.perform(put("/api/polls/vote").contentType(MediaType.APPLICATION_JSON).content(voteDto1))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedPollExtractDto));
    }

}
