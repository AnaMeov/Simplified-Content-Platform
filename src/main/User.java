package main;

import common.Constants;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public class User {
    private final String username;
    private final String subscriptionType;
    private final Map<String, Integer> history;
    private final ArrayList<String> favoriteMovies;
    private int numberRatings = 0;

    public User(final String username, final String subscriptionType,
                final Map<String, Integer> history,
                final ArrayList<String> favoriteMovies) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.history = history;
        this.favoriteMovies = favoriteMovies;
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
        return favoriteMovies;
    }

    public final int getNumberRatings() {
        return numberRatings;
    }


    public static Comparator<User> getCompByRating() {
        return new Comparator<User>() {
            @Override
            public int compare(final User u1, final User u2) {
                if (u1.getNumberRatings() < u2.getNumberRatings()) {
                    return -1;
                } else if (u1.getNumberRatings() > u2.getNumberRatings()) {
                    return 1;
                } else {
                    return 0;
                }
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
                if (!favoriteMovies.contains(videoTitle)) {
                    favoriteMovies.add(videoTitle);
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
     * functia de adaugare de rating
     *
     * @param movies
     * @param serials
     * @param videoTitle
     * @param rating
     * @param username
     * @return
     */
    public final String addRating(final ArrayList<Movie> movies, final ArrayList<Serial> serials,
                                  final String videoTitle, final double rating,
                                  final String username) {
        if (videoTitle == null) {
            return null;
        } else {
            if (history.containsKey(videoTitle)) {
                for (Movie movie : movies) {
                    if (movie.getTitle().equals(videoTitle)) {
                        movie.setRating(rating);
                        numberRatings++;
                        return Constants.SUCCESS + videoTitle + Constants.RATED
                                + rating + Constants.BY + username;
                    }
                }
                for (Serial serial : serials) {
                    if (serial.getTitle().equals(videoTitle)) {
                        for (int i = 0; i < serial.getNumberOfSeasons(); i++) {
                            for (int j = 0; j < serial.getSeasons().get(i).getRatings().size();
                                 j++) {
                                serial.setRating(serial.getSeasons().get(i).getRatings().get(j));
                            }
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
        return null;
    }

}
