package com.jtrmb.movietime.services;


import android.content.Context;

import com.jtrmb.movietime.R;

import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.MovieImages;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

public class SearchService {
    private static String API_KEY;
    private static final String LANGUAGE = "en";
    private static final boolean INCLUDE_ADULT = false;

    public static List<MovieDb> movieSearch(String searchQuery, int searchYear, int pageNumber, Context context) {

        API_KEY = context.getResources().getString(R.string.tmdb);
        TmdbApi api = new TmdbApi(API_KEY);
        TmdbSearch searcher = new TmdbSearch(api);
        MovieResultsPage resultsPage = searcher.searchMovie(searchQuery, searchYear, LANGUAGE, INCLUDE_ADULT, pageNumber);
        return resultsPage.getResults();
    }

    public static MovieImages getImages(int id, Context context) {
        API_KEY = context.getResources().getString(R.string.tmdb);
        TmdbApi api = new TmdbApi(API_KEY);
        TmdbMovies movies = new TmdbMovies(api);
        return movies.getImages(id, "en");
    }

}
