package io.bosch.surveylab.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bosch.surveylab.domain.PollOption;
import io.bosch.surveylab.repository.interfaces.PollOptionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class PollOptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PollOptionRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private PollOption pollOption;
    private PollOption pollOption2;

    @Before
    public void setUp() throws Exception {
        pollOption = new PollOption("g", 1);
        pollOption2 = new PollOption("g2", 2);
    }

    @Test
    public void testGetPollOption() throws Exception {
        Mockito.when(repository.findById(1L)).thenReturn(pollOption);

        String pollOptionString = objectMapper.writeValueAsString(pollOption);

        mockMvc.perform(get("/pollOption/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(pollOptionString));
    }

    @Test
    public void testCreateUser_Post() throws Exception {
        Mockito.when(repository.createPollOption(pollOption)).thenReturn(1L);

        String userString = objectMapper.writeValueAsString(pollOption);

        mockMvc.perform(post("/pollOption").contentType(MediaType.APPLICATION_JSON).content(userString))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(1L)));
    }

    @Test
    public void testUpdate_Put() throws Exception {
        PollOption updatedUser = new PollOption(pollOption2);
        updatedUser.setId(1L);

        Mockito.when(repository.update(pollOption)).thenReturn(updatedUser);
        pollOption.setId(1L);

        String updatedUserString = objectMapper.writeValueAsString(updatedUser);

        mockMvc.perform(put("/pollOption/1").contentType(MediaType.APPLICATION_JSON).content(updatedUserString))
                .andExpect(status().isOk())
                .andExpect(content().string(updatedUserString));
    }

    @Test
    public void testDelete() throws Exception {
        Mockito.doNothing().when(repository).delete(1L);

        mockMvc.perform(delete("/pollOption/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAll() throws Exception {
        Mockito.when(repository.findAll()).thenReturn(Arrays.asList(pollOption, pollOption2));

        //TODO:
        // stream the list and sort it by id and then check if all IDs are there
        //if i rearrange the places of the users in the list, it produces error because the users are stored in
        //hashMap and they are not in order

        String listString = objectMapper.writeValueAsString(repository.findAll());

        mockMvc.perform(get("/pollOption"))
                .andExpect(status().isOk())
                .andExpect(content().string(listString));
    }
}