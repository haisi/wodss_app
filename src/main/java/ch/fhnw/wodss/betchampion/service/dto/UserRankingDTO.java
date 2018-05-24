package ch.fhnw.wodss.betchampion.service.dto;

import ch.fhnw.wodss.betchampion.config.Constants;
import ch.fhnw.wodss.betchampion.domain.Authority;
import ch.fhnw.wodss.betchampion.domain.User;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserRankingDTO implements Comparable<UserRankingDTO> {
    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    private Integer points;
    private Integer rank;

    public UserRankingDTO() {
        // Empty constructor needed for Jackson.
    }

    public UserRankingDTO(User user) {
        this.login = user.getLogin();
        this.points = user.getPoints();
        this.rank = user.getRank();
    }

    /**
     * <pre>
     *     Rank : Score : Login
     *     1 : 9 : Hans
     *     1 : 9 : Hans1
     *     3 : 6 : Urs
     *     3 : 6 : Urs2
     *     5 : 2 : daniel
     *     6 : 0 : Looser
     * </pre>
     *
     * Adds the rank to a list of users.
     *
     * @param rankings
     * @return
     */
    public static List<UserRankingDTO> addRanking(List<UserRankingDTO> rankings) {

        if (rankings.isEmpty()) {
            return rankings;
        } else if (rankings.size() == 1) {
            rankings.get(0).rank = 1;
            return rankings;
        }

        rankings.sort(UserRankingDTO::compareTo);

        int currentRank = 1;
        int prevPoints = rankings.get(0).points;

        for (int i = 0; i < rankings.size(); i++) {
            UserRankingDTO dto = rankings.get(i);
            Integer currentPoints = dto.points;

            if (currentPoints == prevPoints) {
                dto.rank = currentRank;
            } else {
                currentRank = i + 1;
                dto.rank = currentRank;
            }

            prevPoints = dto.points;
        }

        return rankings;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "UserRankingDTO{" +
            "login='" + login + '\'' +
            ", points=" + points +
            ", rank=" + rank +
            '}';
    }

    @Override
    public int compareTo(UserRankingDTO o) {
        return Integer.compare(o.points, this.points);
    }
}
