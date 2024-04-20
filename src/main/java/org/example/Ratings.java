package org.example;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;

public class Ratings {

    private static final String API_KEY = "bf4d1a8e";

    private void  parseRating(String responseBody, Movie movie) {
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonArray ratingsArray = jsonObject.getAsJsonArray("Ratings");

        if (ratingsArray == null) {
            movie.addRating(new Rating("No Ratings", "No ratings available"));
            return;
        }

        for (JsonElement ratingElement : ratingsArray) {
            JsonObject ratingObj = ratingElement.getAsJsonObject();
            String source = ratingObj.get("Source").getAsString();
            String value = ratingObj.get("Value").getAsString();
            movie.addRating(new Rating(source, value));
        }

    }

    public void movieRating(Movie movie) {
        HttpClient client = HttpClient.newHttpClient();
        String encodedTitle = URLEncoder.encode(movie.getTitle(), StandardCharsets.UTF_8);
        String url = String.format("http://www.omdbapi.com/?apikey=%s&t=%s&y=%d", API_KEY, encodedTitle, movie.getYear());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
           if (response.statusCode() == 200) {
               parseRating((response.body()), movie);
           } else {
               movie.addRating(new Rating("HTTP Error", "Status Code: " + response.statusCode() ));
           }

        } catch (Exception e) {
            movie.addRating(new Rating("Excpetion", e.getMessage()));

        }
    }

}
