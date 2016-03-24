package com.epicodus.movietime.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

//import com.epicodus.movietime.services.GenreBuilder;
import com.epicodus.movietime.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.movito.themoviedbapi.model.MovieDb;

/**
 * Created by Guest on 3/23/16.
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {
    private List<MovieDb> mMovies;
    private Context mContext;

    public MovieListAdapter(Context context, List<MovieDb> movies) {
        mContext = context;
        mMovies = movies;
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
        @Bind(R.id.movieGenre) TextView mMovieGenre;
        @Bind(R.id.movieRatingBar) RatingBar mMovieRatingBar;

        private Context mContext;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
        }

        public void bindMovie(MovieDb movie) {
            Resources res = mContext.getResources();

            if (movie.getPosterPath() != null) {
                Picasso.with(mContext).load(String.format(res.getString(R.string.poster_url), movie.getPosterPath())).into(mPosterImage);
            } else {
                Picasso.with(mContext).load(R.drawable.no_image).into(mPosterImage);
            }
            mMovieTitle.setText(movie.getTitle());
            mMovieReleaseDate.setText(movie.getReleaseDate());
//            mMovieGenre.setText(GenreBuilder.getTopTwoGenres(movie));
            if (movie.getVoteCount() > 0) {
                mMovieRatingBar.setRating(movie.getVoteAverage()/2);
            } else {
                mMovieRatingBar.setVisibility(View.INVISIBLE);
            }
        }
    }
}
