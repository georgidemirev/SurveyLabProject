package demirev.surveylab.service.interfaces;

import demirev.surveylab.dto.UserCreateDto;
import demirev.surveylab.dto.UserExtractDto;
import demirev.surveylab.rest.exceptions.ValidationException;

public interface UserService {

    UserExtractDto createUser(UserCreateDto userCreateDto) throws IllegalArgumentException, ValidationException;
}
