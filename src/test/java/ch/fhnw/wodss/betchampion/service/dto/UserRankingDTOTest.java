package ch.fhnw.wodss.betchampion.service.dto;

import ch.fhnw.wodss.betchampion.domain.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * @author Hasan Kara
 */
public class UserRankingDTOTest {

    @Test
    public void addRanking() {

        List<UserRankingDTO> noRanking = new ArrayList<>();
        // 1
        UserRankingDTO dto = new UserRankingDTO();
        dto.setLogin("Hans");
        dto.setPoints(9);
        noRanking.add(dto);
        dto = new UserRankingDTO();
        dto.setLogin("Hans1");
        dto.setPoints(9);
        noRanking.add(dto);
        // 3
        dto = new UserRankingDTO();
        dto.setLogin("Urs");
        dto.setPoints(6);
        noRanking.add(dto);
        dto = new UserRankingDTO();
        dto.setLogin("Urs2");
        dto.setPoints(6);
        noRanking.add(dto);
        // 6
        dto = new UserRankingDTO();
        dto.setLogin("Skrrt");
        dto.setPoints(2);
        noRanking.add(dto);
        // 7
        dto = new UserRankingDTO();
        dto.setLogin("Looser");
        dto.setPoints(0);
        noRanking.add(dto);

        List<UserRankingDTO> ranked = UserRankingDTO.addRanking(noRanking);
        for (UserRankingDTO foo : ranked) {
            System.out.println(foo.getRank() + " : "  + foo.getPoints() + " : " + foo.getLogin());
        }

        assertThat("expted").isEqualTo("expted");
    }
}
