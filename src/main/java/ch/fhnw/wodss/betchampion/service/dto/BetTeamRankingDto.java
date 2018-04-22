package ch.fhnw.wodss.betchampion.service.dto;

import ch.fhnw.wodss.betchampion.domain.BetTeam;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Hasan Kara
 */
public class BetTeamRankingDto {

    private Long id;
    private String betTeamName;
    private List<UserRankingDTO> members = new ArrayList<>();

    public BetTeamRankingDto() {}

    public BetTeamRankingDto(BetTeam betTeam) {
        this.id = betTeam.getId();
        this.betTeamName = betTeam.getBetTeamName();
        this.members = betTeam.getMembers().stream().map(UserRankingDTO::new).sorted().collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBetTeamName() {
        return betTeamName;
    }

    public void setBetTeamName(String betTeamName) {
        this.betTeamName = betTeamName;
    }

    public List<UserRankingDTO> getMembers() {
        return members;
    }

    public void setMembers(List<UserRankingDTO> members) {
        this.members = members;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BetTeamRankingDto that = (BetTeamRankingDto) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(betTeamName, that.betTeamName) &&
            Objects.equals(members, that.members);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, betTeamName, members);
    }

    @Override
    public String toString() {
        return "BetTeamRankingDto{" +
            "id=" + id +
            ", betTeamName='" + betTeamName + '\'' +
            ", members=" + members +
            '}';
    }
}
