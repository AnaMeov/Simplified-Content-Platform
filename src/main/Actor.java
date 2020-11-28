package main;

import actor.ActorsAwards;
import common.Constants;
import fileio.ActionInputData;
import fileio.ActorInputData;
import utils.Utils;

import java.util.*;

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

//    @Override
//    public String toString() {
//        return "ActorInputData{"
//                + "name='" + name + '\'';
//    }

    /**
     * @param compActor
     * @return
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

    public static Comparator<Actor> getCompByName() {
        return new Comparator<Actor>() {
            @Override
            public int compare(final Actor a1, final Actor a2) {
                return a1.getName().compareTo(a2.getName());
            }
        };
    }

    public int getNumberAwards() {
        int number = 0;
        for(Map.Entry<ActorsAwards,Integer> entry : awards.entrySet()) {
            number = number + entry.getValue();
        }
        return number;
    }

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


    public static String averageActors(final ActionInputData currentCommand,
                                       final ArrayList<Actor> actors,
                                       final ArrayList<Movie> movies,
                                       final ArrayList<Serial> serials) {
        HashMap<String, Double> ratingActors = new HashMap<>();
        HashMap<String, Double> ratingShows = new HashMap<>();
        ArrayList<Actor> actorsBuff = new ArrayList<>(actors);
        ArrayList<String> actorssList = new ArrayList<>();

        for (Actor actor : actors) {
            ratingActors.put(actor.getName(), 0.0);
        }
        for (Movie movie : movies) {
            ratingShows.put(movie.getTitle(), movie.getRating());
        }
        for (Serial serial : serials) {
            ratingShows.put(serial.getTitle(), serial.getSerialRating());
        }

        for (Actor actor : actors) {
            ratingActors.put(actor.getName(), 0.0);
            for (String filmography : actor.getFilmography()) {
                if (ratingShows.containsKey(filmography)) {
                    ratingActors.put(actor.getName(), ratingActors.get(actor.getName()) + ratingShows.get(filmography));
                }
            }
        }
        actorsBuff.sort(getCompByDouble(ratingActors));
        if (currentCommand.getSortType().equals(Constants.DSC)) {
            Collections.reverse(actorsBuff);
        }
        for (int i = 0, j = 0; i < actorsBuff.size()
                && j < currentCommand.getNumber(); i++) {
            if (ratingActors.get(actorsBuff.get(i).getName()) != 0) {
                j++;
                actorssList.add(actorsBuff.get(i).getName());
            }
        }
        return Constants.QUERY_RES + actorssList.toString();
    }

    public static String awardsActor(final ActionInputData currentCommand,
                                     final ArrayList<Actor> actors) {

        ArrayList<Actor> actorsBuff = new ArrayList<>(actors);
        ArrayList<String> actorssList = new ArrayList<>();

        actorsBuff.removeIf((actor) -> {
            for (String award : currentCommand.getFilters().get(3)) {
                if (!actor.getAwards().containsKey(Utils.stringToAwards(award))) {
                    return true;
                }
            }
            return false;
        });

        actorsBuff.sort(getCompByAwards());
        if (currentCommand.getSortType().equals(Constants.DSC)) {
            Collections.reverse(actorsBuff);
        }
        for (int i = 0; i < currentCommand.getNumber() && i < actorsBuff.size(); i++) {
            actorssList.add(actorsBuff.get(i).getName());
        }
        return Constants.QUERY_RES + actorssList.toString();
    }

    public static String filterDescription(ActionInputData currentCommand,
                                           ArrayList<Actor> actors) {

        ArrayList<Actor> actorsBuff = new ArrayList<>(actors);
        ArrayList<String> actorssList = new ArrayList<>();

        actorsBuff.removeIf((actor) -> {
            for (String keyword : currentCommand.getFilters().get(2)) {
                if (!actor.getCareerDescription().replace('\n', ' ').
                        toLowerCase().matches(".*\\b"+keyword+"\\b.*")) {
                    return true;
                }
            }
            return false;
        });

        actorsBuff.sort(getCompByName());
        if (currentCommand.getSortType().equals(Constants.DSC)) {
            Collections.reverse(actorsBuff);
        }
        for (int i = 0; i < actorsBuff.size(); i++) {
            actorssList.add(actorsBuff.get(i).getName());
        }
        return Constants.QUERY_RES + actorssList.toString();
    }
}
