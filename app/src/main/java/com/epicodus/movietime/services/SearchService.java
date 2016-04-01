package com.epicodus.movietime.services;


import android.content.Context;
import android.util.Log;

import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.MovieImages;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

public class SearchService {
    private static final String API_KEY = "12df8fd6a94674f402e47303a090863b";
    private static final String LANGUAGE = "en";
    private static final boolean INCLUDE_ADULT = false;

    public static List<MovieDb> movieSearch(String searchQuery, int searchYear, int pageNumber) {

        TmdbApi api = new TmdbApi(API_KEY);
        TmdbSearch searcher = new TmdbSearch(api);
        MovieResultsPage resultsPage = searcher.searchMovie(searchQuery, searchYear, LANGUAGE, INCLUDE_ADULT, pageNumber);
        return resultsPage.getResults();
    }

    public static MovieImages getImages(int id) {
        TmdbApi api = new TmdbApi(API_KEY);
        TmdbMovies movies = new TmdbMovies(api);
        return movies.getImages(id, "en");
    }

}
