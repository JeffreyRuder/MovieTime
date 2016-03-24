package com.epicodus.movietime.ui;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.epicodus.movietime.R;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.movito.themoviedbapi.model.MovieDb;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment {
    @Bind(R.id.movieNameTextView) TextView mMovieNameTextView;
    @Bind(R.id.movieBackdropImageView) ImageView mMovieBackdropImageView;
    @Bind(R.id.releaseDateTextView) TextView mReleaseDateTextView;
    @Bind(R.id.ratingTextView) TextView mRatingTextView;
    @Bind(R.id.overviewTextView) TextView mOverviewTextView;
    @Bind(R.id.inviteFriendButton) Button mInviteFriendButton;

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
        Resources res = view.getContext().getResources();

        mMovieNameTextView.setText(mMovie.getTitle());
        if (mMovie.getBackdropPath() != null) {
            Picasso.with(view.getContext()).load(String.format(res.getString(R.string.backdrop_url), mMovie.getBackdropPath())).fit().centerCrop().into(mMovieBackdropImageView);
        } else {
            Picasso.with(view.getContext()).load(R.drawable.no_image).into(mMovieBackdropImageView);
        }
        mReleaseDateTextView.setText(mMovie.getReleaseDate());
        if (mMovie.getVoteCount() > 0) {
            mRatingTextView.setText(String.format("%.2f", mMovie.getVoteAverage()/2) + "/5");
        } else {
            mRatingTextView.setVisibility(View.INVISIBLE);
        }
        mOverviewTextView.setText(mMovie.getOverview());
        return view;
    }

}
