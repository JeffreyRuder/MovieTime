package com.epicodus.movietime.ui;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.epicodus.movietime.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.movito.themoviedbapi.model.Artwork;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.MovieImages;

public class PosterDetailFragment extends Fragment implements View.OnClickListener {
    @Bind(R.id.posterImageView) ImageView mPosterImageView;
    @Bind(R.id.setWallpaperButton) Button mSetAsWallpaperButton;

    private Artwork mArtwork;

    public static PosterDetailFragment newInstance(Artwork image) {
        PosterDetailFragment posterDetailFragment = new PosterDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("image", image);
        posterDetailFragment.setArguments(args);
        return posterDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mArtwork = (Artwork) getArguments().getSerializable("image");

    }

    @Override
    public void onClick(View v) {
        if (v == mSetAsWallpaperButton) {
            Resources res = getResources();
            setWallpaper(String.format(res.getString(R.string.poster_url_big), mArtwork.getFilePath()));
//
//            try {
//                Bitmap result = Picasso.with(getContext())
//                        .load(String.format(res.getString(R.string.poster_url_big), mArtwork.getFilePath()))
//                        .get();
//
//                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getContext());
//
//                wallpaperManager.setBitmap(result);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }



//            Intent setWallpaperIntent = new Intent(wallpaperManager.getCropAndSetWallpaperIntent(Uri.parse(String.format(res.getString(R.string.poster_url_big), mArtwork.getFilePath()))));
//            startActivity(setWallpaperIntent);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poster_detail, container, false);
        ButterKnife.bind(this, view);

        mSetAsWallpaperButton.setOnClickListener(this);

        Resources res = getResources();

        Picasso.with(getContext())
                .load(String.format(res.getString(R.string.poster_url_big), mArtwork.getFilePath()))
                .centerCrop()
                .fit()
                .into(mPosterImageView);

        return view;
    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getContext());
            try {
                wallpaperManager.setBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    private void setWallpaper(String url) {
        Picasso.with(getContext()).load(url).into(target);
    }

    @Override
    public void onDestroy() {  // could be in onPause or onStop
        Picasso.with(getContext()).cancelRequest(target);
        super.onDestroy();
    }

}
