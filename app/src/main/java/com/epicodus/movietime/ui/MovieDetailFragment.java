package com.epicodus.movietime.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epicodus.movietime.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.movito.themoviedbapi.model.MovieDb;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment {
    @Bind(R.id.movieNameTextView) TextView mMovieNameTextView;

    private MovieDb mMovie;

    public static MovieDetailFragment newInstance(MovieDb movie) {
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("movie", movie);
        movieDetailFragment.setArguments(args);
        return movieDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovie = (MovieDb) getArguments().getSerializable("movie");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, view);

        mMovieNameTextView.setText(mMovie.getTitle());
        return view;
    }

}
