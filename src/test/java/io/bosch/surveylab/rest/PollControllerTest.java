package io.bosch.surveylab.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bosch.surveylab.domain.Poll;
import io.bosch.surveylab.repository.interfaces.PollRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class PollControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PollRepository pollRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Poll poll;
    private Poll poll2;

    @Before
    public void setUp() throws Exception {
        poll = new Poll("Aa", null, true, true, false);
        poll2 = new Poll("BB", null, true, true, false);
    }

//    @Test
//    public void getPollById_SuccessfulTest() throws Exception {
//        Long pollId = 1L;
//
//        poll.setId(1L);
//        when(pollRepository.findById((pollId))).thenReturn(poll);
//
//        String pollToString = objectMapper.writeValueAsString(poll);
//
//        mockMvc.perform(get("/poll/" + pollId))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(content().json(pollToString));
//    }

    @Test
    public void createPoll_AndCheckIfExist_SuccessfulTest() throws Exception {
        String pollToString = objectMapper.writeValueAsString(poll);
        Long expectedId = 1L;
        when(pollRepository.createPoll((poll))).thenReturn(expectedId);

        mockMvc.perform(post("/poll")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(pollToString))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(expectedId)));
    }
    //@Test
//    public void updatePollById_CheckIsUpdating() throws Exception {
//        poll.setId(1L);
//        Long pollId = 1L;
//        when(pollRepository.updatePollById(poll)).thenReturn(poll);
//        String strPoll = objectMapper.writeValueAsString(poll);
//
//        mockMvc.perform(put("/poll/" + pollId).contentType(MediaType.APPLICATION_JSON_UTF8).content(strPoll))
//                .andExpect(status().isOk())
//                .andExpect(content().string(strPoll));
//
//    }

    @Test
    public void deletePollById() throws Exception {
        pollRepository.createPoll(poll);
        Long pollId = 1L;
        doNothing().when(pollRepository).deletePollById(pollId);
        mockMvc.perform(delete("/poll/" + pollId)).andExpect(status().isOk());
        verify(pollRepository, times(1)).deletePollById(pollId);

    }

//    @Test
//    public void getAllPoll() throws Exception {
//
//        pollRepository.createPoll(poll);
//        pollRepository.createPoll(poll2);
//        poll.setId(1L);
//        poll2.setId(2L);
//        List<Poll> polls = new ArrayList<>();
//        polls.add(poll);
//        polls.add(poll2);
//
//        when(pollRepository.findAll()).thenReturn(polls);
//
//        String str2 = objectMapper.writeValueAsString(polls);
//
//        mockMvc.perform(get("/poll"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(str2));
//    }


}