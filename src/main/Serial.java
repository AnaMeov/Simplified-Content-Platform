package main;

import common.Constants;
import entertainment.Season;
import fileio.ActionInputData;

import java.util.*;

public class Serial extends Show {

    private final int numberOfSeasons;
    private final ArrayList<Season> seasons;
    private double rating;
    private static HashMap<String, Integer> favoriteSerials = new HashMap<>();


    public Serial(final String title, final int year, final ArrayList<String> genres,
                  final ArrayList<String> cast, final int numberOfSeasons,
                  final ArrayList<Season> seasons) {

        super(title, year, cast, genres);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
    }

    public final int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public final ArrayList<Season> getSeasons() {
        return seasons;
    }

    public final double getRating() {
        return rating;
    }

    public final void setRating(final double rating) {
        this.rating = rating;
    }

    /**
     * @param compSerial
     * @return
     */
    public static Comparator<Serial> getCompByInt(final HashMap<String, Integer> compSerial) {
        return new Comparator<Serial>() {
            @Override
            public int compare(final Serial s1, final Serial s2) {
                if (Integer.compare(compSerial.get(s1.getTitle()),
                        compSerial.get(s2.getTitle())) == 0) {
                    return s1.getTitle().compareTo(s2.getTitle());
                }
                return Integer.compare(compSerial.get(s1.getTitle()),
                        compSerial.get(s2.getTitle()));
            }
        };
    }

    /**
     * @param currentCommand
     * @param serials
     * @param users
     * @return
     */
    public static String mostViewed(final ActionInputData currentCommand,
                                    final ArrayList<Serial> serials,
                                    final ArrayList<User> users) {
        HashMap<String, Integer> viewsSerial = new HashMap<>();
        ArrayList<Serial> serialsBuff = new ArrayList<>(serials);
        ArrayList<String> serialsList = new ArrayList<>();
        for (Serial serial : serials) {
            viewsSerial.put(serial.getTitle(), 0);
        }
        for (User user : users) {
            for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
                if (viewsSerial.containsKey(entry.getKey())) {
                    viewsSerial.put(entry.getKey(), entry.getValue()
                            + viewsSerial.get(entry.getKey()));
                } else {
                    viewsSerial.put(entry.getKey(), entry.getValue());
                }
            }
        }
        for (int i = serialsBuff.size() - 1; i >= 0; i--) {
            if (!Integer.toString(serialsBuff.get(i).getYear()).
                    equals(currentCommand.getFilters().get(0).get(0))) {
                serialsBuff.remove(i);
                continue;
            }
            if (!serialsBuff.get(i).getGenres().
                    contains(currentCommand.getFilters().get(1).get(0))) {
                serialsBuff.remove(i);
            }
        }
        serialsBuff.sort(getCompByInt(viewsSerial));
        if (currentCommand.getSortType().equals(Constants.DSC)) {
            Collections.reverse(serialsBuff);
        }
        for (int i = 0, j = 0; i < serialsBuff.size() && j < currentCommand.getNumber(); i++) {
            if (viewsSerial.get(serialsBuff.get(i).getTitle()) != 0) {
                j++;
                serialsList.add(serialsBuff.get(i).getTitle());
            }
        }
        return Constants.QUERY_RES + serialsList.toString();
    }

    /**
     * @param currentCommand
     * @param serials
     * @param users
     * @return
     */
    public static String favoriteSerials(final ActionInputData currentCommand,
                                        final ArrayList<Serial> serials,
                                        final ArrayList<User> users) {
        ArrayList<Serial> serialsBuff = new ArrayList<>(serials);
        ArrayList<String> serialList = new ArrayList<>();

        for(Serial serial : serials) {
            favoriteSerials.put(serial.getTitle(), 0);
        }
        for (User user : users) {
            for (String videoTitle : user.getFavoriteMovies()) {
                for (Serial serial : serials) {
                    if (serial.getTitle().equals(videoTitle)) {
                        if (favoriteSerials.containsKey(videoTitle)) {
                            favoriteSerials.put(videoTitle, favoriteSerials.get(videoTitle) + 1);
                        } else {
                            favoriteSerials.put(videoTitle, 1);
                        }
                    }
                }
            }
        }
        for (int i = serialsBuff.size() - 1; i >= 0; i--) {
            if (!Integer.toString(serialsBuff.get(i).getYear()).
                    equals(currentCommand.getFilters().get(0).get(0))) {
                serialsBuff.remove(i);
                continue;
            }
            if (!serialsBuff.get(i).getGenres().
                    contains(currentCommand.getFilters().get(1).get(0))) {
                serialsBuff.remove(i);
            }
        }
        serialsBuff.sort(getCompByInt(favoriteSerials));
        if (currentCommand.getSortType().equals(Constants.DSC)) {
            Collections.reverse(serialsBuff);
        }
        for (int i = 0, j = 0; i < serialsBuff.size() && j < currentCommand.getNumber(); i++) {
            if(favoriteSerials.containsKey(serialsBuff.get(i).getTitle())) {
                if (favoriteSerials.get(serialsBuff.get(i).getTitle()) != 0) {
                    j++;
                    serialList.add(serialsBuff.get(i).getTitle());
                }
            }
        }
        return Constants.QUERY_RES + serialList.toString();
    }

}
