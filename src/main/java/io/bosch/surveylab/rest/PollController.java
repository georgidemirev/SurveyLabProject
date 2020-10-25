package io.bosch.surveylab.rest;

import io.bosch.surveylab.configuration.annotations.ApiPageable;
import io.bosch.surveylab.dto.PollCreateDto;
import io.bosch.surveylab.dto.PollExtractDto;
import io.bosch.surveylab.dto.VoteDto;
import io.bosch.surveylab.rest.exceptions.NotFoundException;
import io.bosch.surveylab.rest.exceptions.ValidationException;
import io.bosch.surveylab.service.interfaces.PollService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.List;

@RestController
@RequestMapping("/api/polls")
public class PollController {

    private final PollService pollService;

    public PollController(PollService pollService) {
        this.pollService = pollService;
    }


    @ApiOperation(value = "Create poll",
            notes = "Create poll with text, completed and multiple choice flag, user id and list of poll option texts",
            httpMethod = "POST", code = 201)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "OK", response = PollExtractDto.class),
            @ApiResponse(code = 400, message = "Bad request, invalid input of parameters", response = ValidationException.class)
    })
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public PollExtractDto createPoll(@RequestBody @Valid PollCreateDto pollCreateDto) throws ValidationException {
        return pollService.createPoll(pollCreateDto);
    }

    @ApiOperation(value = "Get poll by id",
            notes = "Get poll with given id if it exists",
            httpMethod = "GET", code = 200)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = PollExtractDto.class),
            @ApiResponse(code = 404, message = "Not found", response = NotFoundException.class)
    })
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/{id}")
    public PollExtractDto findById(@PathVariable Long id) throws NotFoundException {
        return pollService.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("poll with id: {0} does not exist", id)));

    }

    @ApiOperation(value = "Get all polls that user has created",
            notes = "Get all polls that user has created",
            httpMethod = "GET", code = 200)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = List.class),
            @ApiResponse(code = 404, message = "Not found", response = NotFoundException.class)
    })
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("by-username")
    @ApiPageable
    public Page<PollExtractDto> findAllByUsername(@RequestParam("username") String username, Pageable pageable) throws ValidationException {
        return pollService.findAllByUsername(username, pageable);
    }

    @ApiOperation(value = "Vote for poll options",
            notes = "Vote for given list of poll options with given poll id and given user id",
            httpMethod = "PUT", code = 200)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "OK", response = PollExtractDto.class),
            @ApiResponse(code = 400, message = "Bad request, invalid input of parameters", response = ValidationException.class)
    })
    @ResponseStatus(code = HttpStatus.OK)
    @PutMapping("/vote")
    public PollExtractDto vote(@RequestBody @Valid VoteDto voteDto) throws ValidationException {
        return pollService.vote(voteDto);
    }
}
