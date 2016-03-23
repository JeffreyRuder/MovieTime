package com.epicodus.movietime;

import java.util.List;

import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;

/**
 * Created by Guest on 3/23/16.
 */
public class GenreBuilder {
    public static String getTopTwoGenres(MovieDb movie) {
        List<Genre> genres = movie.getGenres();
        String firstGenre = genres.get(0).getName();
        String secondGenre = "";
        if (genres.size() > 1) {
            secondGenre = ", " + genres.get(1).getName();
        }

        String topTwoGenres = firstGenre + secondGenre;

        return topTwoGenres;
    }
}
