package io.bosch.surveylab.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class PollOption extends BaseEntity {

    @NotEmpty
    @Column(name = "text",
            nullable = false)
    private String text;

    @Column(name = "number_of_votes")
    private int numberOfVotes = 0;

    @NotNull
    @ManyToOne
    private Poll poll;

    @ManyToMany(mappedBy = "pollOptions")
    private Set<User> users = new HashSet<>();

    public void addUser(User user) {
        users.add(user);
        user.getPollOptions().add(this);
    }

    public void removeUser(User user) {
        users.remove(user);
        user.getPollOptions().remove(this);
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = new HashSet<>(users);
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

