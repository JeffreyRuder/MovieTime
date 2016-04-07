package com.epicodus.movietime.ui;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.epicodus.movietime.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.movito.themoviedbapi.model.Artwork;

public class PosterDetailFragment extends Fragment implements View.OnClickListener {
    @Bind(R.id.posterImageView) ImageView mPosterImageView;
    @Bind(R.id.setWallpaperButton) Button mSetAsWallpaperButton;

    private Artwork mArtwork;
    private ProgressDialog mProgressDialog;

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
        initializeProgressDialog();
    }

    @Override
    public void onClick(View v) {
        if (v == mSetAsWallpaperButton) {
            Resources res = getResources();
            setWallpaper(String.format(res.getString(R.string.poster_url_big), mArtwork.getFilePath()));
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
                mProgressDialog.dismiss();
                Toast.makeText(getContext(), getString(R.string.saved_wallpaper), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            mProgressDialog.dismiss();
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            mProgressDialog.show();
        }
    };

    private void setWallpaper(String url) {
        Picasso.with(getContext()).load(url).into(target);
    }

    @Override
    public void onDestroy() {
        Picasso.with(getContext()).cancelRequest(target);
        super.onDestroy();
    }

    private void initializeProgressDialog() {
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setTitle("Loading");
        mProgressDialog.setMessage(getString(R.string.downloading_image));
        mProgressDialog.setCancelable(false);
    }


}
