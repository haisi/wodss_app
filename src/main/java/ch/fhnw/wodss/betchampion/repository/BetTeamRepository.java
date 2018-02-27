package ch.fhnw.wodss.betchampion.repository;

import ch.fhnw.wodss.betchampion.domain.BetTeam;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the BetTeam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BetTeamRepository extends JpaRepository<BetTeam, Long> {
    @Query("select distinct bet_team from BetTeam bet_team left join fetch bet_team.members")
    List<BetTeam> findAllWithEagerRelationships();

    @Query("select bet_team from BetTeam bet_team left join fetch bet_team.members where bet_team.id =:id")
    BetTeam findOneWithEagerRelationships(@Param("id") Long id);

}
