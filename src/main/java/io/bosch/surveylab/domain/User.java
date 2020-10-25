package io.bosch.surveylab.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class User extends BaseEntity {


    //TODO: Look up for unique annotation.


/*    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;
    */

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

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


    public User() {
    }

/*    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(User user) {
        this(user.getFirstName(), user.getLastName(), user.email);
        setId(user.getId());
        this.polls = new HashSet<>(user.polls);
    }*/

    public User(User user) { // TODO : SET USERNAME AND PASSWORD
        setId(user.getId());
        setCreatedOn(user.getCreatedOn());
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setPollOptions(user.getPollOptions());
        setPolls(user.getPolls());
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Poll> getPolls() {
        return polls;
    }

    public void setPolls(Set<Poll> polls) {
        this.polls = new HashSet<>(polls);
    }

    public void addPoll(Poll poll) {
        this.polls.add(poll);
    }

    public void addPollOption(PollOption pollOption) {
        this.pollOptions.add(pollOption);
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
