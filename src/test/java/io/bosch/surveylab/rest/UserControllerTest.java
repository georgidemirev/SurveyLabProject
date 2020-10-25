package io.bosch.surveylab.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bosch.surveylab.dto.UserCreateDto;
import io.bosch.surveylab.dto.UserExtractDto;
import io.bosch.surveylab.rest.exceptions.ValidationException;
import io.bosch.surveylab.service.interfaces.UserService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class UserControllerTest {
    private static final Long EXPECTED_ID_1 = 1L;
    private static final String EXPECTED_FIRST_NAME = "First Name 1";
    private static final String EXPECTED_LAST_NAME = "Second Name 1";
    private static final String EXPECTED_USERNAME = "Username 1";
    private static final String EXPECTED_PASSWORD = "Password 1";

    private UserCreateDto invalidUser;
    private UserCreateDto validUser;

    private UserExtractDto validExtractUser;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService service;

    @Before
    public void setUp() {
        invalidUser = new UserCreateDto();

        validUser = new UserCreateDto();
        validUser.setFirstName(EXPECTED_FIRST_NAME);
        validUser.setLastName(EXPECTED_LAST_NAME);
        validUser.setUsername(EXPECTED_USERNAME);
        validUser.setPassword(EXPECTED_PASSWORD);

        validExtractUser = new UserExtractDto();
        validExtractUser.setFirstName(EXPECTED_FIRST_NAME);
        validExtractUser.setLastName(EXPECTED_LAST_NAME);
        validExtractUser.setUsername(EXPECTED_USERNAME);
    }

    @Test
    public void save_withValidDto() throws Exception {
        when(service.createUser(any())).thenReturn(validExtractUser);

        String userString = objectMapper.writeValueAsString(validUser);
        String expectedUserString = objectMapper.writeValueAsString(validExtractUser);

        mockMvc.perform(post("/api/user").contentType(MediaType.APPLICATION_JSON).content(userString))
                .andExpect(status().isCreated())

                .andExpect(content().string(expectedUserString));
    }

    @Test
    public void save_withInvalidDto_expectClientError() throws Exception {
        String userString = objectMapper.writeValueAsString(invalidUser);

        mockMvc.perform(post("/api/user").contentType(MediaType.APPLICATION_JSON).content(userString))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void save_withSameUsername_expectClientError() throws Exception {

        String userString = objectMapper.writeValueAsString(validUser);

        when(service.createUser(any())).thenThrow(new ValidationException("Username already exists"));

        mockMvc.perform(post("/api/user").contentType(MediaType.APPLICATION_JSON).content(userString))
                .andExpect(status().is4xxClientError());
    }

}