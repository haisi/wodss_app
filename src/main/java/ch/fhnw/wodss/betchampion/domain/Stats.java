package ch.fhnw.wodss.betchampion.domain;

public class Stats {
    public int total;
    public int winTeam1;
    public int winTeam2;
    public int draw;

    public Stats(int total, int winTeam1, int winTeam2, int draw) {
        this.total = total;
        this.winTeam1 = winTeam1;
        this.winTeam2 = winTeam2;
        this.draw = draw;
    }
}
