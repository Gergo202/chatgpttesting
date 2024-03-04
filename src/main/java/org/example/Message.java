package org.example;

public class Message implements Comparable<Message> {
    private int hour;
    private int minute;
    private String phoneNumber;
    private String text;

    public Message(int hour, int minute, String phoneNumber, String text) {
        this.hour = hour;
        this.minute = minute;
        this.phoneNumber = phoneNumber;
        this.text = text;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getText() {
        return text;
    }

    public int calculateTimeDifference(Message other) {
        return (this.hour - other.hour) * 60 + (this.minute - other.minute);
    }

    public String getMessage() {
        return hour + " " + minute + " " + phoneNumber + "\n" + text;
    }

    public void writeToFile(String filename) {
        try {
            java.io.FileWriter writer = new java.io.FileWriter(filename, true);
            writer.write(getMessage() + "\n");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int compareTo(Message other) {
        return Integer.compare(this.hour * 60 + this.minute, other.hour * 60 + other.minute);
    }

    @Override
    public String toString() {
        return hour + " " + minute + " " + phoneNumber + " " + text;
    }
}
