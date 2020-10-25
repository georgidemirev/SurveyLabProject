package io.bosch.surveylab.rest;

import io.bosch.surveylab.dto.UserCreateDto;
import io.bosch.surveylab.dto.UserExtractDto;
import io.bosch.surveylab.rest.exceptions.ValidationException;
import io.bosch.surveylab.service.interfaces.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Create user",
            notes = "Create user with first and last name, username and password",
            httpMethod = "POST", code = 201)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = UserExtractDto.class),
            @ApiResponse(code = 400, message = "Bad request, invalid input of parameters", response = ValidationException.class)
    })
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public UserExtractDto createUser(@RequestBody @Valid UserCreateDto userCreateDto) throws ValidationException {
        return userService.createUser(userCreateDto);
    }
}
