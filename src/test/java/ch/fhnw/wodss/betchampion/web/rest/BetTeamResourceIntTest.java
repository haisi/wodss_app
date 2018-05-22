package ch.fhnw.wodss.betchampion.web.rest;

import ch.fhnw.wodss.betchampion.BetChampionApp;

import ch.fhnw.wodss.betchampion.domain.BetTeam;
import ch.fhnw.wodss.betchampion.repository.BetTeamRepository;
import ch.fhnw.wodss.betchampion.repository.UserRepository;
import ch.fhnw.wodss.betchampion.service.BetTeamService;
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
 * Test class for the BetTeamResource REST controller.
 *
 * @see BetTeamResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BetChampionApp.class)
public class BetTeamResourceIntTest {

    private static final String DEFAULT_BET_TEAM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BET_TEAM_NAME = "BBBBBBBBBB";

    @Autowired
    private BetTeamRepository betTeamRepository;

    @Autowired
    private BetTeamService betTeamService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBetTeamMockMvc;

    private BetTeam betTeam;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BetTeamResource betTeamResource = new BetTeamResource(betTeamService, userRepository);
        this.restBetTeamMockMvc = MockMvcBuilders.standaloneSetup(betTeamResource)
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
    public static BetTeam createEntity(EntityManager em) {
        BetTeam betTeam = new BetTeam()
            .betTeamName(DEFAULT_BET_TEAM_NAME);
        return betTeam;
    }

    @Before
    public void initTest() {
        betTeam = createEntity(em);
    }

    @Test
    @Transactional
    public void createBetTeam() throws Exception {
        int databaseSizeBeforeCreate = betTeamRepository.findAll().size();

        // Create the BetTeam
        restBetTeamMockMvc.perform(post("/api/bet-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(betTeam)))
            .andExpect(status().isCreated());

        // Validate the BetTeam in the database
        List<BetTeam> betTeamList = betTeamRepository.findAll();
        assertThat(betTeamList).hasSize(databaseSizeBeforeCreate + 1);
        BetTeam testBetTeam = betTeamList.get(betTeamList.size() - 1);
        assertThat(testBetTeam.getBetTeamName()).isEqualTo(DEFAULT_BET_TEAM_NAME);
    }

    @Test
    @Transactional
    public void createBetTeamWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = betTeamRepository.findAll().size();

        // Create the BetTeam with an existing ID
        betTeam.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBetTeamMockMvc.perform(post("/api/bet-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(betTeam)))
            .andExpect(status().isBadRequest());

        // Validate the BetTeam in the database
        List<BetTeam> betTeamList = betTeamRepository.findAll();
        assertThat(betTeamList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkBetTeamNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = betTeamRepository.findAll().size();
        // set the field null
        betTeam.setBetTeamName(null);

        // Create the BetTeam, which fails.

        restBetTeamMockMvc.perform(post("/api/bet-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(betTeam)))
            .andExpect(status().isBadRequest());

        List<BetTeam> betTeamList = betTeamRepository.findAll();
        assertThat(betTeamList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBetTeams() throws Exception {
        // Initialize the database
        betTeamRepository.saveAndFlush(betTeam);

        // Get all the betTeamList
        restBetTeamMockMvc.perform(get("/api/bet-teams?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(betTeam.getId().intValue())))
            .andExpect(jsonPath("$.[*].betTeamName").value(hasItem(DEFAULT_BET_TEAM_NAME.toString())));
    }

    @Test
    @Transactional
    public void getBetTeam() throws Exception {
        // Initialize the database
        betTeamRepository.saveAndFlush(betTeam);

        // Get the betTeam
        restBetTeamMockMvc.perform(get("/api/bet-teams/{id}", betTeam.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(betTeam.getId().intValue()))
            .andExpect(jsonPath("$.betTeamName").value(DEFAULT_BET_TEAM_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBetTeam() throws Exception {
        // Get the betTeam
        restBetTeamMockMvc.perform(get("/api/bet-teams/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBetTeam() throws Exception {
        // Initialize the database
        betTeamService.save(betTeam);

        int databaseSizeBeforeUpdate = betTeamRepository.findAll().size();

        // Update the betTeam
        BetTeam updatedBetTeam = betTeamRepository.findOne(betTeam.getId());
        // Disconnect from session so that the updates on updatedBetTeam are not directly saved in db
        em.detach(updatedBetTeam);
        updatedBetTeam
            .betTeamName(UPDATED_BET_TEAM_NAME);

        restBetTeamMockMvc.perform(put("/api/bet-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBetTeam)))
            .andExpect(status().isOk());

        // Validate the BetTeam in the database
        List<BetTeam> betTeamList = betTeamRepository.findAll();
        assertThat(betTeamList).hasSize(databaseSizeBeforeUpdate);
        BetTeam testBetTeam = betTeamList.get(betTeamList.size() - 1);
        assertThat(testBetTeam.getBetTeamName()).isEqualTo(UPDATED_BET_TEAM_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingBetTeam() throws Exception {
        int databaseSizeBeforeUpdate = betTeamRepository.findAll().size();

        // Create the BetTeam

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBetTeamMockMvc.perform(put("/api/bet-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(betTeam)))
            .andExpect(status().isCreated());

        // Validate the BetTeam in the database
        List<BetTeam> betTeamList = betTeamRepository.findAll();
        assertThat(betTeamList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBetTeam() throws Exception {
        // Initialize the database
        betTeamService.save(betTeam);

        int databaseSizeBeforeDelete = betTeamRepository.findAll().size();

        // Get the betTeam
        restBetTeamMockMvc.perform(delete("/api/bet-teams/{id}", betTeam.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BetTeam> betTeamList = betTeamRepository.findAll();
        assertThat(betTeamList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BetTeam.class);
        BetTeam betTeam1 = new BetTeam();
        betTeam1.setId(1L);
        BetTeam betTeam2 = new BetTeam();
        betTeam2.setId(betTeam1.getId());
        assertThat(betTeam1).isEqualTo(betTeam2);
        betTeam2.setId(2L);
        assertThat(betTeam1).isNotEqualTo(betTeam2);
        betTeam1.setId(null);
        assertThat(betTeam1).isNotEqualTo(betTeam2);
    }
}
