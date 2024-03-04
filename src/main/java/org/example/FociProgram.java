package org.example;

import java.io.*;
import java.util.*;

public class FociProgram {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(new File("src/main/resources/meccs.txt"));
            int numOfMatches = scanner.nextInt();
            List<Match> matches = new ArrayList<>();

            for (int i = 0; i < numOfMatches; i++) {
                if (scanner.hasNextInt()) {
                    int round = scanner.nextInt();
                    int homeGoalsFullTime = scanner.nextInt();
                    int awayGoalsFullTime = scanner.nextInt();
                    int homeGoalsHalfTime = scanner.nextInt();
                    int awayGoalsHalfTime = scanner.nextInt();
                    String homeTeam = scanner.next();
                    String awayTeam = scanner.next();

                    Match match = new Match(round, homeGoalsFullTime, awayGoalsFullTime,
                            homeGoalsHalfTime, awayGoalsHalfTime, homeTeam, awayTeam);

                    matches.add(match);
                } else {
                    System.err.println("Hiba: Nem sikerült beolvasni az összes szükséges adatot.");
                    break;  // Kilépés a ciklusból, hogy ne próbáljon további értékeket olvasni
                }
            }

            // Feladatok megoldása

            // 2. feladat
            System.out.println("2. feladat:");
            Scanner inputScanner = new Scanner(System.in);
            System.out.print("Kérem a forduló sorszámát: ");
            int requestedRound = inputScanner.nextInt();
            for (Match match : matches) {
                if (match.getRound() == requestedRound) {
                    System.out.println(match.toString());
                }
            }

            // 3. feladat
            System.out.println("3. feladat:");
            for (int i = 1; i < matches.size(); i++) {
                Match previousMatch = matches.get(i - 1);
                Match currentMatch = matches.get(i);

                if (previousMatch.isLosingAtHalfTime() && currentMatch.isWinningAtFullTime()) {
                    System.out.println(currentMatch.getRound() + " " + currentMatch.getWinner());
                }
            }

            // 4. feladat
            System.out.print("4. feladat: Kérem a csapat nevét: ");
            String chosenTeam = inputScanner.next();

            // 5. feladat
            int goalsScored = 0;
            int goalsConceded = 0;
            for (Match match : matches) {
                if (match.isHomeTeam(chosenTeam)) {
                    goalsScored += match.getHomeGoalsFullTime();
                    goalsConceded += match.getAwayGoalsFullTime();
                } else if (match.isAwayTeam(chosenTeam)) {
                    goalsScored += match.getAwayGoalsFullTime();
                    goalsConceded += match.getHomeGoalsFullTime();
                }
            }
            System.out.println("lőtt: " + goalsScored + " kapott: " + goalsConceded);

            // 6. feladat
            System.out.println("6. feladat:");
            boolean teamNotFound = true;
            for (Match match : matches) {
                if (match.isHomeTeam(chosenTeam) && match.isLoss()) {
                    System.out.println(chosenTeam + " otthon " + match.getRound() + ". fordulóban kapott ki a(z) " + match.getAwayTeam() + " csapattól.");
                    teamNotFound = false;
                    break;
                }
            }
            if (teamNotFound) {
                System.out.println(chosenTeam + " otthon veretlen maradt.");
            }

            // 7. feladat
            System.out.println("7. feladat:");
            Map<String, Integer> resultOccurrences = new HashMap<>();
            for (Match match : matches) {
                String result = match.getFormattedResult();
                resultOccurrences.put(result, resultOccurrences.getOrDefault(result, 0) + 1);
            }

            try (PrintWriter writer = new PrintWriter("stat.txt")) {
                resultOccurrences.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .forEach(entry -> writer.println(entry.getKey() + ": " + entry.getValue() + " darab"));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

