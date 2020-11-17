package main;

import java.util.ArrayList;

public class Video {
    private String title;
    private int launchYear;
    private ArrayList<String> genre;

    public Video(String title, int launchYear, ArrayList<String> genre) {
        this.title = title;
        this.launchYear = launchYear;
        this.genre = genre;
    }

}
