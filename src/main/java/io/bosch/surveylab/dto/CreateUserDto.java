package io.bosch.surveylab.dto;

import java.util.HashSet;
import java.util.Set;

public class CreateUserDto extends BaseDtoEntity {

    private String firstName;

    private String lastName;

    private String email;

    private Set<PollDto> polls = new HashSet<>();

    private Set<PollOptionDto> pollOptions = new HashSet<>();

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<PollDto> getPolls() {
        return polls;
    }

    public void setPolls(Set<PollDto> polls) {
        this.polls = polls;
    }

    public Set<PollOptionDto> getPollOptions() {
        return pollOptions;
    }

    public void setPollOptions(Set<PollOptionDto> pollOptions) {
        this.pollOptions = pollOptions;
    }
}
