package ch.fhnw.wodss.betchampion.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Bet.
 */
@Entity
@Table(name = "bet")
public class Bet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(value = 0)
    @Max(value = 99)
    @Column(name = "goals_team_1", nullable = false)
    private Integer goalsTeam1;

    @NotNull
    @Min(value = 0)
    @Max(value = 99)
    @Column(name = "goals_team_2", nullable = false)
    private Integer goalsTeam2;

    @ManyToOne
    private User user;

    @ManyToOne
    private Game game;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGoalsTeam1() {
        return goalsTeam1;
    }

    public Bet goalsTeam1(Integer goalsTeam1) {
        this.goalsTeam1 = goalsTeam1;
        return this;
    }

    public void setGoalsTeam1(Integer goalsTeam1) {
        this.goalsTeam1 = goalsTeam1;
    }

    public Integer getGoalsTeam2() {
        return goalsTeam2;
    }

    public Bet goalsTeam2(Integer goalsTeam2) {
        this.goalsTeam2 = goalsTeam2;
        return this;
    }

    public void setGoalsTeam2(Integer goalsTeam2) {
        this.goalsTeam2 = goalsTeam2;
    }

    public User getUser() {
        return user;
    }

    public Bet user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Game getGame() {
        return game;
    }

    public Bet game(Game game) {
        this.game = game;
        return this;
    }

    public void setGame(Game game) {
        this.game = game;
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
        Bet bet = (Bet) o;
        if (bet.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Bet{" +
            "id=" + getId() +
            ", goalsTeam1=" + getGoalsTeam1() +
            ", goalsTeam2=" + getGoalsTeam2() +
            "}";
    }
}
