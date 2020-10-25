package io.bosch.surveylab.rest;

import io.bosch.surveylab.dto.PollDto;
import io.bosch.surveylab.dto.PollOptionDto;
import io.bosch.surveylab.service.PollServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PollController {

    @Autowired
    private PollServiceImpl pollService;

    public PollController(PollServiceImpl pollService) {
        this.pollService = pollService;
    }

    @PostMapping("/poll")
    public long createPoll(@RequestBody PollDto pollDto) {
        return pollService.createPoll(pollDto);
    }

    @PostMapping("/poll/{id}")
    public PollDto addPollOption(@PathVariable("id") Long pollId, @RequestBody PollOptionDto pollOptionDto) {
        return pollService.addPollOption(pollId, pollOptionDto);
    }

    @GetMapping("/poll/{id}")
    public PollDto findById(@PathVariable("id") Long id) {
        return pollService.findById(id);
    }

    @PutMapping("/poll/{id}")
    public PollDto updatePollById(@RequestBody PollDto pollDto) {
        return pollService.updatePollById(pollDto);
    }

    @DeleteMapping("/poll/{id}")
    public void deletePollById(@PathVariable("id") Long id) {
        pollService.deletePollById(id);
    }

    @GetMapping("/poll")
    public List<PollDto> findAll() {
        return pollService.findAll();
    }
}
