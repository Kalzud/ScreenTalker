package com.eou.screentalker;

import java.util.ArrayList;
import java.util.List;

public class MovieListBuilder {

    private List<Movie> movies;

    public MovieListBuilder() {
        this.movies = new ArrayList<>();
    }

    public MovieListBuilder addMovie(String title, String type) {
        movies.add(new Movie(title, type));
        return this;
    }

    public List<Movie> build() {
        return movies;
    }
}
