package ch.fhnw.wodss.betchampion.service;

import ch.fhnw.wodss.betchampion.domain.Bet;
import ch.fhnw.wodss.betchampion.domain.Game;
import ch.fhnw.wodss.betchampion.domain.Stats;
import ch.fhnw.wodss.betchampion.domain.User;
import ch.fhnw.wodss.betchampion.repository.BetRepository;
import ch.fhnw.wodss.betchampion.repository.GameRepository;
import ch.fhnw.wodss.betchampion.repository.UserRepository;
import ch.fhnw.wodss.betchampion.security.SecurityUtils;
import ch.fhnw.wodss.betchampion.service.dto.BetDto;
import ch.fhnw.wodss.betchampion.service.dto.UpsertBetDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;


/**
 * Service Implementation for managing Bet.
 */
@Service
@Transactional
public class BetService {

    private final Logger log = LoggerFactory.getLogger(BetService.class);

    private final BetRepository betRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final JdbcTemplate jdbcTemplate;

    public BetService(BetRepository betRepository, UserRepository userRepository, GameRepository gameRepository, JdbcTemplate jdbcTemplate) {
        this.betRepository = betRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    private final String BET_DTO_SQL = "SELECT\n" +
        "  g.id AS game_id,\n" +
        "  g.match_time,\n" +
        "  g.match_time < NOW() AS closed,\n" +
        "  g.goals_team_1, g.goals_team_2,\n" +
        "  g.team1_id, g.team2_id,\n" +
        "  t1.team_name AS team1_name, t2.team_name AS team2_name,\n" +
        "  b.id AS bet_id,\n" +
        "  b.goals_team_1 AS bet_goal_team_1, b.goals_team_2 AS bet_goal_team_2,\n" +
        "  b.user_id AS user_id\n" +
        "FROM game AS g\n" +
        "  LEFT JOIN bet b ON g.id = b.game_id\n" +
        "  INNER JOIN team t1 ON g.team1_id = t1.id\n" +
        "  INNER JOIN team t2 ON g.team2_id = t2.id\n" +
        "HAVING b.user_id IS NULL OR b.user_id = ?\n" +
        "ORDER BY g.match_time ASC";

    private final String GAME_CLOSED_SQL = "SELECT g.match_time < NOW() as closed FROM game as g WHERE g.id = ?;";

    public List<BetDto> getAllBetsAndGamesOfUser() {

        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
        if (!currentUser.isPresent()) {
            throw new UsernameNotFoundException("Have to be logged in!");
        }

        User user = currentUser.get();

        return jdbcTemplate.query(BET_DTO_SQL, new Object[] {user.getId()}, new BetDtoMapper());
    }

    public Bet upsertBet(UpsertBetDto dto) {
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
        if (!currentUser.isPresent()) {
            throw new UsernameNotFoundException("Have to be logged in!");
        }

        User user = currentUser.get();

        Game game = gameRepository.getOne(dto.getGameId());
        if (game == null) {
            throw new RuntimeException("Game doesn't exist!");
        }

        Boolean closed = jdbcTemplate.queryForObject(GAME_CLOSED_SQL, new Object[]{dto.getGameId()}, Boolean.class);
        if (closed == null || closed) {
            throw new RuntimeException("Game already closed!");
        }

        Optional<Bet> optionalBet = betRepository.findByGameIdAndUser(dto.getGameId(), user);
        Bet bet;
        if (optionalBet.isPresent()) {
            bet = optionalBet.get();
        } else {
            bet = new Bet();
            bet.setUser(user);
            bet.setGame(game);
        }

        bet.setGoalsTeam1(dto.getBetGoalTeam1() != null ? dto.getBetGoalTeam1() : 0);
        bet.setGoalsTeam2(dto.getBetGoalTeam2() != null ? dto.getBetGoalTeam2() : 0);
        return betRepository.save(bet);
    }

    public class BetDtoMapper implements RowMapper<BetDto> {

        @Override
        public BetDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            BetDto betDto = new BetDto();
            betDto.setGameId(rs.getLong("game_id"));
            ZonedDateTime matchTime = rs.getTimestamp("match_time").toLocalDateTime().atZone(ZoneOffset.UTC);
            betDto.setMatchTime(matchTime);
            betDto.setGameClosed(rs.getBoolean("closed"));

            betDto.setGoalsTeam1(rs.getInt("goals_team_1"));
            if (rs.wasNull()) betDto.setGoalsTeam1(null);
            betDto.setGoalsTeam2(rs.getInt("goals_team_2"));
            if (rs.wasNull()) betDto.setGoalsTeam2(null);

            betDto.setTeam1Id(rs.getLong("team1_id"));
            betDto.setTeam1Name(rs.getString("team1_name"));
            betDto.setTeam2Id(rs.getLong("team2_id"));
            betDto.setTeam2Name(rs.getString("team2_name"));

            betDto.setBetId(rs.getLong("bet_id"));
            if (rs.wasNull()) betDto.setBetId(null);
            betDto.setUserId(rs.getLong("user_id"));
            if (rs.wasNull()) betDto.setUserId(null);

            betDto.setBetGoalTeam1(rs.getInt("bet_goal_team_1"));
            if (rs.wasNull()) betDto.setBetGoalTeam1(null);
            betDto.setBetGoalTeam2(rs.getInt("bet_goal_team_2"));
            if (rs.wasNull()) betDto.setBetGoalTeam2(null);

            Stats stats = getStats(betDto.getGameId());
            betDto.setStats(stats);

            return betDto;
        }
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

    public Stats getStats(Long gameId){
        List<Bet> bets = betRepository.findAllByGameId(gameId);
        return calculateStats(bets);
    }

    public Stats getStats(Game game){
        List<Bet> bets = betRepository.findAllByGame(game);
        return calculateStats(bets);
    }

    private Stats calculateStats(List<Bet> bets) {
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
        int total = t1+t2+draw;
        if(total != 0){
            t1 = (t1 * 100)/total;
            t2 = (t2 * 100)/total;
            draw = (draw * 100)/total;
        }
        return new Stats(total,t1,t2,draw);
    }
}
