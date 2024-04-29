package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpHeaders;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.Movies;

public class Main {
    public static void main(String[] args) {
        Movies movies = new Movies();
        Ratings ratings = new Ratings();

        List<Movie> movieList = movies.getMovies();
        Map<String, Float> movieDict = new HashMap<>();
        System.out.println("\nThis is the size: " + movieList.size());

        for (int i = 0; i < movieList.size(); i++) {
            if (i % 10 == 0){
                System.out.println(i);
            }
            Movie movie = movieList.get(i);
            ratings.movieRating(movie);

            Rating rottenTRating = movie.ratingBySource("Rotten Tomatoes");
            if(rottenTRating != null) {
               movieDict.put(movie.getTitle(), rottenTRating.getValue());
            }

        }
        List<Map.Entry<String, Float>> sortedEntries = movieDict.entrySet();

        for (Map.Entry<String, Float> entry : movieDict.entrySet()) {
            System.out.println("Title: " + entry.getKey() + ", Rating: " + entry.getValue());
        }

    }
}
