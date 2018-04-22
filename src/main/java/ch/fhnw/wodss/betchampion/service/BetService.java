package ch.fhnw.wodss.betchampion.service;

import ch.fhnw.wodss.betchampion.domain.Bet;
import ch.fhnw.wodss.betchampion.domain.Game;
import ch.fhnw.wodss.betchampion.domain.Stats;
import ch.fhnw.wodss.betchampion.repository.BetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Service Implementation for managing Bet.
 */
@Service
@Transactional
public class BetService {

    private final Logger log = LoggerFactory.getLogger(BetService.class);

    private final BetRepository betRepository;

    public BetService(BetRepository betRepository) {
        this.betRepository = betRepository;
    }

    /**
     * Save a bet.
     *
     * @param bet the entity to save
     * @return the persisted entity
     */
    public Bet save(Bet bet) {
        log.debug("Request to save Bet : {}", bet);
        return betRepository.save(bet);
    }

    /**
     * Get all the bets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Bet> findAll(Pageable pageable) {
        log.debug("Request to get all Bets");
        return betRepository.findAll(pageable);
    }

    /**
     * Get one bet by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Bet findOne(Long id) {
        log.debug("Request to get Bet : {}", id);
        return betRepository.findOne(id);
    }

    /**
     * Delete the bet by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Bet : {}", id);
        betRepository.delete(id);
    }

    public Stats getStats(Game game){
        List<Bet> bets = betRepository.findAllByGame(game);
        int t1 = 0, t2 = 0, draw = 0;
        for(Bet b: bets){
            if(b.getGoalsTeam1().equals(b.getGoalsTeam2())){
                draw++;
            }else if(b.getGoalsTeam1() > b.getGoalsTeam2()){
                t1++;
            }else {
                t2++;
            }
        }
        return new Stats(t1+t2+draw,t1,t2,draw);
    }
}
