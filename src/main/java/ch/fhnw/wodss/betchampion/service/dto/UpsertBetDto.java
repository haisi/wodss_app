package ch.fhnw.wodss.betchampion.service.dto;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @author Hasan Kara
 */
public class UpsertBetDto {

    @NotNull
    private Long gameId;
    private Integer betGoalTeam1;
    private Integer betGoalTeam2;

    public UpsertBetDto() {
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Integer getBetGoalTeam1() {
        return betGoalTeam1;
    }

    public void setBetGoalTeam1(Integer betGoalTeam1) {
        this.betGoalTeam1 = betGoalTeam1;
    }

    public Integer getBetGoalTeam2() {
        return betGoalTeam2;
    }

    public void setBetGoalTeam2(Integer betGoalTeam2) {
        this.betGoalTeam2 = betGoalTeam2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpsertBetDto that = (UpsertBetDto) o;
        return Objects.equals(gameId, that.gameId) &&
            Objects.equals(betGoalTeam1, that.betGoalTeam1) &&
            Objects.equals(betGoalTeam2, that.betGoalTeam2);
    }

    @Override
    public int hashCode() {

        return Objects.hash(gameId, betGoalTeam1, betGoalTeam2);
    }

    @Override
    public String toString() {
        return "UpsertBetDto{" +
            "gameId=" + gameId +
            ", betGoalTeam1=" + betGoalTeam1 +
            ", betGoalTeam2=" + betGoalTeam2 +
            '}';
    }
}
