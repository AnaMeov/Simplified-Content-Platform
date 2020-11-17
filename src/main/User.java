package main;

import java.util.ArrayList;
import java.util.Map;

public class User {
    private String username;
    private String subType;
    Map<String, Integer> history;
    ArrayList<String> favMovie;

    public User(String username, String subType, Map<String, Integer> history, ArrayList<String> favMovie) {
        this.username = username;
        this.subType = subType;
        this.history = history;
        this.favMovie = favMovie;
    }
    
}
