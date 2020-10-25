package io.bosch.surveylab.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Poll extends BaseEntity {

    @NotEmpty
    @Column(name = "text",
            nullable = false)
    private String text;

    @Column(name = "is_completed")
    private boolean isCompleted = false;

    @Column(name = "is_multiple_choice")
    private boolean isMultipleChoice = false;

    @NotNull
    @Column(name = "expiry_date",
            nullable = false)
    private LocalDateTime expiryDate;

    @ManyToOne
    private User user;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "poll_id")
    private List<PollOption> pollOptions = new ArrayList<>();

    public void addPollOption(PollOption pollOption) {
        pollOptions.add(pollOption);
        pollOption.setPoll(this);
    }

    public void removePollOption(PollOption pollOption) {
        pollOptions.remove(pollOption);
        pollOption.setPoll(null);
    }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<PollOption> getPollOptions() {
        return pollOptions;
    }

    public void setPollOptions(List<PollOption> pollOptions) {
        this.pollOptions = new ArrayList<>(pollOptions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Poll poll = (Poll) o;
        return getId().equals(poll.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
