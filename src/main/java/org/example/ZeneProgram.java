package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ZeneProgram {
    public static void main(String[] args) {
        try {
            // 1. feladat: Adatok beolvasása
            Map<Integer, Integer> channelCountMap = readData("musor.txt");

            // 2. feladat: Csatornánkénti számok kiírása
            System.out.println("2. feladat:");
            for (Map.Entry<Integer, Integer> entry : channelCountMap.entrySet()) {
                System.out.println(entry.getKey() + ". adón " + entry.getValue() + " számot lehetett meghallgatni.");
            }

            // 3. feladat: Idő kiszámolása az első és utolsó Eric Clapton szám között az 1. adón
            int startTime = findStartTime("musor.txt", 1, "Eric Clapton:Terraplane Blues");
            int endTime = findEndTime("musor.txt", 1, "Eric Clapton:Crazy Country Hop");
            int elapsedSeconds = endTime - startTime;
            System.out.println("\n3. feladat:");
            System.out.println("Az első Eric Clapton szám kezdete: " + formatTime(startTime));
            System.out.println("Az utolsó Eric Clapton szám vége: " + formatTime(endTime));
            System.out.println("Eltelt idő: " + formatTime(elapsedSeconds));

            // 4. feladat: Csatornák és számok kiírása Eszter váltásakor
            System.out.println("\n4. feladat:");
            int eszterSwitchTime = findStartTime("musor.txt", 1, "Omega:Legenda");
            System.out.println("A \"Omega:Legenda\" szám az " + eszterSwitchTime + ". másodpercben kezdődött az 1. adón.");
            printOtherChannelsData("musor.txt", eszterSwitchTime, 1);

            // 5. feladat: SMS-ben kérhető számok keresése és kiírása
            System.out.println("\n5. feladat:");
            String requestedChars = "gaoaf";
            findAndWriteRequestedSongs("musor.txt", requestedChars, "keres.txt");

            // 6. feladat: Új műsorszerkezet vége kiírása
            System.out.println("\n6. feladat:");
            int newProgramEndTime = calculateNewProgramEndTime("musor.txt");
            System.out.println("Az új műsorszerkezetben az adás vége: " + formatTime(newProgramEndTime));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 1. feladat: Adatok beolvasása
    private static Map<Integer, Integer> readData(String filename) throws IOException {
        Map<Integer, Integer> channelCountMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            int totalSongs = Integer.parseInt(br.readLine().trim());
            for (int i = 0; i < totalSongs; i++) {
                String[] parts = br.readLine().split(" ");
                int channel = Integer.parseInt(parts[0]);
                channelCountMap.put(channel, channelCountMap.getOrDefault(channel, 0) + 1);
            }
        }
        return channelCountMap;
    }

    // 3. feladat: Kezdeti idő keresése adott számhoz adott csatornán
    private static int findStartTime(String filename, int channel, String songIdentifier) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                int currentChannel = Integer.parseInt(parts[0]);
                if (currentChannel == channel && parts[3].equals(songIdentifier)) {
                    return Integer.parseInt(parts[1]) * 60 + Integer.parseInt(parts[2]);
                }
            }
        }
        return -1; // Ha nem található a szám az adott csatornán
    }

    // 3. feladat: Vége idő keresése adott számhoz adott csatornán
    private static int findEndTime(String filename, int channel, String songIdentifier) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            int endTime = -1; // Alapértelmezett érték, ha nincs találat
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                int currentChannel = Integer.parseInt(parts[0]);
                if (currentChannel == channel && parts[3].equals(songIdentifier)) {
                    endTime = Integer.parseInt(parts[1]) * 60 + Integer.parseInt(parts[2]);
                }
            }
            return endTime;
        }
    }

    // 4. feladat: Másik csatornák és számok kiírása adott időpontban
    private static void printOtherChannelsData(String filename, int time, int currentChannel) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                int channel = Integer.parseInt(parts[0]);
                int songTime = Integer.parseInt(parts[1]) * 60 + Integer.parseInt(parts[2]);
                if (channel != currentChannel && songTime < time) {
                    System.out.println("Az " + channel + ". adón " + parts[3] + " szám szólt.");
                }
            }
        }
    }

    // 5. feladat: SMS-ben kérhető számok keresése és kiírása
    private static void findAndWriteRequestedSongs(String filename, String requestedChars, String outputFilename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename));
             PrintWriter writer = new PrintWriter(new FileWriter(outputFilename))) {

            String line;
            StringBuilder currentSongId = new StringBuilder();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                String songId = parts[3];
                for (char c : songId.toCharArray()) {
                    if (requestedChars.contains(String.valueOf(c))) {
                        currentSongId.append(c);
                    }
                }
                if (currentSongId.length() == requestedChars.length()) {
                    writer.println(requestedChars);
                    writer.println(parts[3]);
                    currentSongId.setLength(0);
                }
            }
        }
    }

    // 6. feladat: Új műsorszerkezet vége kiszámolása
    private static int calculateNewProgramEndTime(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            int currentTime = 0;
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                int songTime = Integer.parseInt(parts[1]) * 60 + Integer.parseInt(parts[2]);
                if (parts[3].contains("hír")) {
                    currentTime += 180; // 3 perces hírek minden egész órakor
                } else if (songTime > currentTime) {
                    currentTime = songTime;
                }
            }
            return currentTime;
        }
    }

    // Segédfüggvény a formázott idő kiírásához
    private static String formatTime(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
    }
}
