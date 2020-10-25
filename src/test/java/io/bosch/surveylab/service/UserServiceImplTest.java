package io.bosch.surveylab.service;

import io.bosch.surveylab.domain.User;
import io.bosch.surveylab.dto.CreateUserDto;
import io.bosch.surveylab.mapper.PollMapper;
import io.bosch.surveylab.mapper.PollOptionMapper;
import io.bosch.surveylab.mapper.UserMapper;
import io.bosch.surveylab.mapper.UserMapperImpl;
import io.bosch.surveylab.repository.interfaces.PollOptionRepository;
import io.bosch.surveylab.repository.interfaces.PollRepository;
import io.bosch.surveylab.repository.interfaces.UserRepository;
import io.bosch.surveylab.service.interfaces.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private PollRepository pollRepository;

    private PollOptionRepository pollOptionRepository;

    private PollMapper pollMapper;

    private UserMapper userMapper;

    private PollOptionMapper pollOptionMapper;

    private UserService userService;


    private User defaultUser;
    private User validDataUser;
    private User nullUser;


    @Before
    public void setUp() throws Exception {
        userMapper = new UserMapperImpl();
        initMocks(this);

        userService = new UserServiceImpl(userRepository, pollRepository,
                pollOptionRepository, pollMapper,
                userMapper, pollOptionMapper);

        defaultUser = new User();
        validDataUser = new User();
        validDataUser.setFirstName("Georgi");
        validDataUser.setLastName("Demirev");
        validDataUser.setEmail("mail.com");
    }

    @Test
    public void createValidUser() {// try the argument captor

        CreateUserDto createUserDto = userMapper.toDto(validDataUser);

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        //argumentCaptor.capture()

        Mockito.when(userRepository.createUser(defaultUser)).thenReturn(1L);

        //Assert.assertEquals("Georgi", argumentCaptor.getValue().getFirstName());


        long addedValUser = userService.createUser(createUserDto);

        Mockito.verify(userRepository).createUser(defaultUser);

        Assert.assertEquals("wrong id returned", 1L, addedValUser);
    }

    @Test
    public void createDefaultUser() {
        Mockito.when(userRepository.createUser(defaultUser)).thenReturn(1L);

        CreateUserDto createUserDto = userMapper.toDto(defaultUser);

        long addedDefUser = userService.createUser(createUserDto);

        Mockito.verify(userRepository).createUser(defaultUser);

        Assert.assertEquals("wrong id returned", 1L, addedDefUser);
    }

    @Test
    public void createNullUser() {
        Mockito.when(userRepository.createUser(null)).thenReturn(-1L);

        long addedNullUser = userService.createUser(null);

        Assert.assertEquals("wrong id returned", -1L, addedNullUser);
    }


    @Test
    public void findById() {
        Mockito.when(userRepository.findById(1L)).thenReturn(defaultUser);

        CreateUserDto addedDefUser = userService.findById(1L);

        Mockito.verify(userRepository).findById(1L);

        Assert.assertEquals("different first name returned", defaultUser.getFirstName(), addedDefUser.getFirstName());
        Assert.assertEquals("different id returned", defaultUser.getId(), addedDefUser.getId());

    }

    @Test
    public void update() {
        validDataUser.setId(1L);
        defaultUser.setId(1L);

        CreateUserDto createUserDto = userMapper.toDto(validDataUser);

        Mockito.when(userRepository.findById(1L)).thenReturn(defaultUser);
        Mockito.when(userRepository.update(eq(defaultUser))).thenReturn(validDataUser);

        CreateUserDto updatedDefUser = userService.update(createUserDto);

        Mockito.verify(userRepository).update(eq(defaultUser));

        Long expectedID = 1L;

        Assert.assertEquals("different id returned", expectedID, updatedDefUser.getId());
    }

    @Test
    public void delete() {
        Mockito.doNothing().when(userRepository).delete(1L);

        userService.delete(1L);

        Mockito.verify(userRepository, Mockito.times(1)).delete(1L);
    }

    @Test
    public void findAll() {
        Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList(defaultUser, validDataUser));

        Assert.assertEquals("wrong size of findAll array", 2, userService.findAll().size());

        Mockito.verify(userRepository).findAll();
    }

}