package org.example;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private String title;
    private int year;
    private List<Rating> ratings;

    public Movie(String title, int year){
        this.title = title;
        this.year = year;
        this.ratings = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }
    public int getYear() {
        return year;
    }

    public void addRating(Rating rating) {
        this.ratings.add(rating);
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public List<Rating> ratingBySource(String source) {
        List<Rating> filterRatings = new ArrayList<>();
        for (Rating rating : ratings) {
            if(rating.getSource().equalsIgnoreCase((source)))
                filterRatings.add(rating);
        }
        return filterRatings;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(title).append(" (").append(year).append(")\nRatings:\n");
        for (Rating rating : ratings) {
            result.append(rating.toString()).append("\n");
        }
        return result.toString();
    }

}
