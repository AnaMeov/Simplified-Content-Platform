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
    // hashmap containing movie name and rating
    private final Map<String, Double> ratingMovie = new HashMap<>();
    // hashmap containing serial name and rating
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
     * Username to String
     */
    @Override
    public final String toString() {
        return username;
    }

    /**
     * Compare movies by a double (rating) and second by their order in the data base
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
     * Compare serials by a double (rating) and second by their order in the data base
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
     * Compare shows first by an integer (number of views) and second by their order in the
     * data base
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
     * Compare shows by number of favorites
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
     * Compare shows first by a double (rating) and by name
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
     * Compare users first by a double (rating) and second by their username
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
     * Command to put a show to favorite
     */
    public final String addFavorite(final ActionInputData currentCommand) {
        if (currentCommand.getTitle() == null) {
            return null;
        } else {
            if (history.containsKey(currentCommand.getTitle())) {
                if (!favoriteShows.contains(currentCommand.getTitle())) {
                    favoriteShows.add(currentCommand.getTitle());
                    return Constants.SUCCESS + currentCommand.getTitle()
                            + Constants.FAVORITE_SUCCESS;
                } else {
                    return Constants.ERROR + currentCommand.getTitle()
                            + Constants.FAVORITE_ERROR1;
                }
            } else {
                return Constants.ERROR + currentCommand.getTitle() + Constants.NOT_SEEN;
            }
        }
    }

    /**
     * Command to mark a show as viewed
     */
    public final String addView(final ActionInputData currentCommand) {
        if (currentCommand.getTitle() == null) {
            return null;
        } else {
            if (history.containsKey(currentCommand.getTitle())) {
                history.replace(currentCommand.getTitle(),
                        history.get(currentCommand.getTitle()) + 1);
                return Constants.SUCCESS + currentCommand.getTitle()
                        + Constants.VIEW_SUCCESS + history.get(currentCommand.getTitle());
            } else {
                history.put(currentCommand.getTitle(), Constants.VIEW_CONST);
                return Constants.SUCCESS + currentCommand.getTitle() + Constants.VIEW_SUCCESS
                        + Constants.VIEW_CONST;
            }
        }
    }

    /**
     * Command to add rating to a show
     */
    public final String addRating(final ActionInputData currCommand,
                                  final ArrayList<Movie> movies,
                                  final ArrayList<Serial> serials) {
        if (currCommand.getTitle() == null) {
            return null;
        } else {
            if (history.containsKey(currCommand.getTitle())) {
                for (Movie movie : movies) {
                    if (movie.getTitle().equals(currCommand.getTitle())) {
                        // rate a movie only if it's not already rated
                        if (ratingMovie.get(movie.getTitle()) == null) {
                            ratingMovie.put(movie.getTitle(), movie.getRating());
                            movie.setRating(currCommand.getGrade());
                            numberRatings++;
                            return Constants.SUCCESS + currCommand.getTitle() + Constants.RATED
                                    + currCommand.getGrade() + Constants.BY + username;
                        } else {
                            return Constants.ERROR + currCommand.getTitle() + Constants.AL_RATED;
                        }
                    }
                }
                for (Serial serial : serials) {
                    if (serial.getTitle().equals(currCommand.getTitle())) {
                        // rate a serial only if it's not already rated
                        ratingSerial.computeIfAbsent(currCommand.getTitle(),
                                k -> new ArrayList<>());

                        if (ratingSerial.get(currCommand.getTitle()).
                                contains(currCommand.getSeasonNumber())) {
                            return Constants.ERROR + currCommand.getTitle()
                                    + Constants.AL_RATED;

                        } else {
                            ArrayList<Integer> seasonBuff =
                                    ratingSerial.get(currCommand.getTitle());
                            seasonBuff.add(currCommand.getSeasonNumber());
                            // populate the hashmap
                            ratingSerial.put(currCommand.getTitle(), seasonBuff);
                            serial.getSeasons().get(currCommand.getSeasonNumber() - 1).
                                    getRatings().add(currCommand.getGrade());
                            numberRatings++;
                            return Constants.SUCCESS + currCommand.getTitle()
                                    + Constants.RATED + currCommand.getGrade()
                                    + Constants.BY + username;
                        }
                    }
                }
            } else {
                return Constants.ERROR + currCommand.getTitle() + Constants.NOT_SEEN;
            }
        }
        return Constants.ERROR + currCommand.getTitle() + Constants.NOT_SEEN;
    }

    /**
     * Function to sort first N users by their number of ratings
     */
    public static String ratingsNumber(final ActionInputData currCommand,
                                       final ArrayList<User> users) {
        ArrayList<User> usersBuff = new ArrayList<>();
        ArrayList<String> usernames = new ArrayList<>();

        // populate the users' arraylist
        for (User user : users) {
            if (user.getNumberRatings() != 0) {
                usersBuff.add(user);
            }
        }

        // sort the arraylist
        usersBuff.sort(getcompByUser());
        if ((currCommand.getSortType().equals(Constants.DSC))) {
            Collections.reverse(usersBuff);
        }

        // selecting first N users
        for (int j = 0; j < currCommand.getNumber() && j < usersBuff.size(); j++) {
            usernames.add(usersBuff.get(j).getUsername());
        }
        return Constants.QUERY_RES + usernames;
    }

    /**
     * Function to determine first unseen video from data base
     */
    public String standardUser(final ArrayList<Movie> movies,
                               final ArrayList<Serial> serials) {

        for (Movie movie : movies) {
            // verify if a movie isn't in user's history
            if (!history.containsKey(movie.getTitle())) {
                return Constants.STANDARD_SUCCESS + movie.getTitle();
            }
        }
        for (Serial serial : serials) {
            // verify if a serial isn't in user's history
            if (!history.containsKey(serial.getTitle())) {
                return Constants.STANDARD_SUCCESS + serial.getTitle();
            }
        }
        return Constants.STANDARD_ERROR;
    }

    /**
     * Function to determine best video unseen by a user
     */
    public String bestUnseen(final ArrayList<Movie> movies, final ArrayList<Serial> serials) {

        // hashmap that contains shows' name and rating
        HashMap<String, Double> ratingShow = new HashMap<>();
        ArrayList<Movie> moviesBuff = new ArrayList<>(movies);
        ArrayList<Serial> serialBuff = new ArrayList<>(serials);

        // populate the hashmap with movies
        for (Movie movie : moviesBuff) {
            ratingShow.put(movie.getTitle(), movie.getRating());
        }

        // sort the hashmap after rating
        moviesBuff.sort(getCompByDoubleMovie(ratingShow, movies));
        for (Movie movie : moviesBuff) {
            // verify if the best movie isn't in user's history
            if (!history.containsKey(movie.getTitle())) {
                return Constants.STANDARD_BEST_UNSEEN_SUCCESS + movie.getTitle();
            }
        }

        // populate the hashmap with serials
        for (Serial serial : serialBuff) {
            ratingShow.put(serial.getTitle(), serial.getSerialRating());
        }

        // sort the hashmap after rating
        serialBuff.sort(getCompByDoubleSerial(ratingShow, serials));
        for (Serial serial : serialBuff) {
            // verify if the best serial isn't in user's history
            if (!history.containsKey(serial.getTitle())) {
                return Constants.STANDARD_BEST_UNSEEN_SUCCESS + serial.getTitle();
            }
        }
        return Constants.STANDARD_BEST_UNSEEN_ERROR;
    }

    /**
     * Function to determine all shows from a specific genre
     */
    public String searchPremium(final ActionInputData currentCommand,
                                final ArrayList<Movie> movies,
                                final ArrayList<Serial> serials) {

        // hashmap that contains shows' name and rating
        HashMap<String, Double> ratingShow = new HashMap<>();
        ArrayList<Movie> moviesBuff = new ArrayList<>(movies);
        ArrayList<Serial> serialBuff = new ArrayList<>(serials);
        ArrayList<Show> showBuff = new ArrayList<>();

        // populate the hashmap
        for (Movie movie : moviesBuff) {
            ratingShow.put(movie.getTitle(), movie.getRating());
        }
        for (Serial serial : serialBuff) {
            ratingShow.put(serial.getTitle(), serial.getSerialRating());
        }

        showBuff.addAll(movies);
        showBuff.addAll(serials);

        // remove show if it doesn't have the input genre and if it's in users' history
        showBuff.removeIf(show -> !show.getGenres().contains(currentCommand.getGenre())
                || history.containsKey(show.getTitle()));

        // sort the hashmap
        showBuff.sort(getCompByDoubleShow(ratingShow));

        // check if the user is premium
        if (subscriptionType.equals(Constants.PREMIUM) && showBuff.size() > 0) {
            return Constants.SEARCH_SUCCESS + showBuff.toString();
        }
        return Constants.SEARCH_ERROR;
    }

    /**
     * Function to determine the most common video in the favorites list
     * unseen by the user
     */
    public String favoritePremium(final ArrayList<Movie> movies,
                                  final ArrayList<Serial> serials,
                                  final ArrayList<User> users) {

        // hashmap that contains shows' name and number of favorite
        HashMap<String, Integer> favoriteShows = new HashMap<>();
        ArrayList<Show> showBuff = new ArrayList<>();

        showBuff.addAll(movies);
        showBuff.addAll(serials);
        ArrayList<Show> showBuff2 = new ArrayList<>(showBuff);

        for (Show show : showBuff) {
            favoriteShows.put(show.getTitle(), 0);
        }

        // populate the hashmap
        for (User user : users) {
            for (String title : user.getFavoriteShows()) {
                if (favoriteShows.containsKey(title)) {
                    favoriteShows.put(title, favoriteShows.get(title) + 1);
                } else {
                    favoriteShows.put(title, 1);
                }
            }
        }

        // sort the hashmap
        showBuff.sort(getCompByInt(favoriteShows, showBuff2));

        // remove show is seen by the user
        showBuff.removeIf(show -> history.containsKey(show.getTitle()));

        // check if the user is premium
        if (subscriptionType.equals(Constants.PREMIUM) && showBuff.size() > 0) {
            return Constants.FAV_SUCCESS + showBuff.get(0).getTitle();
        }

        return Constants.FAVORITE_ERROR;
    }

    /**
     * Function to determine the most popular video by genre that is not seen
     */
    public String popularPremium(final ActionInputData currentCommand,
                                 final ArrayList<Movie> movies,
                                 final ArrayList<Serial> serials,
                                 final ArrayList<User> users) {

        // hashmap that contains the genre and its number of views
        HashMap<String, Integer> popularGenre = new HashMap<>();
        // hashmap that contains name of the show and Show object
        HashMap<String, Show> linkShow = new HashMap<>();
        // hashmap that contains name of the show and list of genres
        HashMap<String, ArrayList<String>> genreShow = new HashMap<>();
        ArrayList<Show> showBuff = new ArrayList<>();
        ArrayList<String> genres = new ArrayList<>();

        showBuff.addAll(movies);
        showBuff.addAll(serials);

        for (Show show : showBuff) {
            linkShow.put(show.getTitle(), show);
        }

        // populate the genre hashmap
        for (Show show : showBuff) {
            for (String genre : show.getGenres()) {
                if (genreShow.containsKey(genre)) {
                    genreShow.get(genre).add(show.getTitle());
                } else {
                    genres.add(genre);
                    genreShow.put(genre, new ArrayList<>());
                    genreShow.get(genre).add(show.getTitle());
                }
            }
        }

        // populate the most popular genre hashmap
        for (User user : users) {
            for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
                for (String genre : linkShow.get(entry.getKey()).getGenres()) {
                    if (popularGenre.containsKey(genre)) {
                        popularGenre.put(genre, popularGenre.get(genre) + entry.getValue());
                    } else {
                        popularGenre.put(genre, entry.getValue());
                    }
                }
            }
        }

        // sort the hashmap
        genres.sort(getCompByIntFav(popularGenre));

        for (User user : users) {
            if (currentCommand.getUsername().equals(user.getUsername())) {
                // verify if a user is premium
                if (user.subscriptionType.equals(Constants.PREMIUM)) {
                    for (String genre : genres) {
                        for (String show : genreShow.get(genre)) {
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
