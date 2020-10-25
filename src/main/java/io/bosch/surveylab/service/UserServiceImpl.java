package io.bosch.surveylab.service;

import io.bosch.surveylab.domain.User;
import io.bosch.surveylab.dto.UserCreateDto;
import io.bosch.surveylab.dto.UserExtractDto;
import io.bosch.surveylab.mapper.UserMapper;
import io.bosch.surveylab.repository.UserRepository;
import io.bosch.surveylab.rest.exceptions.ValidationException;
import io.bosch.surveylab.service.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    public static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final UserMapper userMapper;


    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * @param userCreateDto username, password, first and last name
     * @return {@link UserExtractDto}
     * @throws ValidationException when:
     *                             if UserCreateDto username is already in the database
     */
    @Override
    @Transactional
    public UserExtractDto createUser(UserCreateDto userCreateDto) throws ValidationException {
        LOGGER.info("Invoked createUser method with user first name: {}, last name: {}, username: {}, password: {} ",
                userCreateDto.getFirstName(), userCreateDto.getLastName(), userCreateDto.getUsername(), userCreateDto.getPassword());
        // we receive not null user dto from the controller

        if (userRepository.findByUsername(userCreateDto.getUsername()).isPresent()) {
            throw new ValidationException(MessageFormat.format("Username: {0} already exists", userCreateDto.getUsername()));
        }

        User user = userMapper.toCreateEntity(userCreateDto);

        if (user.getCreatedOn() == null) {
            user.setCreatedOn(LocalDateTime.now());
        }

        return userMapper.toExtractDto(userRepository.save(user));
    }


}
