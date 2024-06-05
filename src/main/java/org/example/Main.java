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
        String genreNum = "801362";

        List<Movie> movieList = movies.getMovies(genreNum);
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
