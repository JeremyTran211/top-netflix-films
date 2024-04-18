package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;

public class Rating {

    private String source;
    private float rating;

    public Rating(String source, String value) {
        this.source = source;
        this.value = parseRating(value);
    }

    private static final String API_KEY = "bf4d1a8e";

    private static String parseRating(String responseBody) {
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonArray ratings = jsonObject.getAsJsonArray("Ratings");

        if (ratings == null) {
            return "No ratings available";
        }

        StringBuilder ratingsBuilder = new StringBuilder();

        for (JsonElement ratingElement : ratings) {
            JsonObject ratingObj = ratingElement.getAsJsonObject();
            String source = ratingObj.get("Source").getAsString();
            String value = ratingObj.get("Value").getAsString();
            ratingsBuilder.append(source).append(": ").append(value).append("\n");
        }

        return ratingsBuilder.toString();
    }

    public static String movieRating(String title, int year) {
        HttpClient client = HttpClient.newHttpClient();
        String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
        String url = String.format("http://www.omdbapi.com/?apikey=%s&t=%s&y=%d", API_KEY, encodedTitle, year);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return parseRating(response.body());

        } catch (Exception e) {
            System.out.print("Error occured at getting rating: " + e.getMessage());
            return "Error getting rating.";
        }
    }

    public static void main(String[] args) {
        String title = "Dune";
        int year = 2021;
        String ratings = movieRating(title, year);
        System.out.println("Ratings for " + title + " (" + year + "):\n" + ratings);
    }
}
