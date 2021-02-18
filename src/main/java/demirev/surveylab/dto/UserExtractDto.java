package demirev.surveylab.dto;

import java.util.HashSet;
import java.util.Set;

public class UserExtractDto {

    private String username;

    private String firstName;

    private String lastName;

    private Set<PollExtractDto> pollExtractDtos = new HashSet<>();

    private Set<PollOptionDto> pollOptionDtos = new HashSet<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public Set<PollExtractDto> getPollExtractDtos() {
        return pollExtractDtos;
    }

    public void setPollExtractDtos(Set<PollExtractDto> pollExtractDtos) {
        this.pollExtractDtos = pollExtractDtos;
    }

    public Set<PollOptionDto> getPollOptionDtos() {
        return pollOptionDtos;
    }

    public void setPollOptionDtos(Set<PollOptionDto> pollOptionDtos) {
        this.pollOptionDtos = pollOptionDtos;
    }
}
