package com.epicodus.movietime.ui;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.epicodus.movietime.R;
import com.epicodus.movietime.adapters.MoviePagerAdapter;
import com.epicodus.movietime.adapters.PosterPagerAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.MovieImages;

public class MoviePosterActivity extends AppCompatActivity {

    @Bind(R.id.viewPager)
    ViewPager mViewPager;
    private PosterPagerAdapter adapterViewPager;
    MovieImages mImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_poster);
        ButterKnife.bind(this);

        Bundle posterBundle = getIntent().getBundleExtra("PosterBundle");
        mImages = (MovieImages) posterBundle.getSerializable("MovieImages");

        int startingPosition = 0;
        adapterViewPager = new PosterPagerAdapter(getSupportFragmentManager(), mImages);
        mViewPager.setAdapter(adapterViewPager);
        mViewPager.setCurrentItem(0);
    }
}
