package main;

import actor.ActorsAwards;
import common.Constants;
import fileio.ActionInputData;
import utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Map;
import java.util.Collections;

public class Actor {
    private final String name;
    private final String careerDescription;
    private final ArrayList<String> filmography;
    private final Map<ActorsAwards, Integer> awards;

    public Actor(final String name, final String careerDescription,
                 final ArrayList<String> filmography,
                 final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = filmography;
        this.awards = awards;

    }

    public final String getName() {
        return name;
    }

    public final ArrayList<String> getFilmography() {
        return filmography;
    }

    public final Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public final String getCareerDescription() {
        return careerDescription;
    }

    /**
     * Name to String
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Compare actors first by ratings and second by name
     */
    public static Comparator<Actor> getCompByDouble(final HashMap<String, Double> compActor) {
        return new Comparator<Actor>() {
            @Override
            public int compare(final Actor a1, final Actor a2) {
                if (Double.compare(compActor.get(a1.getName()),
                        compActor.get(a2.getName())) == 0) {
                    return a1.getName().compareTo(a2.getName());
                }
                return Double.compare(compActor.get(a1.getName()),
                        compActor.get(a2.getName()));
            }
        };
    }

    /**
     * Compare actors by name
     */
    public static Comparator<Actor> getCompByName() {
        return new Comparator<Actor>() {
            @Override
            public int compare(final Actor a1, final Actor a2) {
                return a1.getName().compareTo(a2.getName());
            }
        };
    }

    /**
     * Calculate number of awards
     */
    public int getNumberAwards() {
        int number = 0;
        for (Map.Entry<ActorsAwards, Integer> entry : awards.entrySet()) {
            number = number + entry.getValue();
        }
        return number;
    }

    /**
     * Compare actors first by number of awards, second by name
     */
    public static Comparator<Actor> getCompByAwards() {
        return new Comparator<Actor>() {
            @Override
            public int compare(final Actor a1, final Actor a2) {
                if (a1.getNumberAwards() - a2.getNumberAwards() != 0) {
                    return a1.getNumberAwards() - a2.getNumberAwards();
                }
                return a1.getName().compareTo(a2.getName());
            }
        };
    }

    /**
     * Function to determine first N actors sorted by the arithmetic mean of shows'
     * ratings
     */
    public static String averageActors(final ActionInputData currentCommand,
                                       final ArrayList<Actor> actors,
                                       final ArrayList<Movie> movies,
                                       final ArrayList<Serial> serials) {

        // hashmap to store actors' name and rating
        HashMap<String, Double> ratingActors = new HashMap<>();
        // hashmap to store shows' name and rating
        HashMap<String, Double> ratingShows = new HashMap<>();
        ArrayList<Actor> actorsBuff = new ArrayList<>(actors);
        ArrayList<String> actorsList = new ArrayList<>();

        for (Actor actor : actors) {
            ratingActors.put(actor.getName(), 0.0);
        }
        for (Movie movie : movies) {
            ratingShows.put(movie.getTitle(), movie.getRating());
        }
        for (Serial serial : serials) {
            ratingShows.put(serial.getTitle(), serial.getSerialRating());
        }

        // populate actors' hashmap
        for (Actor actor : actors) {
            int numberShows = 0;
            ratingActors.put(actor.getName(), 0.0);
            for (String filmography : actor.getFilmography()) {
                if (ratingShows.containsKey(filmography) && ratingShows.get(filmography) != 0) {
                    ratingActors.put(actor.getName(), ratingActors.get(actor.getName())
                            + ratingShows.get(filmography));
                    numberShows++;
                }
            }
            // calculate the arithmetic mean of ratings
            if (numberShows != 0) {
                ratingActors.put(actor.getName(), ratingActors.get(actor.getName()) / numberShows);
            }
        }

        // sort the actors
        actorsBuff.sort(getCompByDouble(ratingActors));
        if (currentCommand.getSortType().equals(Constants.DSC)) {
            Collections.reverse(actorsBuff);
        }

        // selecting first N actors
        for (int i = 0, j = 0; i < actorsBuff.size()
                && j < currentCommand.getNumber(); i++) {
            if (ratingActors.get(actorsBuff.get(i).getName()) != 0) {
                j++;
                actorsList.add(actorsBuff.get(i).getName());
            }
        }
        return Constants.QUERY_RES + actorsList.toString();
    }

    /**
     * Function to determine first N actors sorted by the awards
     */
    public static String awardsActor(final ActionInputData currentCommand,
                                     final ArrayList<Actor> actors) {

        ArrayList<Actor> actorsBuff = new ArrayList<>(actors);
        ArrayList<String> actorssList = new ArrayList<>();

        // remove actors from list if they don't have a specific award
        actorsBuff.removeIf((actor) -> {
            for (String award : currentCommand.getFilters().get(Constants.MAGIC_NUMBER)) {
                if (!actor.getAwards().containsKey(Utils.stringToAwards(award))) {
                    return true;
                }
            }
            return false;
        });

        // sort the actors
        actorsBuff.sort(getCompByAwards());
        if (currentCommand.getSortType().equals(Constants.DSC)) {
            Collections.reverse(actorsBuff);
        }
        for (int i = 0; i < currentCommand.getNumber() && i < actorsBuff.size(); i++) {
            actorssList.add(actorsBuff.get(i).getName());
        }
        return Constants.QUERY_RES + actorssList.toString();
    }

    /**
     * Function to determine all actors which have some specific keywords in their
     * description
     */
    public static String filterDescription(final ActionInputData currentCommand,
                                           final ArrayList<Actor> actors) {

        ArrayList<Actor> actorsBuff = new ArrayList<>(actors);
        ArrayList<String> actorssList = new ArrayList<>();

        // remove actors from list if they don't have specific words in their description
        actorsBuff.removeIf((actor) -> {
            for (String keyword : currentCommand.getFilters().get(2)) {
                if (!actor.getCareerDescription().replace('\n', ' ').
                        toLowerCase().matches(".*\\b" + keyword + "\\b.*")) {
                    return true;
                }
            }
            return false;
        });

        // sort the actors
        actorsBuff.sort(getCompByName());
        if (currentCommand.getSortType().equals(Constants.DSC)) {
            Collections.reverse(actorsBuff);
        }
        for (Actor actor : actorsBuff) {
            actorssList.add(actor.getName());
        }
        return Constants.QUERY_RES + actorssList.toString();
    }
}
