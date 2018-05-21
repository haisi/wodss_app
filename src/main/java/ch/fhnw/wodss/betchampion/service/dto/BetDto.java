package ch.fhnw.wodss.betchampion.service.dto;

import ch.fhnw.wodss.betchampion.domain.Stats;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * @author Hasan Kara
 */
public class BetDto {

    @NotNull
    private long gameId;
    private ZonedDateTime matchTime;
    private boolean gameClosed;

    @NotNull
    private long team1Id;
    @NotEmpty
    private String team1Name;
    @NotNull
    private long team2Id;
    @NotEmpty
    private String team2Name;

    private Integer goalsTeam1;
    private Integer goalsTeam2;

    private Long betId;
    private Long userId;
    private Integer betGoalTeam1;
    private Integer betGoalTeam2;
    private Stats stats;
    private String info;

    public BetDto() {}

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public ZonedDateTime getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(ZonedDateTime matchTime) {
        this.matchTime = matchTime;
    }

    public boolean isGameClosed() {
        return gameClosed;
    }

    public void setGameClosed(boolean gameClosed) {
        this.gameClosed = gameClosed;
    }

    public long getTeam1Id() {
        return team1Id;
    }

    public void setTeam1Id(long team1Id) {
        this.team1Id = team1Id;
    }

    public String getTeam1Name() {
        return team1Name;
    }

    public void setTeam1Name(String team1Name) {
        this.team1Name = team1Name;
    }

    public long getTeam2Id() {
        return team2Id;
    }

    public void setTeam2Id(long team2Id) {
        this.team2Id = team2Id;
    }

    public String getTeam2Name() {
        return team2Name;
    }

    public void setTeam2Name(String team2Name) {
        this.team2Name = team2Name;
    }

    public Integer getGoalsTeam1() {
        return goalsTeam1;
    }

    public void setGoalsTeam1(Integer goalsTeam1) {
        this.goalsTeam1 = goalsTeam1;
    }

    public Integer getGoalsTeam2() {
        return goalsTeam2;
    }

    public void setGoalsTeam2(Integer goalsTeam2) {
        this.goalsTeam2 = goalsTeam2;
    }

    public Long getBetId() {
        return betId;
    }

    public void setBetId(Long betId) {
        this.betId = betId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BetDto betDto = (BetDto) o;
        return gameId == betDto.gameId &&
            gameClosed == betDto.gameClosed &&
            team1Id == betDto.team1Id &&
            team2Id == betDto.team2Id &&
            Objects.equals(matchTime, betDto.matchTime) &&
            Objects.equals(team1Name, betDto.team1Name) &&
            Objects.equals(team2Name, betDto.team2Name) &&
            Objects.equals(goalsTeam1, betDto.goalsTeam1) &&
            Objects.equals(goalsTeam2, betDto.goalsTeam2) &&
            Objects.equals(betId, betDto.betId) &&
            Objects.equals(userId, betDto.userId) &&
            Objects.equals(betGoalTeam1, betDto.betGoalTeam1) &&
            Objects.equals(betGoalTeam2, betDto.betGoalTeam2) &&
            Objects.equals(stats, betDto.stats) &&
            Objects.equals(info, betDto.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, matchTime, gameClosed, team1Id, team1Name, team2Id, team2Name, goalsTeam1, goalsTeam2, betId, userId, betGoalTeam1, betGoalTeam2, stats, info);
    }

    @Override
    public String toString() {
        return "BetDto{" +
            "gameId=" + gameId +
            ", matchTime=" + matchTime +
            ", gameClosed=" + gameClosed +
            ", team1Id=" + team1Id +
            ", team1Name='" + team1Name + '\'' +
            ", team2Id=" + team2Id +
            ", team2Name='" + team2Name + '\'' +
            ", goalsTeam1=" + goalsTeam1 +
            ", goalsTeam2=" + goalsTeam2 +
            ", betId=" + betId +
            ", userId=" + userId +
            ", betGoalTeam1=" + betGoalTeam1 +
            ", betGoalTeam2=" + betGoalTeam2 +
            ", stats=" + stats +
            ", info='" + info + '\'' +
            '}';
    }
}
