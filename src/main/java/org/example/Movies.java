package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Movies {
     public String[] getMovies() {
        List<String> titles = new ArrayList<>();
        int offset = 0;
        int total = 100;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://unogs-unogs-v1.p.rapidapi.com/search/titles?country_list=78&order_by=date&type=movie"))
                .header("X-RapidAPI-Key", "133e9bfae6mshb9de6d3c5b6cbc3p115558jsnd9346f6be764")
                .header("X-RapidAPI-Host", "unogs-unogs-v1.p.rapidapi.com")
                .build();
        //First run, get total and first batch of movies
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject object = jsonObject.getAsJsonObject("Object");

            total = object.get("total").getAsInt();
            System.out.println("This is the total: " + total);

            List<String> newTitles = GetTitles(response.body());
            titles.addAll(newTitles);
            offset += newTitles.size();
            System.out.println("This is the Offset: " + offset);
        } catch (IOException| InterruptedException e) {
            e.printStackTrace();
        }

        //Complete the run
        while(total > titles.size()) {
            String uriString = "https://unogs-unogs-v1.p.rapidapi.com/search/titles?country_list=78&offset=" + offset + "&order_by=date&type=movie";

            request = HttpRequest.newBuilder()
                    .uri(URI.create(uriString))
                    .header("X-RapidAPI-Key", "133e9bfae6mshb9de6d3c5b6cbc3p115558jsnd9346f6be764")
                    .header("X-RapidAPI-Host", "unogs-unogs-v1.p.rapidapi.com")
                    .build();
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                List<String> newTitles = GetTitles(response.body());
                titles.addAll(newTitles);
                offset += newTitles.size();

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return titles.toArray(new String[0]);
    }
    private List<String> GetTitles(String jsonRes) {
        Gson gson = new Gson();
        JsonObject jsonObject = JsonParser.parseString(jsonRes).getAsJsonObject();
        JsonArray results = jsonObject.getAsJsonArray("results");
        List<String> titles = new ArrayList<>();

        for(int i = 0; i < results.size(); i++) {
            JsonObject movie = results.get(i).getAsJsonObject();
            String title = movie.get("title").getAsString();
            int year = movie.get("year").getAsInt();
            titles.add((title));
        }

        return titles;
    }
}


