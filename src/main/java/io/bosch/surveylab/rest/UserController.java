package io.bosch.surveylab.rest;

import io.bosch.surveylab.dto.CreateUserDto;
import io.bosch.surveylab.dto.PollDto;
import io.bosch.surveylab.dto.PollOptionDto;
import io.bosch.surveylab.service.interfaces.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public Long createUser(@RequestBody CreateUserDto createUserDto) {
        return userService.createUser(createUserDto);
    }


/*    @PostMapping("/user/addPollOption/{id}")
    public User addPollOption(@PathVariable("id") Long userId, @RequestBody PollOptionDto pollOption) {
        userService.vote(userId, pollOption);
        return userService.findById(userId);
    }*/

    @PostMapping("/user/{id}")
    public CreateUserDto addPoll(@PathVariable("id") Long userId, @RequestBody PollDto pollDto) {
        return userService.addPoll(userId, pollDto);
    }

    @PostMapping("/user/vote/{id}")
    public CreateUserDto vote(@PathVariable("id") Long userId, @RequestBody PollOptionDto pollOptionDto) {
        return userService.vote(userId, pollOptionDto);
    }

    @GetMapping("/user/{id}")
    public CreateUserDto findById(@PathVariable("id") long id) {
        return userService.findById(id);
    }

    @PutMapping("/user/{id}")
    public CreateUserDto update(@RequestBody CreateUserDto createUserDto) {
        return userService.update(createUserDto);
    }

    @DeleteMapping("/user/{id}")
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }

    @GetMapping("/user")
    public List<CreateUserDto> findAll() {
        return userService.findAll();
    }

}
