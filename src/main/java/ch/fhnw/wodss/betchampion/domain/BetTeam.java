package ch.fhnw.wodss.betchampion.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A BetTeam.
 */
@Entity
@Table(name = "bet_team")
public class BetTeam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "bet_team_name", nullable = false)
    private String betTeamName;

    @ManyToMany
    @JoinTable(name = "bet_team_members",
               joinColumns = @JoinColumn(name="bet_teams_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="members_id", referencedColumnName="id"))
    private Set<User> members = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBetTeamName() {
        return betTeamName;
    }

    public BetTeam betTeamName(String betTeamName) {
        this.betTeamName = betTeamName;
        return this;
    }

    public void setBetTeamName(String betTeamName) {
        this.betTeamName = betTeamName;
    }

    public Set<User> getMembers() {
        return members;
    }

    public BetTeam members(Set<User> users) {
        this.members = users;
        return this;
    }

    public BetTeam addMembers(User user) {
        this.members.add(user);
        return this;
    }

    public BetTeam removeMembers(User user) {
        this.members.remove(user);
        return this;
    }

    public void setMembers(Set<User> users) {
        this.members = users;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BetTeam betTeam = (BetTeam) o;
        if (betTeam.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), betTeam.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BetTeam{" +
            "id=" + getId() +
            ", betTeamName='" + getBetTeamName() + "'" +
            "}";
    }
}
