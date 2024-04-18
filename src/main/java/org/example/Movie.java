package org.example;

import java.util.List;

public class Movie {
    private String title;
    private int year;
    private List<Rating> Ratings;

    public Movie(String title, int year){
        this.title = title;
        this.year = year;

    }

    public String getTitle() {
        return title;
    }
    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return title + " (" + year + ")";
    }
}
