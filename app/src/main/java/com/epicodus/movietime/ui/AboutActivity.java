package com.epicodus.movietime.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.epicodus.movietime.R;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.poweredByImageView) ImageView mPoweredByImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        Picasso.with(this).load(R.drawable.poweredbytmdb).fit().centerInside().into(mPoweredByImageView);
        mPoweredByImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mPoweredByImageView) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.themoviedb.org/"));
            startActivity(webIntent);
        }
    }
}
