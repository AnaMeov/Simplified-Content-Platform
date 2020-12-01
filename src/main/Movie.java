package main;

import common.Constants;
import fileio.ActionInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Map;
import java.util.Collections;

public class Movie extends Show {

    private final int duration;
    private double rating;
    private double sumRating;
    private int noRatings;

    public Movie(final String title, final ArrayList<String> cast,
                 final ArrayList<String> genres, final int year,
                 final int duration) {
        super(title, year, cast, genres);
        this.duration = duration;
        this.sumRating = 0.0;
        this.noRatings = 0;
        this.rating = 0;
    }

    public final int getDuration() {
        return duration;
    }

    public final double getRating() {
        return rating;
    }

    /**
     * Calculate rating for movies
     */
    public final void setRating(final double rating) {
        sumRating = sumRating + rating;
        noRatings++;
        this.rating = sumRating / noRatings;
    }

    /**
     * Compare movies first by an integer (views/nr of favorites) and second by name
     */
    public static Comparator<Movie> getCompByInt(final HashMap<String, Integer> compMovie) {
        return new Comparator<Movie>() {
            @Override
            public int compare(final Movie m1, final Movie m2) {
                if (Integer.compare(compMovie.get(m1.getTitle()),
                        compMovie.get(m2.getTitle())) == 0) {
                    return m1.getTitle().compareTo(m2.getTitle());
                }
                return Integer.compare(compMovie.get(m1.getTitle()),
                        compMovie.get(m2.getTitle()));
            }
        };
    }

    /**
     * Compare movies first by a double (ratings) and second by name
     */
    public static Comparator<Movie> getCompByDouble(final HashMap<String, Double> compMovie) {
        return new Comparator<Movie>() {
            @Override
            public int compare(final Movie m1, final Movie m2) {
                if (Double.compare(compMovie.get(m1.getTitle()),
                        compMovie.get(m2.getTitle())) == 0) {
                    return m1.getTitle().compareTo(m2.getTitle());
                }
                return Double.compare(compMovie.get(m1.getTitle()),
                        compMovie.get(m2.getTitle()));
            }
        };
    }

    /**
     * Function to determine first N movies sorted by views
     */
    public static String mostViewed(final ActionInputData currentCommand,
                                    final ArrayList<Movie> movies,
                                    final ArrayList<User> users) {

        // hashmap containing movies' name and number of views
        HashMap<String, Integer> viewsMovie = new HashMap<>();
        ArrayList<Movie> moviesBuff = new ArrayList<>(movies);
        ArrayList<String> moviesList = new ArrayList<>();

        for (Movie movie : moviesBuff) {
            viewsMovie.put(movie.getTitle(), 0);
        }

        // populate the hashmap
        for (User user : users) {
            for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
                if (viewsMovie.containsKey(entry.getKey())) {
                    viewsMovie.put(entry.getKey(), entry.getValue()
                            + viewsMovie.get(entry.getKey()));
                } else {
                    viewsMovie.put(entry.getKey(), entry.getValue());
                }
            }
        }

        // filter movies by input filters year and genre
        for (int i = moviesBuff.size() - 1; i >= 0; i--) {
            if (currentCommand.getFilters().get(0).get(0) != null) {
                if (!Integer.toString(moviesBuff.get(i).getYear()).
                        equals(currentCommand.getFilters().get(0).get(0))) {
                    moviesBuff.remove(i);
                    continue;
                }
            }
            if (currentCommand.getFilters().get(1).get(0) != null) {
                if (!moviesBuff.get(i).getGenres().
                        contains(currentCommand.getFilters().get(1).get(0))) {
                    moviesBuff.remove(i);
                }
            }
        }

        // sort the movies
        moviesBuff.sort(getCompByInt(viewsMovie));
        if (currentCommand.getSortType().equals(Constants.DSC)) {
            Collections.reverse(moviesBuff);
        }

