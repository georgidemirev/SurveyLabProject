package io.bosch.surveylab.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bosch.surveylab.domain.User;
import io.bosch.surveylab.dto.CreateUserDto;
import io.bosch.surveylab.mapper.UserMapper;
import io.bosch.surveylab.service.interfaces.UserService;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserMapper userMapper;

    @MockBean
    private UserService service;

    private User user1;
    private User user2;

    @Before
    public void setUp() throws Exception {
        user1 = new User();
        user1.setFirstName("g1");
        user1.setLastName("v1");
        user1.setEmail("m1");
        user2 = new User();
        user1.setFirstName("g2");
        user1.setLastName("v2");
        user1.setEmail("m2");
    }

    @Test
    public void testGetUser() throws Exception {
        CreateUserDto createUserDto = userMapper.toDto(user1);

        Mockito.when(service.findById(1L)).thenReturn(createUserDto);

        String userString = objectMapper.writeValueAsString(user1);

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(userString));
    }

    @Test
    public void testCreateUser_Post() throws Exception {
        CreateUserDto createUserDto = userMapper.toDto(user1);

        Mockito.when(service.createUser(createUserDto)).thenReturn(1L);

        String userString = objectMapper.writeValueAsString(user1);

        mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(userString))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(1L)));
    }

    @Test
    public void testUpdate() throws Exception {

        user1.setId(1L);
        user1.setCreatedOn(LocalDateTime.now());

        CreateUserDto createUserDto = userMapper.toDto(user1);

        Mockito.when(service.update(any())).thenReturn(createUserDto);

        String userDtoString = objectMapper.writeValueAsString(service.update(createUserDto));

        mockMvc.perform(put("/user/1").contentType(MediaType.APPLICATION_JSON).content(userDtoString))
                .andExpect(status().isOk())
                .andExpect(content().string(userDtoString));
    }

    @Test
    public void testDelete() throws Exception {
        Mockito.doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAll() throws Exception {
        CreateUserDto createUserDto1 = userMapper.toDto(user1);
        CreateUserDto createUserDto2 = userMapper.toDto(user2);

        List<CreateUserDto> expectedListOfDtos = new ArrayList<>();
        expectedListOfDtos.add(createUserDto1);
        expectedListOfDtos.add(createUserDto2);

        Mockito.when(service.findAll()).thenReturn(expectedListOfDtos);

        String listString = objectMapper.writeValueAsString(expectedListOfDtos);

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().string(listString));
    }


}