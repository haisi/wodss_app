package ch.fhnw.wodss.betchampion.repository;

import ch.fhnw.wodss.betchampion.domain.BetTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Query(value = "select distinct bet_team from BetTeam bet_team left join fetch bet_team.members",
        countQuery = "select count(distinct bet_team) from BetTeam bet_team")
    Page<BetTeam> findAllWithEagerRelationships(Pageable pageable);

    @Query("select bet_team from BetTeam bet_team left join fetch bet_team.members where bet_team.id =:id")
    BetTeam findOneWithEagerRelationships(@Param("id") Long id);

}
