package ch.fhnw.wodss.betchampion.domain;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Game.
 */
@Entity
@Table(name = "game")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "match_time", nullable = false)
    private ZonedDateTime matchTime;

    @Min(value = 0)
    @Max(value = 99)
    @Column(name = "goals_team_1", nullable = false)
    private Integer goalsTeam1;

    @Min(value = 0)
    @Max(value = 99)
    @Column(name = "goals_team_2", nullable = false)
    private Integer goalsTeam2;

    @ManyToOne
    private Team team1;

    @ManyToOne
    private Team team2;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private Integer totalBets;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private Integer team1Won;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private Integer team2Won;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private Integer draw;


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getMatchTime() {
        return matchTime;
    }

    public Game matchTime(ZonedDateTime matchTime) {
        this.matchTime = matchTime;
        return this;
    }

    public void setMatchTime(ZonedDateTime matchTime) {
        this.matchTime = matchTime;
    }

    public Integer getGoalsTeam1() {
        return goalsTeam1;
    }

    public Game goalsTeam1(Integer goalsTeam1) {
        this.goalsTeam1 = goalsTeam1;
        return this;
    }

    public void setGoalsTeam1(Integer goalsTeam1) {
        this.goalsTeam1 = goalsTeam1;
    }

    public Integer getGoalsTeam2() {
        return goalsTeam2;
    }

    public Game goalsTeam2(Integer goalsTeam2) {
        this.goalsTeam2 = goalsTeam2;
        return this;
    }

    public void setGoalsTeam2(Integer goalsTeam2) {
        this.goalsTeam2 = goalsTeam2;
    }

    public Team getTeam1() {
        return team1;
    }

    public Game team1(Team team) {
        this.team1 = team;
        return this;
    }

    public void setTeam1(Team team) {
        this.team1 = team;
    }

    public Team getTeam2() {
        return team2;
    }

    public Game team2(Team team) {
        this.team2 = team;
        return this;
    }

    public void setTeam2(Team team) {
        this.team2 = team;
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
        Game game = (Game) o;
        if (game.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), game.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Game{" +
            "id=" + getId() +
            ", matchTime='" + getMatchTime() + "'" +
            ", goalsTeam1=" + getGoalsTeam1() +
            ", goalsTeam2=" + getGoalsTeam2() +
            "}";
    }


    public Integer getTotalBets() {
        return 30;//totalBets;
    }

    public Integer getTeam1Won() {
        return 5;//team1Won;
    }

    public Integer getTeam2Won() {
        return 10;//team2Won;
    }

    public Integer getDraw() {
        return 15;//draw;
    }

    public void setStats(Stats stats) {
        this.totalBets = stats.total;
        this.team1Won = stats.winTeam1;
        this.team2Won = stats.winTeam2;
        this.draw = stats.draw;
    }
}
