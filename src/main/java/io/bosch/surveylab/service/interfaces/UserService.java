package io.bosch.surveylab.service.interfaces;

import io.bosch.surveylab.dto.CreateUserDto;
import io.bosch.surveylab.dto.PollDto;
import io.bosch.surveylab.dto.PollOptionDto;

import java.util.List;

public interface UserService {

    Long createUser(CreateUserDto createUserDto);

    CreateUserDto findById(Long id);

    CreateUserDto addPoll(Long userId, PollDto pollDto);

    CreateUserDto vote(Long userId, PollOptionDto pollOptionDto);

    CreateUserDto update(CreateUserDto createUserDto);

    void delete(Long id);

    List<CreateUserDto> findAll();
}
