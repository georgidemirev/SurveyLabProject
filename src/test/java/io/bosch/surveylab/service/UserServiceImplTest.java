package io.bosch.surveylab.service;

import io.bosch.surveylab.domain.User;
import io.bosch.surveylab.dto.UserCreateDto;
import io.bosch.surveylab.dto.UserExtractDto;
import io.bosch.surveylab.mapper.UserMapperImpl;
import io.bosch.surveylab.repository.UserRepository;
import io.bosch.surveylab.rest.exceptions.ValidationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    private static final String EXPECTED_FIRST_NAME = "First Name 1";
    private static final String EXPECTED_LAST_NAME = "Second Name 1";
    private static final String EXPECTED_USERNAME = "Username 1";
    private static final String EXPECTED_PASSWORD = "Password 1";

    private UserCreateDto validUserDto;
    private User user;

    @Mock
    private UserRepository userRepository;

    @Spy
    private UserMapperImpl userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        validUserDto = new UserCreateDto();
        validUserDto.setFirstName(EXPECTED_FIRST_NAME);
        validUserDto.setLastName(EXPECTED_LAST_NAME);
        validUserDto.setUsername(EXPECTED_USERNAME);
        validUserDto.setPassword(EXPECTED_PASSWORD);

        user = new User();
        user = userMapper.toCreateEntity(validUserDto);
        user.setId(1L);
        user.setCreatedOn(LocalDateTime.now());
    }

    @Test
    public void save_ValidUser() throws ValidationException {

        when(userRepository.save(any())).thenReturn(user);

        UserExtractDto userExtractDto = userService.createUser(validUserDto);

        Mockito.verify(userRepository).save(any());

        Assert.assertEquals("wrong id returned", EXPECTED_FIRST_NAME, userExtractDto.getFirstName());
        Assert.assertEquals("wrong last name returned", EXPECTED_LAST_NAME, userExtractDto.getLastName());
        Assert.assertEquals("wrong username returned", EXPECTED_USERNAME, userExtractDto.getUsername());

    }


    @Transactional
    @Test(expected = ValidationException.class)
    public void save_TwoUsersWithSameUsername_throwValidationException() throws ValidationException {
        when(userRepository.findByUsername(validUserDto.getUsername())).thenReturn(Optional.ofNullable(user));
        userService.createUser(validUserDto);
    }


}