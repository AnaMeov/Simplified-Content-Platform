package main;

import common.Constants;

import java.util.ArrayList;
import java.util.Map;

public class Query extends User {
    public Query(final String username, final String subscriptionType,
                 final Map<String, Integer> history, final ArrayList<String> favoriteMovies) {
        super(username, subscriptionType, history, favoriteMovies);
    }

    /**
     * @param number
     * @param sortType
     * @param users
     * @return
     */
    public static String ratingsNumber(final int number, final String sortType,
                                       final ArrayList<User> users) {
        if (sortType.equals(Constants.ASC)) {
            for (int i = 0; i < number; i++) {
                users.sort(User.getCompByRating()); // pt descendent getCompByRating().reversed()
            }
            return Constants.QUERY_RES + users.toString();
        }
        return null;
    }

}
