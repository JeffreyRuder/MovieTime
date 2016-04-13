package com.jtrmb.movietime.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jtrmb.movietime.R;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.poweredByImageView) ImageView mPoweredByImageView;
    @Bind(R.id.githubLinkTextView) TextView mGithubTextView;

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
        if (v == mGithubTextView) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/JeffreyRuder/MovieTime"));
            startActivity(webIntent);
        }
    }
}
