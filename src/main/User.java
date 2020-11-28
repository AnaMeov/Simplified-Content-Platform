package main;

import common.Constants;
import fileio.ActionInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Map;
import java.util.Collections;

public class User {
    private final String username;
    private final String subscriptionType;
    private final Map<String, Integer> history;
    private final ArrayList<String> favoriteShows;
    private int numberRatings = 0;
    //TODO:
    private final Map<String, Double> ratingMovie = new HashMap<>();
    private final Map<String, ArrayList<Integer>> ratingSerial = new HashMap<>();


    public User(final String username, final String subscriptionType,
                final Map<String, Integer> history,
                final ArrayList<String> favoriteShows) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.history = history;
        this.favoriteShows = favoriteShows;
    }

    public final String getUsername() {
        return username;
    }

    public final String getSubscriptionType() {
        return subscriptionType;
    }

    public final Map<String, Integer> getHistory() {
        return history;
    }

    public final ArrayList<String> getFavoriteMovies() {
        return favoriteShows;
    }

    public final int getNumberRatings() {
        return numberRatings;
    }

    @Override
    public final String toString() {
        return username;
    }

    public static Comparator<Movie> getCompByDoubleMovie(final HashMap<String, Double> compMovie, ArrayList<Movie> movies) {
        return new Comparator<Movie>() {
            @Override
            public int compare(final Movie m1, final Movie m2) {
                if(Double.compare(compMovie.get(m2.getTitle()),
                        compMovie.get(m1.getTitle())) == 0) {
                    return movies.indexOf(m1) - movies.indexOf(m2);
                }
                return Double.compare(compMovie.get(m2.getTitle()),
                        compMovie.get(m1.getTitle()));
            }
        };
    }

    public static Comparator<Serial> getCompByDoubleSerial(final HashMap<String, Double> compSerial, ArrayList<Serial> serials) {
        return new Comparator<Serial>() {
            @Override
            public int compare(final Serial s1, final Serial s2) {
                if (Double.compare(compSerial.get(s2.getTitle()),
                        compSerial.get(s1.getTitle())) == 0) {
                    return serials.indexOf(s1) - serials.indexOf(s2);
                }
                return Double.compare(compSerial.get(s2.getTitle()),
                        compSerial.get(s1.getTitle()));
            }
        };
    }


    /**
     * @return
     */
    public static Comparator<User> getcompByUser() {
        return new Comparator<User>() {
            @Override
            public int compare(final User u1, final User u2) {
                return Double.compare(u1.getNumberRatings(), u2.getNumberRatings());
            }
        };
    }

    /**
     * functia de video-uri favorite
     *
     * @param videoTitle
     * @return
     */
    public final String addFavorite(final String videoTitle) {
        if (videoTitle == null) {
            return null;
        } else {
            if (history.containsKey(videoTitle)) {
                if (!favoriteShows.contains(videoTitle)) {
                    favoriteShows.add(videoTitle);
                    return Constants.SUCCESS + videoTitle + Constants.FAVORITE_SUCCESS;
                } else {
                    return Constants.ERROR + videoTitle + Constants.FAVORITE_ERROR1;
                }
            } else {
                return Constants.ERROR + videoTitle + Constants.NOT_SEEN;
            }
        }
    }

    /**
     * functia de video-uri vazute
     *
     * @param videoTitle
     * @return
     */
    public final String addView(final String videoTitle) {
        if (videoTitle == null) {
            return null;
        } else {
            if (history.containsKey(videoTitle)) {
                history.replace(videoTitle, history.get(videoTitle) + 1);
                return Constants.SUCCESS + videoTitle + Constants.VIEW_SUCCESS
                        + history.get(videoTitle);
            } else {
                history.put(videoTitle, Constants.VIEW_CONST);
                return Constants.SUCCESS + videoTitle + Constants.VIEW_SUCCESS
                        + Constants.VIEW_CONST;
            }
        }
    }

    /**
     * @param movies
     * @param serials
     * @param videoTitle
     * @param rating
     * @param season
     * @return
     */
    public final String addRating(final ArrayList<Movie> movies, final ArrayList<Serial> serials,
                                  final String videoTitle, final double rating,
                                  final int season) {
        if (videoTitle == null) {
            return null;
        } else {
            if (history.containsKey(videoTitle)) {
                for (Movie movie : movies) {
                    if (movie.getTitle().equals(videoTitle)) {
                        if (ratingMovie.get(movie.getTitle()) == null) {
                            ratingMovie.put(movie.getTitle(), movie.getRating());
                            movie.setRating(rating);
                            numberRatings++;
                            return Constants.SUCCESS + videoTitle + Constants.RATED
                                    + rating + Constants.BY + username;
                        } else {
                            return Constants.ERROR + videoTitle + Constants.AL_RATED;
                        }
                    }
                }
                for (Serial serial : serials) {
                    if (serial.getTitle().equals(videoTitle)) {
                        ratingSerial.computeIfAbsent(videoTitle, k -> new ArrayList<>());
                        if (ratingSerial.get(videoTitle).contains(season)) {
                            return Constants.ERROR + videoTitle + Constants.AL_RATED;
                        } else {
                            ArrayList<Integer> seasonBuff = ratingSerial.get(videoTitle);
                            seasonBuff.add(season);
                            ratingSerial.put(videoTitle, seasonBuff);
                            serial.getSeasons().get(season - 1).getRatings().add(rating);
                            numberRatings++;
                            return Constants.SUCCESS + videoTitle + Constants.RATED + rating
                                    + Constants.BY + username;
                        }
                    }
                }
            } else {
                return Constants.ERROR + videoTitle + Constants.NOT_SEEN;
            }
        }
        return Constants.ERROR + videoTitle + Constants.NOT_SEEN;
    }

    /**
     * @param sortType
     * @param number
     * @param users
     * @return
     */
    public static String ratingsNumber(final String sortType, final int number,
                                       final ArrayList<User> users) {
        ArrayList<User> usersBuff = new ArrayList<>();
        for (int i = 0, j = 0; i < users.size() && j < number; i++) {
            if (users.get(i).getNumberRatings() != 0) {
                usersBuff.add(users.get(i));
                j++;
            }
        }
        if (sortType.equals(Constants.ASC)) {
            usersBuff.sort(getcompByUser());
        } else {
            Collections.reverse(usersBuff);
        }
        return Constants.QUERY_RES + usersBuff.toString();
    }

    public String standardUser(ArrayList<Movie> movies,
                               ArrayList<Serial> serials) {

        for (Movie movie : movies) {
            if (!history.containsKey(movie.getTitle())) {
                return Constants.STANDARD_SUCCESS + movie.getTitle();
            }
        }
        for (Serial serial : serials) {
            if(!history.containsKey(serial.getTitle())) {
                return Constants.STANDARD_SUCCESS + serial.getTitle();
            }
        }
        return Constants.STANDARD_ERROR;
    }

    public String bestUnseen(ArrayList<Movie> movies, ArrayList<Serial> serials) {
        HashMap<String, Double> ratingShow = new HashMap<>();
        ArrayList<Movie> moviesBuff = new ArrayList<>(movies);
        ArrayList<Serial> serialBuff = new ArrayList<>(serials);

        for (Movie movie : moviesBuff) {
            ratingShow.put(movie.getTitle(), movie.getRating());
        }
        moviesBuff.sort(getCompByDoubleMovie(ratingShow, movies));
        for (Movie movie : moviesBuff) {
            if(!history.containsKey(movie.getTitle())) {
                return Constants.STANDARD_BEST_UNSEEN_SUCCESS + movie.getTitle();
            }
        }

        for (Serial serial : serialBuff) {
            ratingShow.put(serial.getTitle(), serial.getSerialRating());
        }
        serialBuff.sort(getCompByDoubleSerial(ratingShow, serials));
        for(Serial serial : serialBuff) {
            if(!history.containsKey(serial.getTitle())) {
                return Constants.STANDARD_BEST_UNSEEN_SUCCESS + serial.getTitle();
            }
        }
        return Constants.STANDARD_BEST_UNSEEN_ERROR;
    }

}
