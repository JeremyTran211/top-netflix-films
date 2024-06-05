package org.example.genres;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.HashMap;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Genres {
    public static void main(String[] args){
        HashMap<String, String> genreMap = new HashMap<String, String>();
        genreMap.put("Acción y aventuras", null);
        genreMap.put("Anime", null);
        genreMap.put("Children & Family", null);
        genreMap.put("Classic", null);
        genreMap.put("Comedies", null);
        genreMap.put("Documentaries", null);
        genreMap.put("Dramas", null);
        genreMap.put("Horror", null);
        genreMap.put("Music", null);
        genreMap.put("Romantic", null);
        genreMap.put("Sci-fi & Fantasy", null);
        genreMap.put("Sports", null);
        genreMap.put("Thrillers", null);
        genreMap.put("TV Shows", null);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://unogs-unogs-v1.p.rapidapi.com/static/genres"))
                .header("X-RapidAPI-Key", "133e9bfae6mshb9de6d3c5b6cbc3p115558jsnd9346f6be764")
                .header("X-RapidAPI-Host", "unogs-unogs-v1.p.rapidapi.com")
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonArray results = jsonObject.getAsJsonArray("results");

            for(int i = 0; i < results.size(); i++){
                JsonObject genreObj = results.get(i).getAsJsonObject();
                String genreName = genreObj.get("genre").getAsString();

                if(genreMap.containsKey(genreName)) {
                    genreMap.put(genreName, genreObj.get("netflix_id").getAsString());
                }
            }
        } catch(IOException | InterruptedException e){
            e.printStackTrace();
        }

        System.out.println(genreMap);

    }
}
