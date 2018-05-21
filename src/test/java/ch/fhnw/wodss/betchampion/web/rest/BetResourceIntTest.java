package ch.fhnw.wodss.betchampion.web.rest;

import ch.fhnw.wodss.betchampion.BetChampionApp;

import ch.fhnw.wodss.betchampion.domain.Bet;
import ch.fhnw.wodss.betchampion.repository.BetRepository;
import ch.fhnw.wodss.betchampion.service.BetService;
import ch.fhnw.wodss.betchampion.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static ch.fhnw.wodss.betchampion.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BetResource REST controller.
 *
 * @see BetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BetChampionApp.class)
public class BetResourceIntTest {

    private static final Integer DEFAULT_GOALS_TEAM_1 = 0;
    private static final Integer UPDATED_GOALS_TEAM_1 = 1;

    private static final Integer DEFAULT_GOALS_TEAM_2 = 0;
    private static final Integer UPDATED_GOALS_TEAM_2 = 1;

    @Autowired
    private BetRepository betRepository;

    @Autowired
    private BetService betService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBetMockMvc;

    private Bet bet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BetResource betResource = new BetResource(betService);
        this.restBetMockMvc = MockMvcBuilders.standaloneSetup(betResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bet createEntity(EntityManager em) {
        Bet bet = new Bet()
            .goalsTeam1(DEFAULT_GOALS_TEAM_1)
            .goalsTeam2(DEFAULT_GOALS_TEAM_2);
        return bet;
    }

    @Before
    public void initTest() {
        bet = createEntity(em);
    }

    @Test
    @Transactional
    public void createBet() throws Exception {
        int databaseSizeBeforeCreate = betRepository.findAll().size();

        // Create the Bet
        restBetMockMvc.perform(post("/api/bets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bet)))
            .andExpect(status().isCreated());

        // Validate the Bet in the database
        List<Bet> betList = betRepository.findAll();
        assertThat(betList).hasSize(databaseSizeBeforeCreate + 1);
        Bet testBet = betList.get(betList.size() - 1);
        assertThat(testBet.getGoalsTeam1()).isEqualTo(DEFAULT_GOALS_TEAM_1);
        assertThat(testBet.getGoalsTeam2()).isEqualTo(DEFAULT_GOALS_TEAM_2);
    }

    @Test
    @Transactional
    public void createBetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = betRepository.findAll().size();

        // Create the Bet with an existing ID
        bet.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBetMockMvc.perform(post("/api/bets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bet)))
            .andExpect(status().isBadRequest());

        // Validate the Bet in the database
        List<Bet> betList = betRepository.findAll();
        assertThat(betList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkGoalsTeam1IsRequired() throws Exception {
        int databaseSizeBeforeTest = betRepository.findAll().size();
        // set the field null
        bet.setGoalsTeam1(null);

        // Create the Bet, which fails.

        restBetMockMvc.perform(post("/api/bets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bet)))
            .andExpect(status().isBadRequest());

        List<Bet> betList = betRepository.findAll();
        assertThat(betList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGoalsTeam2IsRequired() throws Exception {
        int databaseSizeBeforeTest = betRepository.findAll().size();
        // set the field null
        bet.setGoalsTeam2(null);

        // Create the Bet, which fails.

        restBetMockMvc.perform(post("/api/bets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bet)))
            .andExpect(status().isBadRequest());

        List<Bet> betList = betRepository.findAll();
        assertThat(betList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBets() throws Exception {
        // Initialize the database
        betRepository.saveAndFlush(bet);

        // Get all the betList
        restBetMockMvc.perform(get("/api/bets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bet.getId().intValue())))
            .andExpect(jsonPath("$.[*].goalsTeam1").value(hasItem(DEFAULT_GOALS_TEAM_1)))
            .andExpect(jsonPath("$.[*].goalsTeam2").value(hasItem(DEFAULT_GOALS_TEAM_2)));
    }

    @Test
    @Transactional
    public void getBet() throws Exception {
        // Initialize the database
        betRepository.saveAndFlush(bet);

        // Get the bet
        restBetMockMvc.perform(get("/api/bets/{id}", bet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bet.getId().intValue()))
            .andExpect(jsonPath("$.goalsTeam1").value(DEFAULT_GOALS_TEAM_1))
            .andExpect(jsonPath("$.goalsTeam2").value(DEFAULT_GOALS_TEAM_2));
    }

    @Test
    @Transactional
    public void getNonExistingBet() throws Exception {
        // Get the bet
        restBetMockMvc.perform(get("/api/bets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBet() throws Exception {
        // Initialize the database
        betService.save(bet);

        int databaseSizeBeforeUpdate = betRepository.findAll().size();

        // Update the bet
        Bet updatedBet = betRepository.findOne(bet.getId());
        // Disconnect from session so that the updates on updatedBet are not directly saved in db
        em.detach(updatedBet);
        updatedBet
            .goalsTeam1(UPDATED_GOALS_TEAM_1)
            .goalsTeam2(UPDATED_GOALS_TEAM_2);

        restBetMockMvc.perform(put("/api/bets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBet)))
            .andExpect(status().isOk());

        // Validate the Bet in the database
        List<Bet> betList = betRepository.findAll();
        assertThat(betList).hasSize(databaseSizeBeforeUpdate);
        Bet testBet = betList.get(betList.size() - 1);
        assertThat(testBet.getGoalsTeam1()).isEqualTo(UPDATED_GOALS_TEAM_1);
        assertThat(testBet.getGoalsTeam2()).isEqualTo(UPDATED_GOALS_TEAM_2);
    }

    @Test
    @Transactional
    public void updateNonExistingBet() throws Exception {
        int databaseSizeBeforeUpdate = betRepository.findAll().size();

        // Create the Bet

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBetMockMvc.perform(put("/api/bets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bet)))
            .andExpect(status().isCreated());

        // Validate the Bet in the database
        List<Bet> betList = betRepository.findAll();
        assertThat(betList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBet() throws Exception {
        // Initialize the database
        betService.save(bet);

        int databaseSizeBeforeDelete = betRepository.findAll().size();

        // Get the bet
        restBetMockMvc.perform(delete("/api/bets/{id}", bet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Bet> betList = betRepository.findAll();
        assertThat(betList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bet.class);
        Bet bet1 = new Bet();
        bet1.setId(1L);
        Bet bet2 = new Bet();
        bet2.setId(bet1.getId());
        assertThat(bet1).isEqualTo(bet2);
        bet2.setId(2L);
        assertThat(bet1).isNotEqualTo(bet2);
        bet1.setId(null);
        assertThat(bet1).isNotEqualTo(bet2);
    }
}
