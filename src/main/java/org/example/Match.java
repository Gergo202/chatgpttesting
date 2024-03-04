package org.example;
import java.util.Objects;

public class Match {
    private int round;
    private int homeGoalsFullTime;
    private int awayGoalsFullTime;
    private int homeGoalsHalfTime;
    private int awayGoalsHalfTime;
    private String homeTeam;
    private String awayTeam;

    public Match(int round, int homeGoalsFullTime, int awayGoalsFullTime,
                 int homeGoalsHalfTime, int awayGoalsHalfTime,
                 String homeTeam, String awayTeam) {
        this.round = round;
        this.homeGoalsFullTime = homeGoalsFullTime;
        this.awayGoalsFullTime = awayGoalsFullTime;
        this.homeGoalsHalfTime = homeGoalsHalfTime;
        this.awayGoalsHalfTime = awayGoalsHalfTime;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    public int getRound() {
        return round;
    }

    public int getHomeGoalsFullTime() {
        return homeGoalsFullTime;
    }

    public int getAwayGoalsFullTime() {
        return awayGoalsFullTime;
    }

    public int getHomeGoalsHalfTime() {
        return homeGoalsHalfTime;
    }

    public int getAwayGoalsHalfTime() {
        return awayGoalsHalfTime;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public boolean isLosingAtHalfTime() {
        return homeGoalsHalfTime < awayGoalsHalfTime;
    }

    public boolean isWinningAtFullTime() {
        return homeGoalsFullTime > awayGoalsFullTime;
    }

    public boolean isLoss() {
        return homeGoalsFullTime < awayGoalsFullTime;
    }

    public boolean isHomeTeam(String team) {
        return homeTeam.equals(team);
    }

    public boolean isAwayTeam(String team) {
        return awayTeam.equals(team);
    }

    public String getWinner() {
        return (homeGoalsFullTime > awayGoalsFullTime) ? homeTeam : awayTeam;
    }

    public String getFormattedResult() {
        return Math.max(homeGoalsFullTime, awayGoalsFullTime) + "-" + Math.min(homeGoalsFullTime, awayGoalsFullTime);
    }

    @Override
    public String toString() {
        return homeTeam + "-" + awayTeam + ": " + homeGoalsFullTime + "-" + awayGoalsFullTime + " (" + homeGoalsHalfTime + "-" + awayGoalsHalfTime + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Match match = (Match) obj;
        return homeGoalsFullTime == match.homeGoalsFullTime &&
                awayGoalsFullTime == match.awayGoalsFullTime &&
                homeGoalsHalfTime == match.homeGoalsHalfTime &&
                awayGoalsHalfTime == match.awayGoalsHalfTime &&
                Objects.equals(homeTeam, match.homeTeam) &&
                Objects.equals(awayTeam, match.awayTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeTeam, awayTeam, homeGoalsFullTime, awayGoalsFullTime, homeGoalsHalfTime, awayGoalsHalfTime);
    }
}
