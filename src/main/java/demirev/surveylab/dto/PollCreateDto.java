package demirev.surveylab.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PollCreateDto {

    @NotEmpty
    private String text;

    private boolean isCompleted = false;

    private boolean isMultipleChoice = false;

    private LocalDateTime expiryDate;

    @NotNull
    private Long userId;

    @NotNull
    @Size(min = 2)
    private List<String> pollOptionStrings;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<String> getPollOptionStrings() {
        return pollOptionStrings;
    }

    public void setPollOptionStrings(List<String> pollOptionStrings) {
        this.pollOptionStrings = new ArrayList<>(pollOptionStrings);
    }
}
