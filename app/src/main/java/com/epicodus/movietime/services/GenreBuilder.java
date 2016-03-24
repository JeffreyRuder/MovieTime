package com.epicodus.movietime.services;

import android.util.Log;

import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;

/**
 * Created by Guest on 3/23/16.
 */
//public class GenreBuilder {
//    private static final String API_KEY = "12df8fd6a94674f402e47303a090863b";
//
//    public static String getTopTwoGenres(MovieDb movie) {
//        TmdbMovies movies = new TmdbApi(API_KEY).getMovies();
//        MovieDb detailMovie = movies.getMovie(movie.getId(), "en", TmdbMovies.MovieMethod.values());
//
//    }
//}