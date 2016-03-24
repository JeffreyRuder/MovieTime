package com.epicodus.movietime.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.epicodus.movietime.ui.MovieDetailFragment;

import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;

public class MoviePagerAdapter extends FragmentPagerAdapter {
    private List<MovieDb> mMovies;

    public MoviePagerAdapter(FragmentManager fm, List<MovieDb> movies) {
        super(fm);
        mMovies = movies;
    }

    @Override
    public Fragment getItem(int position) {
        return MovieDetailFragment.newInstance(mMovies.get(position));
    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mMovies.get(position).getTitle();
    }

}
