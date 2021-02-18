package demirev.surveylab.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class VoteDto {

    @NotNull
    Long pollId;

    @NotNull
    Long userId;

    @NotNull
    @Size(min = 1)
    List<Long> pollOptionIds;

    public Long getPollId() {
        return pollId;
    }

    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getPollOptionIds() {
        return pollOptionIds;
    }

    public void setPollOptionIds(List<Long> pollOptionIds) {
        this.pollOptionIds = pollOptionIds;
    }
}
