package main;

import common.Constants;
import fileio.Input;
import fileio.InputLoader;
import fileio.UserInputData;
import fileio.Writer;
import org.json.simple.JSONArray;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Map;

public class User {
    private final String username;
    private final String subscriptionType;
    private final Map<String, Integer> history;
    private final ArrayList<String> favoriteMovies;

    public User(final String username, final String subscriptionType,
                final Map<String, Integer> history,
                final ArrayList<String> favoriteMovies) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.history = history;
        this.favoriteMovies = favoriteMovies;
    }

    public String getUsername() {
        return username;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    public ArrayList<String> getFavoriteMovies() {
        return favoriteMovies;
    }


    public String addFavorite(String videoTitle) {
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
                return Constants.ERROR + videoTitle + Constants.FAVORITE_ERROR2;
            }
        }
    }

    public String addView(String videoTitle) {
        if (videoTitle == null) {
            return null;
        }
        return null;
    }
}
