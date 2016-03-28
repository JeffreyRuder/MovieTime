package com.epicodus.movietime.ui;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.epicodus.movietime.R;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.ObjectUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.movito.themoviedbapi.model.MovieDb;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment implements View.OnClickListener {
    @Bind(R.id.movieNameTextView) TextView mMovieNameTextView;
    @Bind(R.id.movieBackdropImageView) ImageView mMovieBackdropImageView;
    @Bind(R.id.releaseDateTextView) TextView mReleaseDateTextView;
    @Bind(R.id.ratingTextView) TextView mRatingTextView;
    @Bind(R.id.overviewTextView) TextView mOverviewTextView;
    @Bind(R.id.inviteFriendButton) Button mInviteFriendButton;

    private MovieDb mMovie;
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

}
