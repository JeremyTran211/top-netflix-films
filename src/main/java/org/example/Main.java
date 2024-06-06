package org.example;

import java.util.*;
import java.io.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.Map.Entry;


public class Main {

    public static void processMovie(Movie movie, Map<String, Float> movieDict, Ratings ratings){
        ratings.movieRating(movie);
        Rating rottenTRating = movie.ratingBySource("Rotten Tomatoes");
        if(rottenTRating != null){
            synchronized (movieDict){
                movieDict.put(movie.getTitle(), rottenTRating.getValue());
            }

        }
    }
    public static void main(String[] args) {
        Movies movies = new Movies();
        Ratings ratings = new Ratings();
        int genreID = 0;

        HashMap<String, Integer> genreMap = new HashMap<String, Integer>();
        genreMap.put("Action", 801362);
        genreMap.put("Anime", 7424);
        genreMap.put("Children", 783);
        genreMap.put("Classic", 31574);
        genreMap.put("Comedy", 6548);
        genreMap.put("Documentaries", 6839);
        genreMap.put("Dramas", 5763);
        genreMap.put("Horror", 8711);
        genreMap.put("Music", 1701);
        genreMap.put("Romantic", 8883);
        genreMap.put("Sci-fi & Fantasy", 1492);
        genreMap.put("Sports", 4370);
        genreMap.put("Thrillers", 8933);
        genreMap.put("TV Shows", 83);

        List<String> genres = new ArrayList<>(genreMap.keySet());

        System.out.println("Select a genre");
        for(int i = 0; i < genres.size(); i++){
            System.out.println((i + 1) + ". " + genres.get(i));
        }

        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        try {
            int index = Integer.parseInt(input) - 1;
            if (index >= 0 && index < genres.size()) {
                genreID = genreMap.get(genres.get(index));
            }

        } catch(NumberFormatException e) {
            input = input.trim();

            if(genreMap.containsKey(input)) {
                genreID = genreMap.get(input);
            }
        }

        List<Movie> movieList = movies.getMovies(genreID);
        Map<String, Float> movieDict = new ConcurrentHashMap<>();
        System.out.println("\nThis is the size: " + movieList.size());

        int numberOfThreads = Math.max(1,Runtime.getRuntime().availableProcessors() / 2);
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        for (int i = 0; i < movieList.size(); i++) {
            final Movie movie = movieList.get(i);
            try {
                executor.submit(() -> processMovie(movie, movieDict, ratings));
            } catch (RejectedExecutionException e) {
                System.err.println("Failed to submit task: " + e.getMessage());
            }
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            executor.shutdownNow();
        }

        List<Map.Entry<String, Float>> sortedEntries = new ArrayList<>(movieDict.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        try (PrintWriter writer = new PrintWriter(new File("movies.csv"))) {

            for (Entry<String, Float> entry : sortedEntries) {
                writer.println(entry.getKey() + ": " + entry.getValue());
            }
        } catch (FileNotFoundException e) {
            System.err.println("An error occurred while writing the CSV file: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("CSV file was created successfully.");
    }

}
