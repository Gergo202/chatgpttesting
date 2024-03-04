package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SMSProgram {
    public static void main(String[] args) {
        try {
            // Feladat 1: Olvassa be az sms.txt állományt
            ArrayList<Message> messages = readMessages("sms.txt");

            // Feladat 2: Legfrissebb üzenet kiírása
            System.out.println("2. feladat: Legfrissebb üzenet:");
            Message latestMessage = messages.get(messages.size() - 1);
            System.out.println(latestMessage.getMessage());

            // Feladat 3: Leghosszabb és legrövidebb üzenetek kiírása
            System.out.println("\n3. feladat: Leghosszabb és legrövidebb üzenetek:");
            Message longestMessage = Collections.max(messages);
            Message shortestMessage = Collections.min(messages);
            System.out.println("Leghosszabb: " + longestMessage);
            System.out.println("Legrövidebb: " + shortestMessage);

            // Feladat 4: Statisztika készítése karakterhossz szerint
            System.out.println("\n4. feladat: Statisztika karakterhossz szerint:");
            Map<String, Integer> lengthStats = calculateLengthStats(messages);
            for (Map.Entry<String, Integer> entry : lengthStats.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue() + " db");
            }

            // Feladat 5: Elolvasott és törölt üzenetek számának kiszámítása
            System.out.println("\n5. feladat: Elolvasott és törölt üzenetek száma:");
            int deletedMessagesCount = calculateDeletedMessages(messages);
            System.out.println("Törölni kellene: " + deletedMessagesCount + " db");

            // Feladat 6: Leghosszabb időkülönbség két üzenet között
            System.out.println("\n6. feladat: Leghosszabb időkülönbség két üzenet között:");
            String timeDifference = calculateMaxTimeDifference(messages, "123456789");
            System.out.println(timeDifference);

            // Feladat 7: Későn érkezett üzenet beolvasása és tárolása
            System.out.println("\n7. feladat: Későn érkezett üzenet beolvasása és tárolása:");
            Message lateMessage = readLateMessage();
            messages.add(lateMessage);

            // Feladat 8: Telefonszám szerinti csoportosítás és kiíratás
            System.out.println("\n8. feladat: Telefonszám szerinti csoportosítás és kiíratás:");
            writeGroupedMessages("smski.txt", messages);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Feladat 1: Sms.txt állomány beolvasása és üzenetek létrehozása
    private static ArrayList<Message> readMessages(String filename) throws FileNotFoundException {
        ArrayList<Message> messages = new ArrayList<>();
        Scanner scanner = new Scanner(new File(filename));

        int numOfMessages = scanner.nextInt();
        scanner.nextLine(); // consume the newline character

        for (int i = 0; i < numOfMessages; i++) {
            int hour = scanner.nextInt();
            int minute = scanner.nextInt();
            String phoneNumber = scanner.next();
            String text = scanner.nextLine().trim();
            messages.add(new Message(hour, minute, phoneNumber, text));
        }

        scanner.close();
        return messages;
    }

    // Feladat 4: Statisztika készítése karakterhossz szerint
    private static Map<String, Integer> calculateLengthStats(ArrayList<Message> messages) {
        Map<String, Integer> lengthStats = new HashMap<>();

        for (Message message : messages) {
            int length = message.getText().length();
            int intervalStart = ((length - 1) / 20) * 20 + 1;
            int intervalEnd = (length / 20) * 20;

            String key = intervalStart + "-" + intervalEnd;
            lengthStats.put(key, lengthStats.getOrDefault(key, 0) + 1);
        }

        return lengthStats;
    }

    // Feladat 5: Elolvasott és törölt üzenetek számának kiszámítása
    private static int calculateDeletedMessages(ArrayList<Message> messages) {
        int readAndDeletedCount = 0;

        for (Message message : messages) {
            if (message.getHour() % 24 == 0 && message.getMinute() == 0) {
                readAndDeletedCount++;
            }
        }

        return readAndDeletedCount;
    }

    // Feladat 6: Leghosszabb időkülönbség két üzenet között
    private static String calculateMaxTimeDifference(ArrayList<Message> messages, String phoneNumber) {
        Collections.sort(messages);

        int maxTimeDifference = -1;
        for (int i = 1; i < messages.size(); i++) {
            if (messages.get(i).getPhoneNumber().equals(phoneNumber)) {
                int timeDifference = messages.get(i).calculateTimeDifference(messages.get(i - 1));
                maxTimeDifference = Math.max(maxTimeDifference, timeDifference);
            }
        }

        if (maxTimeDifference == -1) {
            return "nincs elegendő üzenet";
        } else {
            int hours = maxTimeDifference / 60;
            int minutes = maxTimeDifference % 60;
            return hours + " óra " + minutes + " perc";
        }
    }

    // Feladat 7: Későn érkezett üzenet beolvasása és tárolása
    private static Message readLateMessage() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Kérjük, adja meg a későn érkezett üzenet adatait!");
        System.out.print("Óra: ");
        int hour = scanner.nextInt();
        System.out.print("Perc: ");
        int minute = scanner.nextInt();
        System.out.print("Telefonszám: ");
        String phoneNumber = scanner.next();
        System.out.print("Üzenet: ");
        String text = scanner.nextLine().trim();

        return new Message(hour, minute, phoneNumber, text);
    }

    // Feladat 8: Telefonszám szerinti csoportosítás és kiíratás
    private static void writeGroupedMessages(String filename, ArrayList<Message> messages) {
        try {
            Collections.sort(messages);
            Map<String, ArrayList<Message>> groupedMessages = new HashMap<>();

            for (Message message : messages) {
                groupedMessages.computeIfAbsent(message.getPhoneNumber(), k -> new ArrayList<>()).add(message);
            }

            // Írás a smski.txt állományba
            for (Map.Entry<String, ArrayList<Message>> entry : groupedMessages.entrySet()) {
                Collections.sort(entry.getValue());
                entry.getValue().get(0).writeToFile(filename);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

