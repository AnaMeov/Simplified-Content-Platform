package main;

import java.util.ArrayList;

public class Movie extends Video {

    private int movieLength;
    private int rating;

    public Movie(String title, int launchYear, ArrayList<String> genre, int movieLength, int rating) {
        super(title, launchYear, genre);
        this.movieLength = movieLength;
        this.rating = rating;
    }

}
