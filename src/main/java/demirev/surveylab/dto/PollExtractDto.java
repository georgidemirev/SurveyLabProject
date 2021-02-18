package demirev.surveylab.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PollExtractDto {

    private String text;

    private boolean isCompleted;

    private boolean isMultipleChoice;

    private LocalDateTime expiryDate;

    private String user;

    private List<PollOptionDto> pollOptionDtos = new ArrayList<>();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isMultipleChoice() {
        return isMultipleChoice;
    }

    public void setMultipleChoice(boolean multipleChoice) {
        isMultipleChoice = multipleChoice;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<PollOptionDto> getPollOptionDtos() {
        return pollOptionDtos;
    }

    public void setPollOptionDtos(List<PollOptionDto> pollOptionDtos) {
        this.pollOptionDtos = pollOptionDtos;
    }
}
