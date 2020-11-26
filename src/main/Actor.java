package main;

import actor.ActorsAwards;

import java.util.ArrayList;
import java.util.Map;

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

}
