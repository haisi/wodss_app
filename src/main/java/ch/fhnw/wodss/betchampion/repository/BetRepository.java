package ch.fhnw.wodss.betchampion.repository;

import ch.fhnw.wodss.betchampion.domain.Bet;
import ch.fhnw.wodss.betchampion.domain.Game;
import ch.fhnw.wodss.betchampion.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<Bet> findByUserIsCurrentUser(Pageable pageable);

    List<Bet> findAllByUser(User user);

    List<Bet> findAllByGame(Game game);
    List<Bet> findAllByGameId(Long gameId);
}
