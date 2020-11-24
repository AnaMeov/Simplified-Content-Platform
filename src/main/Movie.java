package main;

import common.Constants;
import fileio.ActionInputData;

import java.util.*;

public class Movie extends Show {

    private final int duration;
    private double rating;

    public Movie(final String title, final ArrayList<String> cast,
                 final ArrayList<String> genres, final int year,
                 final int duration) {
        super(title, year, cast, genres);
        this.duration = duration;
    }

    public final int getDuration() {
        return duration;
    }

    public final double getRating() {
        return rating;
    }

    public final void setRating(final double rating) {
        this.rating = rating;
    }


    public static Comparator<Movie> getcompByView(HashMap<String, Integer> viewsMovie) {
        return new Comparator<Movie>() {
            @Override
            public int compare(Movie m1, Movie m2) {
                if(Integer.compare(viewsMovie.get(m1.getTitle()), viewsMovie.get(m2.getTitle()))==0) {
                    return m1.getTitle().compareTo(m2.getTitle());
                }
                return Integer.compare(viewsMovie.get(m1.getTitle()), viewsMovie.get(m2.getTitle()));
            }
        };
    }

    public static String mostViewed(ActionInputData currentCommand, final ArrayList<Movie> movies,
                                    final ArrayList<User> users) {
        HashMap<String, Integer> viewsMovie = new HashMap<>();
        ArrayList<Movie> moviesBuff = new ArrayList<>(movies);
        ArrayList<String> moviesList = new ArrayList<>();
        for(Movie movie : moviesBuff) {
            viewsMovie.put(movie.getTitle(), 0);
        }
        for(User user : users) {
            for (Map.Entry<String,Integer> entry : user.getHistory().entrySet()) {
//                entry.getKey(); //accesez titlul
//                entry.getValue(); //accesez nr
                if(viewsMovie.containsKey(entry.getKey())) {
                    viewsMovie.put(entry.getKey(), entry.getValue() + viewsMovie.get(entry.getKey()));
                } else {
                    viewsMovie.put(entry.getKey(), entry.getValue());
                }
            }
        }
        for(int i = moviesBuff.size() - 1; i >= 0; i--) {
            if(!Integer.toString(moviesBuff.get(i).getYear()).equals(currentCommand.getFilters().get(0).get(0))) {
                moviesBuff.remove(i);
                continue;
            }
            if(!moviesBuff.get(i).getGenres().contains(currentCommand.getFilters().get(1).get(0))) {
                moviesBuff.remove(i);
            }
        }
        moviesBuff.sort(getcompByView(viewsMovie));
        if(currentCommand.getSortType().equals(Constants.DSC)) {
            Collections.reverse(moviesBuff);
        }
        for(int i = 0, j = 0; i < moviesBuff.size() && j < currentCommand.getNumber(); i++) {
            if(viewsMovie.get(moviesBuff.get(i).getTitle()) !=0 ) {
                j++;
                moviesList.add(moviesBuff.get(i).getTitle());
            }
        }
        return Constants.QUERY_RES + moviesList.toString();
    }

}
