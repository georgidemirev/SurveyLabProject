package io.bosch.surveylab.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class User extends BaseEntity {

    @NotEmpty
    @Column(name = "username",
            unique = true,
            nullable = false)
    private String username;

    @NotEmpty
    @Column(name = "password",
            nullable = false)
    private String password;

    @NotEmpty
    @Column(name = "first_name",
            nullable = false)
    private String firstName;

    @NotEmpty
    @Column(name = "last_name",
            nullable = false)
    private String lastName;

    @OneToMany(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "user_id")
    private Set<Poll> polls = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_poll_option",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "poll_option_id")
    )
    private Set<PollOption> pollOptions = new HashSet<>();

    public void addPoll(Poll poll) {
        polls.add(poll);
        poll.setUser(this);
    }

    public void removePoll(Poll poll) {
        polls.remove(poll);
        poll.setUser(null);
    }

    public void addPollOption(PollOption pollOption) {
        pollOptions.add(pollOption);
        pollOption.getUsers().add(this);
    }

    public void removePollOption(PollOption pollOption) {
        pollOptions.remove(pollOption);
        pollOption.getUsers().remove(this);
    }

    public Set<PollOption> getPollOptions() {
        return pollOptions;
    }

    public void setPollOptions(Set<PollOption> pollOptions) {
        this.pollOptions = new HashSet<>(pollOptions);
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

    public Set<Poll> getPolls() {
        return polls;
    }

    public void setPolls(Set<Poll> polls) {
        this.polls = new HashSet<>(polls);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
