package ch.fhnw.wodss.betchampion.service;

import ch.fhnw.wodss.betchampion.domain.BetTeam;
import ch.fhnw.wodss.betchampion.repository.BetTeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing BetTeam.
 */
@Service
@Transactional
public class BetTeamService {

    private final Logger log = LoggerFactory.getLogger(BetTeamService.class);

    private final BetTeamRepository betTeamRepository;

    public BetTeamService(BetTeamRepository betTeamRepository) {
        this.betTeamRepository = betTeamRepository;
    }

    /**
     * Save a betTeam.
     *
     * @param betTeam the entity to save
     * @return the persisted entity
     */
    public BetTeam save(BetTeam betTeam) {
        log.debug("Request to save BetTeam : {}", betTeam);
        return betTeamRepository.save(betTeam);
    }

    /**
     * Get all the betTeams.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BetTeam> findAll(Pageable pageable) {
        log.debug("Request to get all BetTeams");
        return betTeamRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<BetTeam> findAllEager(Pageable pageable) {
        log.debug("Request to get all BetTeams with all it's members");
        return betTeamRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one betTeam by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public BetTeam findOne(Long id) {
        log.debug("Request to get BetTeam : {}", id);
        return betTeamRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the betTeam by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BetTeam : {}", id);
        betTeamRepository.delete(id);
    }
}
