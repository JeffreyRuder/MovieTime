package com.epicodus.movietime.ui;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.epicodus.movietime.R;
import com.epicodus.movietime.adapters.MovieListAdapter;
import com.epicodus.movietime.services.SearchService;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

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
    @Bind(R.id.inviteFriendButton) Button mInviteFriendButton;
    @Bind(R.id.scrollLinearLayout) LinearLayout mScrollLinearLayout;

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

        new GetImagesTask().execute(mMovie);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, view);
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
            mRatingTextView.setText(String.format("%.2f", mMovie.getVoteAverage()/2) + "/5");
        } else {
            mRatingTextView.setVisibility(View.INVISIBLE);
        }
        mOverviewTextView.setText(mMovie.getOverview());
        mInviteFriendButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == mInviteFriendButton) {
            Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
            pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contactUri = data.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

                try {
                    Cursor cursor = this.getContext().getContentResolver().query(contactUri, projection, null, null, null);
                    cursor.moveToFirst();

                    int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String number = cursor.getString(column);
                    cursor.close();
                    sendSMS(this.getView(), number, mMovie);
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }

            }
        }
    }

    public void sendSMS(View v, String number, MovieDb movie) {
        String movieUrl = "https://www.themoviedb.org/movie/" + movie.getId();

        Uri uri = Uri.parse("smsto:" + number);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body",
                "Let's go see " + movie.getTitle() + "! " + movieUrl);
        startActivity(intent);
    }

    private class GetImagesTask extends AsyncTask<MovieDb, Void, MovieImages> {
        @Override
        protected MovieImages doInBackground(MovieDb... params) {
            MovieImages images = SearchService.getImages(params[0].getId());
            return images;
        }

        @Override
        protected void onPostExecute(MovieImages result) {
            int size = result.getPosters().size();
            for (int i = 0; i < 10; i ++) {
                if (size > 0 && size > i) {

                    Object object = result.getPosters().get(i);

                    ImageView imageView = new ImageView(getContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, 0, 48);
                    imageView.setLayoutParams(params);
                    mScrollLinearLayout.addView(imageView);

                    Resources res = getResources();
                    Picasso.with(getContext())
                            .load(String.format(res.getString(R.string.poster_url_big), result.getPosters().get(i).getFilePath()))
                            .into(imageView);

                }
            }
        }
    }
}
