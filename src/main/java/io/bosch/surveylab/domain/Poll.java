package io.bosch.surveylab.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Poll extends BaseEntity {

    @Column(name = "allow_multiple_answers")
    private boolean allowMultipleAnswers;

    @Column(name = "is_completed")
    private boolean isCompleted;

    @Column(name = "is_anonymous")
    private boolean isAnonymous;

    @Column(name = "poll_name")
    private String pollName;

    @JsonIgnore
    @ManyToOne
    private User user;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "poll_id")
    private Set<PollOption> pollOptions = new HashSet<>();


    public Poll() {
    }

    public Poll(String pollName, Set<PollOption> options, boolean allowMultipleAnswers, boolean isCompleted, boolean isAnonymous) {
        this.pollName = pollName;
        this.pollOptions = options;
        this.allowMultipleAnswers = allowMultipleAnswers;
        this.isCompleted = isCompleted;
        this.isAnonymous = isAnonymous;
    }

    public Poll(Poll newPoll) {
        setId(newPoll.getId());
        this.pollName = newPoll.getPollName();
        this.pollOptions = newPoll.getPollOptions();
        this.allowMultipleAnswers = newPoll.isAllowMultipleAnswers();
        this.isCompleted = newPoll.isCompleted();
        this.isAnonymous = newPoll.isAnonymous();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPollName() {
        return pollName;
    }

    public void setPollName(String pollName) {
        this.pollName = pollName;
    }

    public Set<PollOption> getPollOptions() {
        return pollOptions;
    }

    public void setPollOptions(Set<PollOption> pollOptions) {
        this.pollOptions = pollOptions;
    }

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

    public void addPollOption(PollOption pollOption) {
        pollOptions.add(pollOption);
    }

    public void removePollOption(PollOption pollOption) {
        this.pollOptions.remove(pollOption);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Poll poll = (Poll) o;
        return getId() == poll.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
