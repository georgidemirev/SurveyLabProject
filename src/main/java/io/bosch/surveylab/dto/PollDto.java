package io.bosch.surveylab.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class PollDto extends BaseDtoEntity{

    private boolean allowMultipleAnswers;

    private boolean isCompleted;

    private boolean isAnonymous;

    private String pollName;

    private Long userId;

    private Set<PollOptionDto> pollOptions = new HashSet<>();

    public boolean isAllowMultipleAnswers() {
        return allowMultipleAnswers;
    }

    public void setAllowMultipleAnswers(boolean allowMultipleAnswers) {
        this.allowMultipleAnswers = allowMultipleAnswers;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    public String getPollName() {
        return pollName;
    }

    public void setPollName(String pollName) {
        this.pollName = pollName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<PollOptionDto> getPollOptions() {
        return pollOptions;
    }

    public void setPollOptions(Set<PollOptionDto> pollOptions) {
        this.pollOptions = pollOptions;
    }
}
