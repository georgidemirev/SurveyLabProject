package io.bosch.surveylab.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class PollOption extends BaseEntity {

    @Column(name = "text")
    private String text;

    @Column(name = "number_of_votes")
    private int numberOfVotes;

    @JsonIgnore
    @ManyToOne
    private Poll poll;

    @JsonIgnore
    @ManyToMany(mappedBy = "pollOptions")
    private Set<User> users = new HashSet<>();

    public PollOption() {
    }

    public PollOption(String text, Integer numberOfVotes) {
        this.text = text;
        this.numberOfVotes = numberOfVotes;
    }

    public PollOption(PollOption pollOption) {
        this(pollOption.getText(), pollOption.getNumberOfVotes());
        setId(pollOption.getId());
        this.setCreatedOn(pollOption.getCreatedOn());
        this.setNumberOfVotes(pollOption.getNumberOfVotes());
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getNumberOfVotes() {
        return numberOfVotes;
    }

    public void setNumberOfVotes(int numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }

    public void setNumberOfVotes(Integer numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PollOption that = (PollOption) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

