package main;

import checker.Checkstyle;
import checker.Checker;
import common.Constants;
import fileio.ActionInputData;
import fileio.SerialInputData;
import fileio.MovieInputData;
import fileio.Writer;
import fileio.UserInputData;
import fileio.Input;
import fileio.InputLoader;
import org.json.simple.JSONArray;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     *
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        //TODO add here the entry point to your implementation

        // imi generez baza de date pentru useri
        ArrayList<User> users = new ArrayList<>();
        for (UserInputData userInputData : input.getUsers()) {
            User user = new User(userInputData.getUsername(),
                    userInputData.getSubscriptionType(),
                    userInputData.getHistory(),
                    userInputData.getFavoriteMovies());
            users.add(user);
        }

        // imi generez baza de date pentru movies
        ArrayList<Movie> movies = new ArrayList<>();
        for (MovieInputData movieInputData : input.getMovies()) {
            Movie movie = new Movie(movieInputData.getTitle(),
                    movieInputData.getCast(), movieInputData.getGenres(),
                    movieInputData.getYear(), movieInputData.getDuration());
            movie.setType(Constants.MOVIE);
            movies.add(movie);
        }

        // imi generez baza de date pentru serials
        ArrayList<Serial> serials = new ArrayList<>();
        for (SerialInputData serialInputData : input.getSerials()) {
            Serial serial = new Serial(serialInputData.getTitle(),
                    serialInputData.getYear(),
                    serialInputData.getGenres(), serialInputData.getCast(),
                    serialInputData.getNumberSeason(),
                    serialInputData.getSeasons());
            serial.setType(Constants.SERIAL);
            serials.add(serial);
        }


        for (int i = 0; i < input.getCommands().size(); i++) {
            // variabila pentru lizibilitate
            ActionInputData currentCommand = input.getCommands().get(i);

            if (currentCommand.getActionType() != null) {
                if (currentCommand.getActionType().equals(Constants.COMMAND)) {
                    if (currentCommand.getType().equals(Constants.FAVORITE)) {
                        for (int j = 0; j < users.size(); j++) {
                            String output = users.get(j).addFavorite(currentCommand.getTitle());
                            if (currentCommand.getUsername().equals(users.get(j).getUsername())) {
                                arrayResult.add(fileWriter.writeFile(currentCommand.getActionId(),
                                        null, output));
                            }
                        }
                    } else if (input.getCommands().get(i).getType().equals(Constants.VIEW)) {
                        for (int j = 0; j < users.size(); j++) {
                            String output = users.get(j).addView(currentCommand.getTitle());
                            if (currentCommand.getUsername().equals(users.get(j).getUsername())) {
                                arrayResult.add(fileWriter.writeFile(currentCommand.getActionId(),
                                        null, output));
                            }
                        }
                    } else if (input.getCommands().get(i).getType().equals(Constants.RATING)) {
                        for (int j = 0; j < users.size(); j++) {
                            String output = users.get(j).addRating(movies, serials,
                                    currentCommand.getTitle(), currentCommand.getGrade(),
                                    currentCommand.getUsername());
                            if (currentCommand.getUsername().equals(users.get(j).getUsername())) {
                                arrayResult.add(fileWriter.writeFile(currentCommand.getActionId(),
                                        null, output));
                            }
                        }
                    }
                }
                if (currentCommand.getActionType().equals(Constants.QUERY)) {
                    String output = Query.ratingsNumber(currentCommand.getNumber(),
                                currentCommand.getSortType(), users);
                    arrayResult.add(fileWriter.writeFile(currentCommand.getActionId(),
                                    null, output));
                }
            }
        }
        fileWriter.closeJSON(arrayResult);
    }
}
