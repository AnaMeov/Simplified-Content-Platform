package main;

import common.Constants;
import fileio.ActionInputData;

import java.net.UnknownServiceException;
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

    public final ArrayList<String> getFavoriteShows() {
        return favoriteShows;
    }

    public final int getNumberRatings() {
        return numberRatings;
    }

    /**
     * @return
     */
    @Override
    public final String toString() {
        return username;
    }

    /**
     * @param compMovie
     * @param movies
     * @return
     */
    public static Comparator<Movie> getCompByDoubleMovie(final HashMap<String, Double> compMovie,
                                                         final ArrayList<Movie> movies) {
        return new Comparator<Movie>() {
            @Override
            public int compare(final Movie m1, final Movie m2) {
                if (Double.compare(compMovie.get(m2.getTitle()),
                        compMovie.get(m1.getTitle())) == 0) {
                    return movies.indexOf(m1) - movies.indexOf(m2);
                }
                return Double.compare(compMovie.get(m2.getTitle()),
                        compMovie.get(m1.getTitle()));
            }
        };
    }

    /**
     * @param compSerial
     * @param serials
     * @return
     */
    public static Comparator<Serial> getCompByDoubleSerial(final HashMap<String, Double> compSerial,
                                                           final ArrayList<Serial> serials) {
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
     * @param compShow
     * @return
     */
    public static Comparator<Show> getCompByInt(final HashMap<String, Integer> compShow,
                                                final ArrayList<Show> shows) {
        return new Comparator<Show>() {
            @Override
            public int compare(final Show s1, final Show s2) {
                if (Integer.compare(compShow.get(s1.getTitle()),
                        compShow.get(s2.getTitle())) == 0) {
                    return shows.indexOf(s1) - shows.indexOf(s2);
                }
                return Integer.compare(compShow.get(s2.getTitle()),
                        compShow.get(s1.getTitle()));
            }
        };
    }

    /**
     * @param compShow
     * @return
     */
    public static Comparator<String> getCompByIntFav(final HashMap<String, Integer> compShow) {
        return new Comparator<String>() {
            @Override
            public int compare(final String s1, final String s2) {
                return Integer.compare(compShow.get(s2), compShow.get(s1));
            }
        };
    }

    /**
     * @param compShow
     * @return
     */
    public static Comparator<Show> getCompByDoubleShow(final HashMap<String, Double> compShow) {
        return new Comparator<Show>() {
            @Override
            public int compare(final Show s1, final Show s2) {
                if (Double.compare(compShow.get(s2.getTitle()),
                        compShow.get(s1.getTitle())) == 0) {
                    return s1.getTitle().compareTo(s2.getTitle());
                }
                return Double.compare(compShow.get(s1.getTitle()),
                        compShow.get(s2.getTitle()));
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
                if (Double.compare(u1.getNumberRatings(),
                        u2.getNumberRatings()) == 0) {
                    return u1.getUsername().compareTo(u2.getUsername());
                }
                return Double.compare(u1.getNumberRatings(),
                        u2.getNumberRatings());
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
        ArrayList<String> usernames = new ArrayList<>();

        for (int i = 0, j = 0; i < users.size(); i++) {
            if (users.get(i).getNumberRatings() != 0) {
                usersBuff.add(users.get(i));
            }
        }

        usersBuff.sort(getcompByUser());
        if ((sortType.equals(Constants.DSC))) {
            Collections.reverse(usersBuff);
        }
        for(int j = 0; j < number && j < usersBuff.size(); j++) {
            usernames.add(usersBuff.get(j).getUsername());
        }
        return Constants.QUERY_RES + usernames;
    }

    /**
     * @param movies
     * @param serials
     * @return
     */
    public String standardUser(final ArrayList<Movie> movies,
                               final ArrayList<Serial> serials) {

        for (Movie movie : movies) {
            if (!history.containsKey(movie.getTitle())) {
                return Constants.STANDARD_SUCCESS + movie.getTitle();
            }
        }
        for (Serial serial : serials) {
            if (!history.containsKey(serial.getTitle())) {
                return Constants.STANDARD_SUCCESS + serial.getTitle();
            }
        }
        return Constants.STANDARD_ERROR;
    }

    /**
     * @param movies
     * @param serials
     * @return
     */
    public String bestUnseen(final ArrayList<Movie> movies, final ArrayList<Serial> serials) {
        HashMap<String, Double> ratingShow = new HashMap<>();
        ArrayList<Movie> moviesBuff = new ArrayList<>(movies);
        ArrayList<Serial> serialBuff = new ArrayList<>(serials);

        for (Movie movie : moviesBuff) {
            ratingShow.put(movie.getTitle(), movie.getRating());
        }
        moviesBuff.sort(getCompByDoubleMovie(ratingShow, movies));
        for (Movie movie : moviesBuff) {
            if (!history.containsKey(movie.getTitle())) {
                return Constants.STANDARD_BEST_UNSEEN_SUCCESS + movie.getTitle();
            }
        }

        for (Serial serial : serialBuff) {
            ratingShow.put(serial.getTitle(), serial.getSerialRating());
        }
        serialBuff.sort(getCompByDoubleSerial(ratingShow, serials));
        for (Serial serial : serialBuff) {
            if (!history.containsKey(serial.getTitle())) {
                return Constants.STANDARD_BEST_UNSEEN_SUCCESS + serial.getTitle();
            }
        }
        return Constants.STANDARD_BEST_UNSEEN_ERROR;
    }

    /**
     * @param currentCommand
     * @param movies
     * @param serials
     * @return
     */
    public String searchPremium(final ActionInputData currentCommand,
                                final ArrayList<Movie> movies,
                                final ArrayList<Serial> serials) {
        HashMap<String, Double> ratingShow = new HashMap<>();
        ArrayList<Movie> moviesBuff = new ArrayList<>(movies);
        ArrayList<Serial> serialBuff = new ArrayList<>(serials);
        ArrayList<Show> showBuff = new ArrayList<>();

        for (Movie movie : moviesBuff) {
            ratingShow.put(movie.getTitle(), movie.getRating());
        }
        for (Serial serial : serialBuff) {
            ratingShow.put(serial.getTitle(), serial.getSerialRating());
        }

        showBuff.addAll(movies);
        showBuff.addAll(serials);

        showBuff.removeIf(show -> !show.getGenres().contains(currentCommand.getGenre())
                || history.containsKey(show.getTitle()));

        showBuff.sort(getCompByDoubleShow(ratingShow));
        if (subscriptionType.equals(Constants.PREMIUM) && showBuff.size() > 0) {
            return Constants.SEARCH_SUCCESS + showBuff.toString();
        }
        return Constants.SEARCH_ERROR;
    }

    /**
     * @param currentCommand
     * @param movies
     * @param serials
     * @param users
     * @return
     */
    public String favoritePremium(final ActionInputData currentCommand,
                                  final ArrayList<Movie> movies,
                                  final ArrayList<Serial> serials, final ArrayList<User> users) {

        HashMap<String, Integer> favoriteShows = new HashMap<>();
        ArrayList<Show> showBuff = new ArrayList<>();
        //ArrayList<String> showList = new ArrayList<>();

        showBuff.addAll(movies);
        showBuff.addAll(serials);
        ArrayList<Show> showBuff2 = new ArrayList<>(showBuff);

        for (Show show : showBuff) {
            favoriteShows.put(show.getTitle(), 0);
        }

        for (User user : users) {
            for (String title : user.getFavoriteShows()) {
                if (favoriteShows.containsKey(title)) {
                    favoriteShows.put(title, favoriteShows.get(title) + 1);
                } else {
                    favoriteShows.put(title, 1);
                }
            }
        }

        showBuff.sort(getCompByInt(favoriteShows, showBuff2));
        showBuff.removeIf(show -> history.containsKey(show.getTitle()));

        if (subscriptionType.equals(Constants.PREMIUM) && showBuff.size() > 0) {
            return Constants.FAV_SUCCESS + showBuff.get(0).getTitle();
        }

        return Constants.FAVORITE_ERROR;
    }

    public String popularPremium(final ActionInputData currentCommand,
                                 final ArrayList<Movie> movies,
                                 final ArrayList<Serial> serials,
                                 final ArrayList<User> users) {

        HashMap<String, Integer> popularGenre = new HashMap<>();
        HashMap<String, Show> linkShow = new HashMap<>();
        HashMap<String, ArrayList<String>> genreShow = new HashMap<>();
        ArrayList<Show> showBuff = new ArrayList<>();
        ArrayList<String> genres = new ArrayList<>();


        showBuff.addAll(movies);
        showBuff.addAll(serials);

        for (Show show : showBuff) {
            linkShow.put(show.getTitle(), show);
        }

        for(Show show : showBuff) {
            for(String genre : show.getGenres()) {
                if (genreShow.containsKey(genre)) {
                        genreShow.get(genre).add(show.getTitle());
                    } else {
                        genres.add(genre);
                        genreShow.put(genre, new ArrayList<>());
                        genreShow.get(genre).add(show.getTitle());
                    }
            }
        }

        for (User user : users) {
            for(Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
                for(String genre : linkShow.get(entry.getKey()).getGenres()) {
                    if(popularGenre.containsKey(genre)) {
                        popularGenre.put(genre, popularGenre.get(genre) + entry.getValue());
                    } else {
                        popularGenre.put(genre, entry.getValue());
                    }
                }
            }
        }

        genres.sort(getCompByIntFav(popularGenre));

        for(User user : users) {
            if(currentCommand.getUsername().equals(user.getUsername())) {
                if(user.subscriptionType.equals(Constants.PREMIUM)) {
                    for(String genre : genres) {
                        for(String show : genreShow.get(genre)) {
                            if (!user.getHistory().containsKey(show)) {
                                return Constants.POPULAR_SUCCES + show;
                            }
                        }
                    }
                }
            }
        }
        return Constants.POPULAR_ERROR;
    }
}
