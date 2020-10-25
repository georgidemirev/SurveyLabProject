package io.bosch.surveylab.service.interfaces;

import io.bosch.surveylab.dto.UserCreateDto;
import io.bosch.surveylab.dto.UserExtractDto;
import io.bosch.surveylab.rest.exceptions.ValidationException;

public interface UserService {

    UserExtractDto createUser(UserCreateDto userCreateDto) throws IllegalArgumentException, ValidationException;
}