        // selecting first N movies
        for (int i = 0, j = 0; i < moviesBuff.size() && j < currentCommand.getNumber(); i++) {
            if (viewsMovie.get(moviesBuff.get(i).getTitle()) != 0) {
                j++;
                moviesList.add(moviesBuff.get(i).getTitle());
            }
        }
        return Constants.QUERY_RES + moviesList.toString();
    }

    /**
     * Function to determine first N movies sorted by their length
     */
    public static String longestMovies(final ActionInputData currentCommand,
                                       final ArrayList<Movie> movies) {

        // hashmap containing movies' name and length
        HashMap<String, Integer> longMovie = new HashMap<>();
        ArrayList<Movie> moviesBuff = new ArrayList<>(movies);
        ArrayList<String> moviesList = new ArrayList<>();

        for (Movie movie : moviesBuff) {
            longMovie.put(movie.getTitle(), movie.getDuration());
        }

        // populate the hashmap
        for (int i = moviesBuff.size() - 1; i >= 0; i--) {
            if (currentCommand.getFilters().get(0).get(0) != null) {
                if (!Integer.toString(moviesBuff.get(i).getYear()).
                        equals(currentCommand.getFilters().get(0).get(0))) {
                    moviesBuff.remove(i);
                    continue;
                }
            }
            if (currentCommand.getFilters().get(1).get(0) != null) {
                if (!moviesBuff.get(i).getGenres().
                        contains(currentCommand.getFilters().get(1).get(0))) {
                    moviesBuff.remove(i);
                }
            }
        }

        // sort the movies
        moviesBuff.sort(getCompByInt(longMovie));
        if (currentCommand.getSortType().equals(Constants.DSC)) {
            Collections.reverse(moviesBuff);
        }

        // selecting first N movies
        for (int i = 0, j = 0; i < moviesBuff.size()
                && j < currentCommand.getNumber(); i++) {
            if (longMovie.get(moviesBuff.get(i).getTitle()) != 0) {
                j++;
                moviesList.add(moviesBuff.get(i).getTitle());
            }
        }
        return Constants.QUERY_RES + moviesList.toString();
    }

    /**
     * Function to sort first N movies by the number of appearances in users'
     * favorite video lists
     */
    public static String favoriteMovies(final ActionInputData currentCommand,
                                        final ArrayList<Movie> movies,
                                        final ArrayList<User> users) {

        // hashmap containing movies' name and number of views
        HashMap<String, Integer> favoriteMovies = new HashMap<>();
        ArrayList<Movie> moviesBuff = new ArrayList<>(movies);
        ArrayList<String> moviesList = new ArrayList<>();

        // populate the hashmap
        for (User user : users) {
            for (String videoTitle : user.getFavoriteShows()) {
                for (Movie movie : movies) {
                    if (movie.getTitle().equals(videoTitle)) {
                        if (favoriteMovies.containsKey(videoTitle)) {
                            favoriteMovies.put(videoTitle, favoriteMovies.get(videoTitle) + 1);
                        } else {
                            favoriteMovies.put(videoTitle, 1);
                        }
                    }
                }
            }
        }

        // filter movies by input filters year and genre
        for (int i = moviesBuff.size() - 1; i >= 0; i--) {
            if (currentCommand.getFilters().get(0).get(0) != null) {
                if (!Integer.toString(moviesBuff.get(i).getYear()).
                        equals(currentCommand.getFilters().get(0).get(0))) {
                    moviesBuff.remove(i);
                    continue;
                }
            }
            if (currentCommand.getFilters().get(1).get(0) != null) {
                if (!moviesBuff.get(i).getGenres().
                        contains(currentCommand.getFilters().get(1).get(0))) {
                    moviesBuff.remove(i);
                }
            }
        }

        // sort the movies
        moviesBuff.sort(getCompByInt(favoriteMovies));
        if (currentCommand.getSortType().equals(Constants.DSC)) {
            Collections.reverse(moviesBuff);
        }

        // selecting first N movies
        for (int i = 0, j = 0; i < moviesBuff.size() && j < currentCommand.getNumber(); i++) {
            if (favoriteMovies.get(moviesBuff.get(i).getTitle()) != 0) {
                j++;
                moviesList.add(moviesBuff.get(i).getTitle());
            }
        }
        return Constants.QUERY_RES + moviesList.toString();
    }

    /**
     * Function to sort first N movies after rating
     */
    public static String ratingMovies(final ActionInputData currentCommand,
                                      final ArrayList<Movie> movies) {

        // hashmap that contains movies' name and rating
        HashMap<String, Double> ratingMovie = new HashMap<>();
        ArrayList<Movie> moviesBuff = new ArrayList<>(movies);
        ArrayList<String> moviesList = new ArrayList<>();

        // populate the hashmap
        for (Movie movie : moviesBuff) {
            ratingMovie.put(movie.getTitle(), movie.getRating());
        }

        // filter movies by input filters year and genre
        for (int i = moviesBuff.size() - 1; i >= 0; i--) {
            if (currentCommand.getFilters().get(0).get(0) != null) {
                if (!Integer.toString(moviesBuff.get(i).getYear()).
                        equals(currentCommand.getFilters().get(0).get(0))) {
                    moviesBuff.remove(i);
                    continue;
                }
            }
            if (currentCommand.getFilters().get(1).get(0) != null) {
                if (!moviesBuff.get(i).getGenres().
                        contains(currentCommand.getFilters().get(1).get(0))) {
                    moviesBuff.remove(i);
                }
            }
        }

        // sort the hashmap
        moviesBuff.sort(getCompByDouble(ratingMovie));
        if (currentCommand.getSortType().equals(Constants.DSC)) {
            Collections.reverse(moviesBuff);
        }

        // selecting first N movies
        for (int i = 0, j = 0; i < moviesBuff.size()
                && j < currentCommand.getNumber(); i++) {
            if (ratingMovie.get(moviesBuff.get(i).getTitle()) != 0) {
                j++;
                moviesList.add(moviesBuff.get(i).getTitle());
            }
        }
        return Constants.QUERY_RES + moviesList.toString();
    }

}
