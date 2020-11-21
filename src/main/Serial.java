package main;

import entertainment.Season;

import java.util.ArrayList;

public class Serial extends Show {

    private final int numberOfSeasons;
    private final ArrayList<Season> seasons;
    private double rating;

    public Serial(final String title, final int year, final ArrayList<String> genres,
                  final ArrayList<String> cast, final int numberOfSeasons,
                  final ArrayList<Season> seasons) {

        super(title, year, cast, genres);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
    }

    public final int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public final ArrayList<Season> getSeasons() {
        return seasons;
    }

    public final double getRating() {
        return rating;
    }

    public final void setRating(double rating) {
        this.rating = rating;
    }
}
