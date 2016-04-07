package com.epicodus.movietime.ui;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epicodus.movietime.R;
import com.epicodus.movietime.services.SearchService;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.MovieImages;

public class MovieDetailFragment extends Fragment implements View.OnClickListener {
    @Bind(R.id.movieNameTextView) TextView mMovieNameTextView;
    @Bind(R.id.movieBackdropImageView) ImageView mMovieBackdropImageView;
    @Bind(R.id.releaseDateTextView) TextView mReleaseDateTextView;
    @Bind(R.id.ratingTextView) TextView mRatingTextView;
    @Bind(R.id.overviewTextView) TextView mOverviewTextView;
    @Bind(R.id.shareButton) Button mShareButton;
    @Bind(R.id.netflixButton) Button mNetflixButton;
    @Bind(R.id.scrollLinearLayout) LinearLayout mScrollLinearLayout;
    @Bind(R.id.posterButton) Button mPosterButton;

    private MovieDb mMovie;
    private MovieImages mImages;
    private static final int PICK_CONTACT_REQUEST = 1;

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
        new GetImagesTask().execute(mMovie);
        Resources res = view.getContext().getResources();
        mMovieNameTextView.setText(mMovie.getTitle());
        if (mMovie.getBackdropPath() != null) {
            Picasso.with(view.getContext()).load(String.format(res.getString(R.string.backdrop_url), mMovie.getBackdropPath())).fit().centerCrop().into(mMovieBackdropImageView);
        } else if (mMovie.getPosterPath() != null) {
            Picasso.with(view.getContext()).load(String.format(res.getString(R.string.backdrop_url), mMovie.getPosterPath())).into(mMovieBackdropImageView);
        } else {
            Picasso.with(view.getContext()).load(R.drawable.no_image).into(mMovieBackdropImageView);
        }
        mReleaseDateTextView.setText(mMovie.getReleaseDate());
        if (mMovie.getVoteCount() > 0) {
            mRatingTextView.setText(String.format(Locale.US, "%.2f", mMovie.getVoteAverage()/2) + "/5");
        } else {
            mRatingTextView.setVisibility(View.INVISIBLE);
        }
        mOverviewTextView.setText(mMovie.getOverview());

        mShareButton.setOnClickListener(this);
        mNetflixButton.setOnClickListener(this);
        mPosterButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == mShareButton) {
            new ShareImageTask().execute(mMovieBackdropImageView);
        }
        if (view == mNetflixButton) {
            try {
                Intent intent = new Intent(Intent.ACTION_SEARCH);
                intent.setClassName("com.netflix.mediaclient", "com.netflix.mediaclient.ui.search.SearchActivity");
                intent.putExtra("query", mMovie.getTitle());
                startActivity(intent);
            }
            catch(Exception e) {
                Toast.makeText(getContext(), "Please install the Netflix app!", Toast.LENGTH_SHORT).show();
            }
        }

        if (view == mPosterButton) {
            Intent intent = new Intent(getActivity(), MoviePosterActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("MovieImages", mImages);
            intent.putExtra("PosterBundle", bundle);
            startActivity(intent);
        }
    }

    private class GetImagesTask extends AsyncTask<MovieDb, Void, MovieImages> {
        @Override
        protected MovieImages doInBackground(MovieDb... params) {
            return SearchService.getImages(params[0].getId());
        }

        @Override
        protected void onPostExecute(final MovieImages result) {
            mImages = result;
            if (result.getPosters().size() > 0) {
                mPosterButton.setVisibility(View.VISIBLE);
                mPosterButton.setEnabled(true);
            }
        }
    }

    private class ShareImageTask extends AsyncTask<ImageView, Void, Uri> {
        @Override
        protected Uri doInBackground(ImageView... params) {
            return getLocalBitmapUri(params[0]);
        }

        @Override
        protected void onPostExecute(final Uri result) {
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(Intent.EXTRA_TEXT, mMovie.getTitle());
            if (result != null) {
                intent.putExtra(Intent.EXTRA_STREAM, result);
                intent.setType("image/*");
            } else {
                intent.putExtra(Intent.EXTRA_TEXT,
                        String.format(getResources().getString(R.string.backdrop_url), mMovie.getPosterPath()));
                intent.setType("text/plain");
            }
            startActivity(Intent.createChooser(intent, getString(R.string.how_to_share)));
        }
    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            File file =  new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

}
