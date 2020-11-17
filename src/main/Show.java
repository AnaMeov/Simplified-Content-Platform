package main;

import java.util.ArrayList;

public class Show extends Video {

    private int seasonNumber;
    private int seasonLength;
    private int rating;

    public Show(String title, int launchYear, ArrayList<String> genre, int seasonNumber, int seasonLength, int rating) {
        super(title, launchYear, genre);
        this.seasonNumber = seasonNumber;
        this.seasonLength = seasonLength;
        this.rating = rating;
    }

}
