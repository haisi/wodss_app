package ch.fhnw.wodss.betchampion.web.rest;

import ch.fhnw.wodss.betchampion.domain.Team;
import ch.fhnw.wodss.betchampion.domain.User;
import ch.fhnw.wodss.betchampion.repository.UserRepository;
import ch.fhnw.wodss.betchampion.security.SecurityUtils;
import com.codahale.metrics.annotation.Timed;
import ch.fhnw.wodss.betchampion.domain.BetTeam;
import ch.fhnw.wodss.betchampion.service.BetTeamService;
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
 * REST controller for managing BetTeam.
 */
@RestController
@RequestMapping("/api")
public class BetTeamResource {

    private final Logger log = LoggerFactory.getLogger(BetTeamResource.class);

    private static final String ENTITY_NAME = "betTeam";

    private final BetTeamService betTeamService;

    private final UserRepository userRepository;

    public BetTeamResource(BetTeamService betTeamService, UserRepository userRepository) {
        this.betTeamService = betTeamService;
        this.userRepository = userRepository;
    }

    /**
     * POST  /bet-teams : Create a new betTeam.
     *
     * @param betTeam the betTeam to create
     * @return the ResponseEntity with status 201 (Created) and with body the new betTeam, or with status 400 (Bad Request) if the betTeam has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bet-teams")
    @Timed
    public ResponseEntity<BetTeam> createBetTeam(@Valid @RequestBody BetTeam betTeam) throws URISyntaxException {
        log.debug("REST request to save BetTeam : {}", betTeam);
        if (betTeam.getId() != null) {
            throw new BadRequestAlertException("A new betTeam cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BetTeam result = betTeamService.save(betTeam);
        return ResponseEntity.created(new URI("/api/bet-teams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bet-teams : Updates an existing betTeam.
     *
     * @param betTeam the betTeam to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated betTeam,
     * or with status 400 (Bad Request) if the betTeam is not valid,
     * or with status 500 (Internal Server Error) if the betTeam couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bet-teams")
    @Timed
    public ResponseEntity<BetTeam> updateBetTeam(@Valid @RequestBody BetTeam betTeam) throws URISyntaxException {
        log.debug("REST request to update BetTeam : {}", betTeam);
        if (betTeam.getId() == null) {
            return createBetTeam(betTeam);
        }
        BetTeam result = betTeamService.save(betTeam);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, betTeam.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bet-teams : get all the betTeams.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of betTeams in body
     */
    @GetMapping("/bet-teams")
    @Timed
    public ResponseEntity<List<BetTeam>> getAllBetTeams(Pageable pageable) {
        log.debug("REST request to get a page of BetTeams");
        Page<BetTeam> page = betTeamService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bet-teams");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bet-teams/:id : get the "id" betTeam.
     *
     * @param id the id of the betTeam to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the betTeam, or with status 404 (Not Found)
     */
    @GetMapping("/bet-teams/{id}")
    @Timed
    public ResponseEntity<BetTeam> getBetTeam(@PathVariable Long id) {
        log.debug("REST request to get BetTeam : {}", id);
        BetTeam betTeam = betTeamService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(betTeam));
    }

    /**
     * DELETE  /bet-teams/:id : delete the "id" betTeam.
     *
     * @param id the id of the betTeam to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bet-teams/{id}")
    @Timed
    public ResponseEntity<Void> deleteBetTeam(@PathVariable Long id) {
        log.debug("REST request to delete BetTeam : {}", id);
        betTeamService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    /**
     * PUT /bet-teams/:id/join : logged in user joins the "id" betTeam
     *
     * @param id the id of the betTeam to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @PutMapping("bet-teams/{id}/join")
    @Timed
    public ResponseEntity<Void> joinTeam(@PathVariable Long id){
        log.debug("Add :"+SecurityUtils.getCurrentUserLogin().get()+" to bet team "+id);
        Optional<User> optionalUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            BetTeam team =  betTeamService.findOne(id);
            team.addMembers(user);
            betTeamService.save(team);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * PUT /bet-teams/:id/leave : logged in user leaves the "id" betTeam
     *
     * @param id the id of the betTeam to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @PutMapping("bet-teams/{id}/leave")
    @Timed
    public ResponseEntity<Void> leaveTeam(@PathVariable Long id){
        log.debug("Remove :"+SecurityUtils.getCurrentUserLogin().get()+" from bet team "+id);
        Optional<User> optionalUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            BetTeam team =  betTeamService.findOne(id);
            team.removeMembers(user);
            betTeamService.save(team);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
