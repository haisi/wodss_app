package ch.fhnw.wodss.betchampion.service;

import ch.fhnw.wodss.betchampion.domain.Bet;
import ch.fhnw.wodss.betchampion.domain.User;
import ch.fhnw.wodss.betchampion.repository.BetRepository;
import ch.fhnw.wodss.betchampion.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PointUpdateService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final BetRepository betRepository;

    public PointUpdateService(UserRepository userRepository, BetRepository betRepository) {
        this.userRepository = userRepository;
        this.betRepository = betRepository;
    }

    @Scheduled(cron = "0 */10 * * * *")
    public void updatePoints(){
        log.debug("Start to calculate points");
        List<User> users = userRepository.findAll();
        users.forEach(this::updateUser);
        log.debug("All points calculated");
        updateRank();
        log.debug("All ranks updated");
    }


    private void updateUser(User user){
        List<Bet> bets = betRepository.findAllByUser(user);
        int points = 0;
        for(Bet b: bets){
            int team1_bet = b.getGoalsTeam1();
            int team2_bet = b.getGoalsTeam2();
            int team1_result = b.getGame().getGoalsTeam1();
            int team2_result = b.getGame().getGoalsTeam2();

            if(team1_bet == team1_result && team2_bet == team2_result){
                points += 3;
            }else if(team1_bet-team2_bet == team1_result - team2_result){
                points += 2;
            }else if((team1_bet > team2_bet && team1_result > team2_result)||(team1_bet < team2_bet && team1_result < team2_result)){
                points++;
            }
        }
        user.setPoints(points);
    }

    private void updateRank(){
        List<User> users = userRepository.findAll();
        users = users.stream().sorted(Comparator.comparingInt(User::getPoints)).collect(Collectors.toList());

        int currentRank = 1;
        int prevPoints = users.get(0).getPoints();

        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            Integer currentPoints = u.getPoints();

            if (currentPoints != prevPoints) {
                currentRank = i + 1;
            }

            u.setRank(currentRank);
            prevPoints = u.getPoints();
        }
    }
}
