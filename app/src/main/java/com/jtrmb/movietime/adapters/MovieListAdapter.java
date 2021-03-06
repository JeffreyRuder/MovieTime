package com.jtrmb.movietime.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jtrmb.movietime.R;
import com.jtrmb.movietime.ui.MovieDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.movito.themoviedbapi.model.MovieDb;


public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {
    private ArrayList<MovieDb> mMovies;
    private Context mContext;

    public MovieListAdapter(Context context, List<MovieDb> movies) {
        mContext = context;
        mMovies = new ArrayList<>(movies);
    }

    @Override
    public MovieListAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieListAdapter.MovieViewHolder holder, int position) {
        holder.bindMovie(mMovies.get(position));
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.posterImage) ImageView mPosterImage;
        @Bind(R.id.movieTitle) TextView mMovieTitle;
        @Bind(R.id.movieReleaseDate) TextView mMovieReleaseDate;
        @Bind(R.id.movieRatingBar) RatingBar mMovieRatingBar;
        @Bind(R.id.overviewSnippet) TextView mOverviewSnippet;

        private Context mContext;

        public MovieViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = getLayoutPosition();
                    Intent intent = new Intent(mContext, MovieDetailActivity.class);
                    intent.putExtra("position", itemPosition + "");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("movies", mMovies);
                    intent.putExtra("movieBundle", bundle);
                    mContext.startActivity(intent);
                }
            });
        }

        public void bindMovie(MovieDb movie) {
            Resources res = mContext.getResources();

            if (movie.getPosterPath() != null) {
                Picasso.with(mContext).load(String.format(res.getString(R.string.poster_url), movie.getPosterPath())).fit().centerCrop().into(mPosterImage);
            } else {
                Picasso.with(mContext).load(R.drawable.no_image).fit().centerCrop().into(mPosterImage);
            }
            mMovieTitle.setText(movie.getTitle());
            mMovieReleaseDate.setText(movie.getReleaseDate());
            if (movie.getOverview().length() > 80) {
                mOverviewSnippet.setText(movie.getOverview().substring(0, 80) + "...");
            } else {
                mOverviewSnippet.setText(movie.getOverview());
            }
            if (movie.getVoteCount() > 0) {
                mMovieRatingBar.setRating(movie.getVoteAverage()/2);
            } else {
                mMovieRatingBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void addMovies(List<MovieDb> newMovies) {
        int currentSize = mMovies.size();
        mMovies.addAll(newMovies);
        notifyItemRangeInserted(currentSize, newMovies.size());
    }
}
