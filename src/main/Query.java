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
        ArrayList<User> usersBuff = new ArrayList<>();
//        if (sortType.equals(Constants.ASC)) {
//            users.sort(User.getCompByRating()); // pt descendent getCompByRating().reversed()
//        } else {
//            users.sort(User.getCompByRating().reversed()); // pt descendent getCompByRating().reversed()
//        }
        for (int i = 0; i < number; i++) {
            if (i == users.size()) {
                break;
            }
            if (users.get(i).getNumberRatings() == 0) {
                continue;
            }
            usersBuff.add(users.get(i));
        }
        return Constants.QUERY_RES + usersBuff.toString();
    }

    public static String mostViewed(final Map<String,Integer> viewsShow, final int number,
                                    final ArrayList<Show> shows) {

        for(int i = 0; i < number; i++) {

        }
        return Constants.QUERY_RES + shows.toString();
    }

}
