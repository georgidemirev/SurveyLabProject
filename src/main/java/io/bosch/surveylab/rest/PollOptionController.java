package io.bosch.surveylab.rest;

import io.bosch.surveylab.domain.PollOption;
import io.bosch.surveylab.service.interfaces.PollOptionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PollOptionController {

    private PollOptionService pollOptionService;

    public PollOptionController(PollOptionService pollOptionService) {
        this.pollOptionService = pollOptionService;
    }

    @PostMapping("/pollOption")
    public Long createPollOption(@RequestBody PollOption pollOption) {
        return pollOptionService.createPollOption(pollOption);
    }

    @GetMapping("/pollOption/{id}")
    public PollOption findById(@PathVariable("id") Long id) {
        return pollOptionService.findById(id);
    }

    @PutMapping("/pollOption/{id}")
    public PollOption update(@RequestBody PollOption pollOption) {
        return pollOptionService.update(pollOption);
    }

    @DeleteMapping("/pollOption/{id}")
    public void delete(@PathVariable("id") Long id) {
        pollOptionService.delete(id);
    }

    @GetMapping("/pollOption")
    public List<PollOption> findAll() {
        return pollOptionService.findAll();
    }

}
