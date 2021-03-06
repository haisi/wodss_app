package ch.fhnw.wodss.betchampion.web.rest;

import ch.fhnw.wodss.betchampion.service.dto.BetDto;
import ch.fhnw.wodss.betchampion.service.dto.UpsertBetDto;
import com.codahale.metrics.annotation.Timed;
import ch.fhnw.wodss.betchampion.domain.Bet;
import ch.fhnw.wodss.betchampion.service.BetService;
import ch.fhnw.wodss.betchampion.web.rest.errors.BadRequestAlertException;
import ch.fhnw.wodss.betchampion.web.rest.util.HeaderUtil;
import ch.fhnw.wodss.betchampion.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Bet.
 */
@RestController
@RequestMapping("/api")
public class BetResource {

    private final Logger log = LoggerFactory.getLogger(BetResource.class);

    private static final String ENTITY_NAME = "bet";

    private final BetService betService;

    public BetResource(BetService betService) {
        this.betService = betService;
    }


    @PostMapping("/upsert")
    public Bet upsert(@Valid @RequestBody UpsertBetDto dto) {
        log.debug("REST request to upsert Bet : {}", dto);
        return betService.upsertBet(dto);
    }

    @GetMapping("/mybets")
    @Timed
    public ResponseEntity<List<BetDto>> getAllBetsAndGamesOfUser() {
        log.debug("REST request to get all bets of current user");
        List<BetDto> dtos = betService.getAllBetsAndGamesOfUser();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * GET  /bets : get all the bets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bets in body
     */
    @GetMapping("/bets")
    @Timed
    public ResponseEntity<List<Bet>> getAllBets(Pageable pageable) {
        log.debug("REST request to get a page of Bets");
        Page<Bet> page = betService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bets/:id : get the "id" bet.
     *
     * @param id the id of the bet to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bet, or with status 404 (Not Found)
     */
    @GetMapping("/bets/{id}")
    @Timed
    public ResponseEntity<Bet> getBet(@PathVariable Long id) {
        log.debug("REST request to get Bet : {}", id);
        Bet bet = betService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bet));
    }

    /**
     * DELETE  /bets/:id : delete the "id" bet.
     *
     * @param id the id of the bet to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bets/{id}")
    @Timed
    public ResponseEntity<Void> deleteBet(@PathVariable Long id) {
        log.debug("REST request to delete Bet : {}", id);
        betService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
