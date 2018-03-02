package ch.fhnw.wodss.betchampion.service.dto;

import ch.fhnw.wodss.betchampion.config.Constants;
import ch.fhnw.wodss.betchampion.domain.Authority;
import ch.fhnw.wodss.betchampion.domain.User;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserRankingDTO {
    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;


    private Integer points;

    public UserRankingDTO() {
        // Empty constructor needed for Jackson.
    }

    public UserRankingDTO(User user) {
        this.login = user.getLogin();
        this.points = user.getPoints();
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

    @Override
    public String toString() {
        return "UserDTO{" +
            "login='" + login + '\'' +
            ", points=" + points +
            "}";
    }
}
