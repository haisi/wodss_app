package ch.fhnw.wodss.betchampion.repository;

import ch.fhnw.wodss.betchampion.domain.Bet;
import ch.fhnw.wodss.betchampion.domain.User;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Bet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BetRepository extends JpaRepository<Bet, Long> {

    @Query("select bet from Bet bet where bet.user.login = ?#{principal.username}")
    List<Bet> findByUserIsCurrentUser();

    List<Bet> findAllByUser(User user);
}
