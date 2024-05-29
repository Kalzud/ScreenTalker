package com.eou.screentalker;


import static junit.framework.TestCase.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieListTest {

    @Test
    public void testFindMostFrequentType() {
        List<Movie> movies = new MovieListBuilder()
                .addMovie("Movie 1", "Drama")
                .addMovie("Movie 2", "Comedy")
                .addMovie("Movie 3", "Drama")
                .addMovie("Movie 4", "Action")
                .build();

        String mostFrequentType = findMostFrequentType(movies);
        assertEquals("Drama", mostFrequentType);
    }

    @Test
    public void testGetMoviesByType() {
        List<Movie> movies = new MovieListBuilder()
                .addMovie("Movie 1", "Drama")
                .addMovie("Movie 2", "Comedy")
                .addMovie("Movie 3", "Action")
                .addMovie("Movie 4", "Comedy")
                .build();
        System.out.println(movies);

        List<Movie> comedyMovies = getMoviesByType(movies, "Comedy");
        assertEquals(2, comedyMovies.size());
    }


    public static String findMostFrequentType(List<Movie> movies) {
        Map<String, Integer> typeCountMap = new HashMap<>();
        for (Movie movie : movies) {
            String type = movie.getType();
            if (type != null) {
                int count = typeCountMap.getOrDefault(type, 0);
                count++;
                typeCountMap.put(type, count);
            }
        }

        int maxCount = 0;
        String mostFrequentType = null;
        for (Map.Entry<String, Integer> entry : typeCountMap.entrySet()) {
            String type = entry.getKey();
            int count = entry.getValue();
            if (count > maxCount) {
                maxCount = count;
                mostFrequentType = type;
            }
        }

        return mostFrequentType;
    }


    public static List<Movie> getMoviesByType(List<Movie> movies, String type) {
        System.out.println("got here");
        List<Movie> filteredMovies = new ArrayList<>();
        System.out.println(type);
        for (Movie movie : movies) {
            if (type != null && movie.getType() != null && type.equals(movie.getType())) { // Check for null type and movie.getType() before comparison
                filteredMovies.add(movie);
            }
        }
        return filteredMovies;
    }
}

